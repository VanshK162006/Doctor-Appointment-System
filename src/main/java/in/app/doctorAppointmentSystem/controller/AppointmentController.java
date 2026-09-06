package in.app.doctorAppointmentSystem.controller;

import in.app.doctorAppointmentSystem.dto.AppointmentRequestDto;
import in.app.doctorAppointmentSystem.dto.PatientAppointmentResponseDto;
import in.app.doctorAppointmentSystem.services.AppointmentServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/doctor_appointment_system/appointment")
public class AppointmentController {

    private final AppointmentServices appointmentServices;

    public AppointmentController(AppointmentServices appointmentServices) {
        this.appointmentServices = appointmentServices;
    }

    @PostMapping("/book")
    @PreAuthorize("hasAuthority('CRUD_APPOINTMENT')")
    public ResponseEntity<PatientAppointmentResponseDto> bookAppointment(@RequestBody AppointmentRequestDto dto){
        PatientAppointmentResponseDto response=appointmentServices.bookAnAppointment(dto.getSlotId(),dto.getReason());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @GetMapping("/get_of_each_patient/{id}")
    @PreAuthorize("hasAuthority('CRUD_APPOINTMENT')")
    public ResponseEntity<List<PatientAppointmentResponseDto>> getAppointmentsOfPatient(@PathVariable("id") Long patientId){
        return ResponseEntity.ok(appointmentServices.getTodayAppointmentsOfPatient(patientId));
    }

    @PutMapping("/cancel/{id}")
    @PreAuthorize("hasAuthority('CRUD_APPOINTMENT')")
    public ResponseEntity<String> cancelAppointment(@PathVariable("id") Long id){
        String response=appointmentServices.cancelAppointment(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
