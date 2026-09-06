# Doctor Appointment System

A full-stack appointment booking system for clinics — patients browse doctors,
book 30-minute time slots, and doctors write prescriptions after the visit.
Built with a Spring Boot + Hibernate REST API and a vanilla HTML/CSS/JS
frontend, secured end-to-end with JWT.


## Tech Stack

`Java 21` · `Spring Boot` · `Spring Security` · `Hibernate (raw, no Spring Data JPA)` · `MySQL` · `JWT` · `HTML/CSS/JS`

## Repository Structure

```
.
├── backend/    Spring Boot REST API — see backend/README.md for full setup,
│               architecture notes, and the complete API reference
└── frontend/   Static HTML/CSS/JS pages — open frontend/index.html directly
                in a browser once the backend is running
```

## Quick Start

**1. Backend**
```bash
cd backend
# create a MySQL database named doctor_appointment_system, then:
export DB_USERNAME=root
export DB_PASSWORD=your_mysql_password
export JWT_SECRET=some-long-random-string
./mvnw spring-boot:run
```
Full setup details, the data model, and the complete API reference are in
[`backend/README.md`](backend/README.md).

**2. Frontend**
```bash
cd frontend
open index.html   # or just double-click it / drag it into a browser
```

## Features

- Dual-role auth (patient / doctor) behind a single login, JWT-secured
- Doctor registration auto-generates 30-minute bookable slots from their working hours
- Patients browse doctors, view open slots, and book with a reason for visit
- Patients can cancel scheduled appointments and view prescriptions for completed ones
- Doctors see their appointment list and write prescriptions (medicine + time pattern) per visit
