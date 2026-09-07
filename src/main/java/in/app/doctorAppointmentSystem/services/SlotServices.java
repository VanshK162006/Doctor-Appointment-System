package in.app.doctorAppointmentSystem.services;

import in.app.doctorAppointmentSystem.Entity.Doctor;
import in.app.doctorAppointmentSystem.Entity.Slot;
import in.app.doctorAppointmentSystem.dto.SlotResponseDto;
import in.app.doctorAppointmentSystem.repository.DoctorDao;
import in.app.doctorAppointmentSystem.repository.SlotDao;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class SlotServices {
    private final SlotDao slotDao;
    private final DoctorDao doctorDao;

    public SlotServices(SlotDao slotDao, DoctorDao doctorDao) {
        this.slotDao = slotDao;
        this.doctorDao = doctorDao;
    }

    public void generateSlots(LocalTime availableFrom, LocalTime availableTo, Long doctorId) {
        LocalTime startTime = availableFrom;
        LocalTime endTime = startTime.plusMinutes(30);
        Doctor d1 = doctorDao.findById(doctorId);

        while (startTime.isBefore(availableTo) && endTime.isBefore(availableTo)) {
            Slot s1 = Slot.builder()
                    .start(startTime)
                    .end(endTime)
                    .doctor(d1)
                    .isBooked(false)
                    .build();

            slotDao.save(s1);
            startTime = endTime;
            endTime = startTime.plusMinutes(30);
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void makeAllSlotsNonBooked() {
        slotDao.makeSlotsNonBooked();
    }


    public List<SlotResponseDto> getAvailableSlots(Long doctorId) {
        return slotDao.getAvailableSlots(doctorId).stream()
                .map((a) -> SlotResponseDto.builder()
                        .id(a.getId())
                        .startTime(a.getStart().format(DateTimeFormatter.ofPattern("h:mm a")))
                        .endTime(a.getEnd().format(DateTimeFormatter.ofPattern("h:mm a")))
                        .doctorId(a.getDoctor().getId())
                        .doctorEmail(a.getDoctor().getEmail())
                        .build()
                ).toList();
    }

    public void makeSlotBooked(Long slotId) {
        slotDao.makeSlotBooked(slotId);
    }

}
