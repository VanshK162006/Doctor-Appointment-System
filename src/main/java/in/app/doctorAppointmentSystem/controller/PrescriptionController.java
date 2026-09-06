package in.app.doctorAppointmentSystem.controller;

import in.app.doctorAppointmentSystem.dto.MedicinesRequestDto;
import in.app.doctorAppointmentSystem.dto.PrescriptionDto;
import in.app.doctorAppointmentSystem.services.PrescriptionServices;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/doctor_appointment_system/prescription")
public class PrescriptionController {
    private final PrescriptionServices prescriptionServices;

    public PrescriptionController(PrescriptionServices prescriptionServices) {
        this.prescriptionServices = prescriptionServices;
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public ResponseEntity<PrescriptionDto> addPrescription(@RequestParam Long appointmentId, @RequestBody List<MedicinesRequestDto> medicines){
        PrescriptionDto dto=prescriptionServices.addPrescription(appointmentId,medicines);
        return ResponseEntity.ok(dto);
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public ResponseEntity<PrescriptionDto> getPrescription(@RequestParam Long appointmentId){
        PrescriptionDto dto=prescriptionServices.getPrescription(appointmentId);
        return ResponseEntity.ok(dto);
    }
}
