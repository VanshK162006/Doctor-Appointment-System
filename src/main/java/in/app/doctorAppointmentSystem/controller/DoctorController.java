package in.app.doctorAppointmentSystem.controller;

import in.app.doctorAppointmentSystem.dto.*;
import in.app.doctorAppointmentSystem.services.DoctorServices;
import in.app.doctorAppointmentSystem.services.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.CredentialNotFoundException;

@RestController
@RequestMapping("/api/doctor_appointment_system")
@Validated
public class DoctorController {
    private final DoctorServices doctorServices;

    public DoctorController(DoctorServices doctorServices) {
        this.doctorServices = doctorServices;
    }

    @PostMapping("/doctor/register")
    public ResponseEntity<DoctorResponseDto> register(@Valid @RequestBody DoctorRequestDto dto) {
        DoctorResponseDto d1 = doctorServices.registerDoctor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(d1);
    }

}
