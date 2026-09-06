# Doctor Appointment System — Complete End-to-End Workflow & Backend Technical Docs

This documentation maps the Frontend User Interface flows directly to the corresponding Spring Boot Backend Controllers, Endpoints, DTOs, and Authorization Requirements.

---

## 1. Authentication & Onboarding Module

### 1.1 User Login
* Frontend Flow: The default landing screen captures credentials (username and password).
* Backend Endpoint: POST /api/doctor_appointment_system/login
* Controller: LoginController.login()
* Payload (LoginDto):
  * username
  * password
* Response: Returns a plain JWT Token string on success (HTTP 200 OK). Frontend stores this token to send in the Authorization: Bearer <token> header for subsequent requests.

---

### 1.2 User Registration
Users choose between registering as a Doctor or a Patient if they do not have an existing account.

#### A. Doctor Registration
* Frontend Flow: Navigates to Doctor Registration form, submits basic profile details.
* Backend Endpoint: POST /api/doctor_appointment_system/doctor/register
* Controller: DoctorController.register()
* Payload: DoctorRequestDto
* Response: DoctorResponseDto (HTTP 201 CREATED)

#### B. Patient Registration
* Frontend Flow: Navigates to Patient Registration form, submits demographic details.
* Backend Endpoint: POST /api/doctor_appointment_system/patient/register
* Controller: PatientController.register()
* Payload: PatientRequestDto
* Response: PatientResponseDto (HTTP 201 CREATED)

---

## 2. Patient Dashboard Module

### 2.1 Viewing Available Doctors & Slots
* Fetch Doctors List:
  * Frontend Flow: Displays all onboarded doctors on the "Doctors" tab.
  * Backend Endpoint: GET /api/doctor_appointment_system/dashboard/patient/list_of_doctors
  * Controller: PatientDashboardController.getListOfDoctor()
  * Security: Required Role ROLE_PATIENT
  * Response: List<DoctorResponseDto>

* Fetch Available Slots:
  * Frontend Flow: When the patient selects a doctor and clicks Book Appointment, the UI fetches open time slots.
  * Backend Endpoint: GET /api/doctor_appointment_system/slots/{doctorId}
  * Controller: SlotController.getListOfSlots()
  * Response: List<SlotResponseDto>

---

### 2.2 Appointment Booking & Cancellation
* Book Appointment:
  * Frontend Flow: Patient selects an open slot, provides a reason, and confirms.
  * Backend Endpoint: POST /api/doctor_appointment_system/appointment/book
  * Controller: AppointmentController.bookAppointment()
  * Security: Required Role CRUD_APPOINTMENT
  * Payload (AppointmentRequestDto):
    * slotId
    * reason
  * Response: PatientAppointmentResponseDto (HTTP 202 ACCEPTED)

* Cancel Appointment:
  * Frontend Flow: Patient chooses to cancel an existing upcoming slot.
  * Backend Endpoint: PUT /api/doctor_appointment_system/appointment/cancel/{id}
  * Controller: AppointmentController.cancelAppointment()
  * Security: Required Role CRUD_APPOINTMENT
  * Response: Confirmation String (HTTP 202 ACCEPTED)

---

### 2.3 Viewing Patient Appointments & Prescriptions
* Fetch Patient Appointments:
  * Frontend Flow: Displays appointment history (Scheduled, Completed, Cancelled) on the "Appointments" tab.
  * Backend Endpoint: GET /api/doctor_appointment_system/dashboard/patient/list_of_appointment
  * Controller: PatientDashboardController.getAppointmentsOfPatient()
  * Security: Required Role ROLE_PATIENT
  * Response: List<PatientAppointmentResponseDto>

* Fetch Prescription Details:
  * Frontend Flow: Clicking a Completed appointment opens a read-only modal showing medicines and dosage patterns.
  * Backend Endpoint: GET /api/doctor_appointment_system/prescription?appointmentId={appointmentId}
  * Controller: PrescriptionController.getPrescription()
  * Security: Required Role ROLE_PATIENT
  * Response: PrescriptionDto

---

## 3. Doctor Dashboard Module

### 3.1 Daily Schedule View
* Frontend Flow: Upon landing on the dashboard, the doctor views all appointments booked for the day.
* Backend Endpoint: GET /api/doctor_appointment_system/dashboard/doctor/get_all_appointments
* Controller: DoctorDashboardController.getTodayAppointments()
* Security: Required Role ROLE_DOCTOR
* Response: List<DoctorAppointmentResponseDto>

---

### 3.2 Issuing Prescriptions
* Frontend Flow: Doctor selects an active appointment, inputs prescribed medicines along with timing instructions, and submits.
* Backend Endpoint: POST /api/doctor_appointment_system/prescription?appointmentId={appointmentId}
* Controller: PrescriptionController.addPrescription()
* Security: Required Role ROLE_DOCTOR
* Payload: List<MedicinesRequestDto>
* Response: PrescriptionDto

---

## 4. Complete API & Security Mapping Reference

| Action / Feature | HTTP Method | Endpoint URI | Controller Method | Security Role |
| :--- | :--- | :--- | :--- | :--- |
| Login | POST | /api/doctor_appointment_system/login | LoginController.login() | Public |
| Register Patient | POST | /api/doctor_appointment_system/patient/register | PatientController.register() | Public |
| Register Doctor | POST | /api/doctor_appointment_system/doctor/register | DoctorController.register() | Public |
| Get Doctor List | GET | /api/doctor_appointment_system/dashboard/patient/list_of_doctors | PatientDashboardController.getListOfDoctor() | ROLE_PATIENT |
| Get Available Slots | GET | /api/doctor_appointment_system/slots/{doctorId} | SlotController.getListOfSlots() | Authenticated Users |
| Book Appointment | POST | /api/doctor_appointment_system/appointment/book | AppointmentController.bookAppointment() | CRUD_APPOINTMENT |
| Cancel Appointment | PUT | /api/doctor_appointment_system/appointment/cancel/{id} | AppointmentController.cancelAppointment() | CRUD_APPOINTMENT |
| Patient Appointments | GET | /api/doctor_appointment_system/dashboard/patient/list_of_appointment | PatientDashboardController.getAppointmentsOfPatient() | ROLE_PATIENT |
| Doctor Schedule | GET | /api/doctor_appointment_system/dashboard/doctor/get_all_appointments | DoctorDashboardController.getTodayAppointments() | ROLE_DOCTOR |
| Add Prescription | POST | /api/doctor_appointment_system/prescription | PrescriptionController.addPrescription() | ROLE_DOCTOR |
| View Prescription | GET | /api/doctor_appointment_system/prescription | PrescriptionController.getPrescription() | ROLE_PATIENT |
"""

file_path = "Doctor_Appointment_System_Documentation.txt"
with open(file_path, "w", encoding="utf-8") as f:
    f.write(markdown_text)

print(f"File saved successfully as {file_path}")