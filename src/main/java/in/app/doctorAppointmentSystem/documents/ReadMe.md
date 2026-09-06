# Doctor Appointment System — REST API

A Spring Boot backend for booking doctor appointments. Patients and Doctors both
register/login through the same endpoint, JWT secures everything after login,
and doctors' available time is auto-sliced into 30-minute bookable slots.

---

## 1. Quick Mental Model (read this if you forget everything else)

- **Two kinds of users, one login**: `Patient` and `Doctor` are separate DB
  entities, but both log in via the same `/login` endpoint. The system checks
  Patient table first, then Doctor table, for a matching email.
- **No Spring Data JPA.** This project manually builds a Hibernate
  `SessionFactory` bean (`HibernateConfigurtion.java`) and hand-writes DAOs
  (`BaseRepository<ENTITY,ID>` + per-entity DAO classes) using raw Hibernate
  `Session`/`Transaction`/HQL. There is no `spring-boot-starter-data-jpa` and
  no `JpaRepository` anywhere.
- **Slots are auto-generated at doctor registration.** When a doctor signs up
  with `availableFrom`/`availableTo`, `SlotServices.generateSlots()` immediately
  creates 30-minute `Slot` rows for that whole window.
- **Booking = claim a slot.** Booking an appointment creates an `Appointment`
  row linked to a `Slot`, then flips that slot's `isBooked` to `true`.
- **Security = JWT + role/authority checks.** `JwtAuthFilter` reads the
  `Authorization: Bearer <token>` header on every request and populates the
  Spring Security context. Endpoint-level access is then restricted with
  `@PreAuthorize`.

---

## 2. Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.1.0 (`spring-boot-starter-webmvc`) |
| Security | Spring Security 6, JWT (`io.jsonwebtoken` / `jjwt` 0.13.0), BCrypt |
| Persistence | Raw Hibernate ORM 7.4.1 (SessionFactory bean, hand-written DAOs — **not** Spring Data JPA) |
| Database | MySQL (`mysql-connector-j`) |
| Validation | Jakarta Bean Validation (`jakarta.validation-api`) |
| Boilerplate reduction | Lombok |
| Build tool | Maven (wrapper included: `mvnw` / `mvnw.cmd`) |

---

## 3. Project Structure

```
src/main/java/in/app/doctorAppointmentSystem/
├── DoctorAppointmentSystemApplication.java   # main() entry point
├── AppointmentStatus.java                    # enum: SCHEDULED, CANCELLED, COMPLETED
│
├── Entity/            # JPA/Hibernate entities (the DB schema, via hbm2ddl=update)
│   ├── Patient.java
│   ├── Doctor.java
│   ├── Slot.java
│   ├── Appointment.java
│   ├── Prescription.java
│   └── Medicines.java
│
├── dto/                # Request/response shapes exposed over REST
├── Mapper/             # Manual Entity <-> DTO converters (DoctorMapper, PatientMapper)
│
├── repository/         # Hand-written DAOs on top of raw Hibernate Session
│   ├── BaseRepository.java     # generic save/update/delete/findById
│   ├── PatientDao / DoctorDao / SlotDao / AppointmentDao / PrescriptionDao / MedicinesDao
│
├── services/           # Business logic
│   ├── PatientService.java           # patient registration
│   ├── DoctorServices.java           # doctor registration (+ triggers slot generation)
│   ├── LoginServices.java            # shared login for both roles
│   ├── SlotServices.java             # slot generation / booking / freeing
│   ├── AppointmentServices.java      # book / cancel / list appointments (patient side)
│   ├── PatientDashboardServices.java # doctor list + patient's own appointments
│   ├── DoctorDashboardServices.java  # doctor's today appointments
│   ├── PrescriptionServices.java     # add/view prescription for an appointment
│   ├── UserDetailsServiceImpl.java   # Spring Security UserDetailsService (checks Patient then Doctor)
│
├── controller/         # REST endpoints (see full API table below)
├── config/
│   ├── HibernateConfigurtion.java    # builds the SessionFactory bean manually
│   └── SecurityConfiguration.java    # filter chain, permitAll rules, password encoder
└── utils/
    ├── JwtServices.java          # generate/validate/parse JWT
    ├── JwtAuthFilter.java        # runs once per request, populates SecurityContext
    └── GlobalExceptionHandler.java  # @RestControllerAdvice — turns exceptions into JSON error bodies
```

---

