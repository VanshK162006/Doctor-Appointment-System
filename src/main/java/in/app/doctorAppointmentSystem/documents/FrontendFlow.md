# Frontend Part of Application

## 1. Login Page
- When the user opens the site/app, they are shown the **Login page** by default.
- User enters **username** and **password**.
- On pressing the **Submit** button, a `POST` request is sent with a `LoginDto` body to the Login Controller method:
  ```java
  login(@RequestBody LoginDto login);
  ```

## 2. Registration Links
- Below the Submit button, the user sees the text:
  > Don't have an Account?  
  > **Register as Doctor** | **Register as Patient**

## 3. Registration Flow
- Clicking **Register as Doctor** or **Register as Patient** opens the respective registration form.
- User fills in the fields and clicks **Submit**.
- A `POST` request is sent with:
  - `DoctorRequestDto` → Doctor Controller (`register()` method)
  - `PatientRequestDto` → Patient Controller (`register()` method)

## 4. Post Login / Registration
- On successful login or registration, the app identifies the user role:
  - **Patient** → Patient Dashboard page
  - **Doctor** → Doctor Dashboard page

---

## 5. Patient Dashboard Page

### i) Doctors Page
- Patient can switch between two pages: **Doctors** and **Appointments**.
- On the **Doctors** page, a list of doctors is fetched from the **Patient Dashboard Controller**.
- Clicking on any doctor shows doctor details (in a dropdown or information panel) with two buttons:
  - **Book Appointment**
  - **Cancel**

### ii) Booking an Appointment
- Clicking **Book Appointment** navigates to the **Available Slots** page.
- Patient sees a list of available slots for that specific doctor (fetched via **Slot Controller**).
- Selecting a slot prompts a confirmation dialog.
- On confirmation, the appointment is booked using the **Appointment Controller**.

### iii) Appointments Page
- Patient can view all appointments (**Cancelled**, **Completed**, **Scheduled**) fetched via the **Patient Dashboard Controller**.
- Clicking on a **Completed** appointment opens a non-editable form showing:
  - Medicines and their time patterns
  - Doctor name
  - Patient name
  - Reason  
  (Data fetched via **Prescription Controller**)

---

## 6. Doctor Dashboard Page

### i) Appointments List
- Doctor sees a single page containing a list of appointments booked by patients.
- When a patient visits, the doctor selects the corresponding appointment.
- Doctor enters a list of medicines and their respective time patterns.
- This data is sent as a `POST` request via the **Prescription Controller**.

---

**Congratulations, you have completed the Frontend Flow.**
```