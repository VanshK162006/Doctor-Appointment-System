package in.app.doctorAppointmentSystem.controller;

import in.app.doctorAppointmentSystem.dto.DoctorAppointmentResponseDto;
import in.app.doctorAppointmentSystem.services.DoctorDashboardServices;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/doctor_appointment_system/dashboard/doctor")
public class DoctorDashboardController {
    private final DoctorDashboardServices doctorDashboardServices;

    public DoctorDashboardController(DoctorDashboardServices doctorDashboardServices) {
        this.doctorDashboardServices = doctorDashboardServices;
    }

    @GetMapping("/get_all_appointments")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public ResponseEntity<List<DoctorAppointmentResponseDto>> getTodayAppointments(){
        return ResponseEntity.ok(doctorDashboardServices.getTodayAppointment());
    }
}
