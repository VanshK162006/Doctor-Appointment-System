package in.app.doctorAppointmentSystem.controller;

import in.app.doctorAppointmentSystem.dto.LoginDto;
import in.app.doctorAppointmentSystem.dto.PatientRequestDto;
import in.app.doctorAppointmentSystem.dto.PatientResponseDto;
import in.app.doctorAppointmentSystem.repository.PatientDao;
import in.app.doctorAppointmentSystem.services.PatientService;
import jakarta.validation.Valid;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
public class PatientController {
    private final PatientService patientService;

        public PatientController(PatientService patientService) {
            this.patientService = patientService;
        }

        @PostMapping("/patient/register")
        public ResponseEntity<PatientResponseDto> register(@Valid @RequestBody PatientRequestDto patientRequestDto){
            PatientResponseDto p1=patientService.register(patientRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(p1);
        }

}