## 4. Data Model / Entity Relationships

```
Doctor (1) ───< Slot (many)                Patient (1) ───< Appointment (many)
Slot   (1) ─── Appointment (0..1, OneToOne)
Appointment (1) ─── Prescription (0..1, OneToOne)
Prescription (1) ───< Medicines (many)
```

| Entity | Key fields | Notes |
|---|---|---|
| `Patient` | name, email (unique), phone (unique, 10 digits), password (BCrypt) | Table explicitly named `patients` |
| `Doctor` | name, email (unique), password, specialization, yearsOfExperience, phoneNo (unique), Address, availableFrom, availableTo | `slot` list is `@JsonIgnore`d so it never serializes in responses |
| `Slot` | start, end (LocalTime), isBooked, doctor (ManyToOne), `@Version` (optimistic locking) | Auto-generated in 30-min chunks when a doctor registers |
| `Appointment` | patient (ManyToOne), slot (OneToOne), bookedAt (auto timestamp), appointmentStatus, reason | Status enum: SCHEDULED / CANCELLED / COMPLETED |
| `Prescription` | appointment (OneToOne), medicines (OneToMany), appointmentDate (auto), forWhatPurpose | Unique constraint on (doctor, patient, appointment_date, purpose) — **note:** the constraint references columns `doctor`/`patient` that don't actually exist on this table (see Known Issues) |
| `Medicines` | medicines (name), timePattern, prescription (ManyToOne) | |

**Schema creation:** `hibernate.hbm2ddl.auto=update` — tables are created/altered
automatically on startup from the entity classes. You do **not** need to write
SQL DDL yourself; just create an empty database and start the app.

**Naming heads-up:** because this project bypasses Spring Boot's JPA
auto-configuration and builds its own `Configuration` object by hand, table
names follow Hibernate's default (not Spring Boot's snake_case) naming —
expect tables that closely mirror entity class names (`Doctor`, `Slot`,
`Appointment`, `Prescription`, `Medicines`), except `Patient` which is pinned
to `patients` via `@Table`.

---

## 5. Application Flow (what actually happens, step by step)

1. **Doctor registers** → `POST /doctor/register` → password is BCrypt-hashed →
   saved → `SlotServices.generateSlots()` immediately creates 30-minute `Slot`
   rows spanning `availableFrom` → `availableTo`.
2. **Patient registers** → `POST /patient/register` → phone validated to be
   exactly 10 digits → password hashed → saved.
3. **Either logs in** → `POST /login` with `{username, password}` (username =
   email). `LoginServices` builds a Spring `AuthenticationProvider` call,
   which delegates to `UserDetailsServiceImpl` (checks Patient table, then
   Doctor table). On success, a signed JWT is returned as the raw response
   body (not wrapped in JSON) containing the user's email as subject and
   their granted authorities as a claim.
4. **Every subsequent request** carries `Authorization: Bearer <token>`.
   `JwtAuthFilter` verifies the signature + expiry, loads the user again via
   `UserDetailsServiceImpl`, and sets the Spring Security context — this is
   what `@PreAuthorize` and `SecurityContextHolder.getContext()` calls
   downstream rely on.
5. **Patient browses doctors** → `GET /dashboard/patient/list_of_doctors`.
6. **Patient views a doctor's open slots** → `GET /slots/{doctorId}` → only
   slots where `isBooked = false` are returned.
7. **Patient books a slot** → `POST /appointment/book` `{slotId, reason}` →
   creates an `Appointment` (status `SCHEDULED`) → flips the `Slot.isBooked`
   to `true`.
8. **Patient cancels** → `PUT /appointment/cancel/{id}` → sets status to
   `CANCELLED` (see Known Issues — the slot-freeing part of this has a bug).
9. **Doctor views today's appointments** → `GET /dashboard/doctor/get_all_appointments`.
10. **Doctor writes a prescription** → `POST /prescription?appointmentId=...`
    with a list of medicines → old prescription for that appointment (if any)
    is deleted and replaced → appointment status flipped to `COMPLETED`.
11. **Patient views prescription** → `GET /prescription?appointmentId=...`.

> Your own `readMe/FlowOfApp.txt` describes an intended UI (login page with
> "Register as User / Register as Doctor" links, doctor list with a dropdown
> for "Available Appointment" vs "See Details"). That UI doesn't exist yet —
> it's the plan for the frontend, not something already built. Steps 5–8
> above are the actual backend support for that plan.

