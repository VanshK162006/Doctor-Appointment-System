package in.app.doctorAppointmentSystem.controller;

import in.app.doctorAppointmentSystem.Entity.Slot;
import in.app.doctorAppointmentSystem.dto.SlotResponseDto;
import in.app.doctorAppointmentSystem.services.SlotServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/doctor_appointment_system/slots")
public class SlotController {
    private final SlotServices slotServices;

    public SlotController(SlotServices slotServices) {
        this.slotServices = slotServices;
    }

    @GetMapping(value = "/{doctorId}")
    public ResponseEntity<List<SlotResponseDto>> getListOfSlots(@PathVariable(name = "doctorId") Long doctorId){
        List<SlotResponseDto> slotList=slotServices.getAvailableSlots(doctorId);
        return ResponseEntity.ok(slotList);
    }
}
