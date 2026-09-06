package in.app.doctorAppointmentSystem.controller;

import in.app.doctorAppointmentSystem.dto.DoctorResponseDto;
import in.app.doctorAppointmentSystem.dto.PatientAppointmentResponseDto;
import in.app.doctorAppointmentSystem.services.AppointmentServices;
import in.app.doctorAppointmentSystem.services.PatientDashboardServices;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/doctor_appointment_system/dashboard/patient")
public class PatientDashboardController {
    private final PatientDashboardServices patientDashboardServices;
    private final AppointmentController appointmentController;

    public PatientDashboardController(PatientDashboardServices patientDashboardServices, AppointmentServices appointmentServices, AppointmentController appointmentController) {
        this.patientDashboardServices = patientDashboardServices;
        this.appointmentController = appointmentController;
    }

    @GetMapping("/list_of_doctors")
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public ResponseEntity<List<DoctorResponseDto>> getListOfDoctor() {
        List<DoctorResponseDto> doctorResponseDtos = patientDashboardServices.getAllDoctors();
        return ResponseEntity.ok(doctorResponseDtos);
    }

    @GetMapping("/list_of_appointment")
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public ResponseEntity<List<PatientAppointmentResponseDto>> getAppointmentsOfPatient( Long patientId) {
        return ResponseEntity.ok(patientDashboardServices.getTodayAppointment());
    }

}