---

## 6. REST API Reference

Base path: `/api/doctor_appointment_system`

| Method | Endpoint | Auth required | Role/Authority needed | Body | Notes |
|---|---|---|---|---|---|
| POST | `/patient/register` | No (permitAll) | — | `PatientRequestDto` | 201 Created, returns `PatientResponseDto` |
| POST | `/doctor/register` | No (permitAll) | — | `DoctorRequestDto` | 201 Created, triggers slot generation |
| POST | `/login` | No (permitAll) | — | `LoginDto {username, password}` | Returns raw JWT string, 200 |
| GET | `/dashboard/patient/list_of_doctors` | Yes | `ROLE_PATIENT` ⚠️ broken | — | List of all doctors |
| GET | `/dashboard/patient/list_of_appointment` | Yes | `ROLE_PATIENT` ⚠️ broken | — | ⚠️ has an unused/invalid `@PathVariable` — will error, see below |
| GET | `/slots/{doctorId}` | Yes (falls under default `authenticated()`) | any logged-in user | — | Open (unbooked) slots for that doctor |
| POST | `/appointment/book` | Yes | `CRUD_APPOINTMENT` ⚠️ broken | `AppointmentRequestDto {slotId, reason}` | 202 Accepted |
| GET | `/appointment/get_of_each_patient/{id}` | Yes | `CRUD_APPOINTMENT` ⚠️ broken | — | |
| PUT | `/appointment/cancel/{id}` | Yes | `CRUD_APPOINTMENT` ⚠️ broken | — | ⚠️ frees the wrong slot, see Known Issues |
| GET | `/dashboard/doctor/get_all_appointments` | Yes | `ROLE_DOCTOR` ⚠️ broken | — | Doctor's appointments |
| POST | `/prescription?appointmentId=` | Yes | `ROLE_DOCTOR` ⚠️ broken | `List<MedicinesRequestDto>` | Replaces any existing prescription |
| GET | `/prescription?appointmentId=` | Yes | `ROLE_PATIENT` ⚠️ broken | — | |

Every ⚠️ above is explained precisely in Section 7 — don't just take my word
for "broken," the reasoning is there so you can verify it yourself later.

---

## 7. How to Run It

### Prerequisites
- JDK 21
- MySQL Server running locally (or reachable)
- No need to install Maven — this project ships the wrapper (`./mvnw`)

### 1. Create the database
```sql
CREATE DATABASE doctor_appointment_system;
```
No need to create tables — `hibernate.hbm2ddl.auto=update` builds/updates
them from the entities automatically on startup.

### 2. Configure `src/main/resources/application.properties`
```properties
application.db.url = jdbc:mysql://localhost:3306/doctor_appointment_system
application.db.username = <your-mysql-username>
application.db.password = <your-mysql-password>

application.jwt.key=<any-long-random-string>
application.jwt.expiry=3600000
```
(`application.jwt.expiry` is in milliseconds — `3600000` = 1 hour.)

### 3. Run the app
```bash
./mvnw spring-boot:run
```
or build a jar and run it directly:
```bash
./mvnw clean package -DskipTests
java -jar target/doctorAppointmentSystem-0.0.1-SNAPSHOT.jar
```
Default port: **8080**. Base API path: `http://localhost:8080/api/doctor_appointment_system`.

### 4. Quick smoke test
```bash
# Register a patient
curl -X POST http://localhost:8080/api/doctor_appointment_system/patient/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test Patient","email":"patient@test.com","phone":9876543210,"password":"password123"}'

# Login
curl -X POST http://localhost:8080/api/doctor_appointment_system/login \
  -H "Content-Type: application/json" \
  -d '{"username":"patient@test.com","password":"password123"}'
# -> returns a raw JWT string. Use it as: Authorization: Bearer <token>
```

---

## 9. Suggested Next Steps
1. Fix the `hasRole`/`hasAuthority` mismatches (Section 7) — nothing
   role-protected works correctly until this is fixed.
2. Add CORS configuration — required before any browser-based frontend can
   call this API.
3. Fix the slot-unbooking bug in `cancelAppointment`.
4. Remove/fix the invalid `@PathVariable` in `PatientDashboardController`.
5. Move DB credentials and JWT secret to environment variables.
6. Decide if the dead `getTodayAppointmentsOfDoctor` stub in
   `AppointmentServices` should be deleted or actually implemented.