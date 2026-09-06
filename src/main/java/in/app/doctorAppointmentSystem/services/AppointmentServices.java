package in.app.doctorAppointmentSystem.services;

import in.app.doctorAppointmentSystem.AppointmentStatus;
import in.app.doctorAppointmentSystem.Entity.Appointment;
import in.app.doctorAppointmentSystem.Entity.Doctor;
import in.app.doctorAppointmentSystem.Entity.Patient;
import in.app.doctorAppointmentSystem.Entity.Slot;
import in.app.doctorAppointmentSystem.dto.PatientAppointmentResponseDto;
import in.app.doctorAppointmentSystem.repository.AppointmentDao;
import in.app.doctorAppointmentSystem.repository.DoctorDao;
import in.app.doctorAppointmentSystem.repository.PatientDao;
import in.app.doctorAppointmentSystem.repository.SlotDao;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AppointmentServices {
    private final PatientDao patientDao;
    private final AppointmentDao appointmentDao;
    private final SlotDao slotDao;
    private final DoctorDao doctorDao;

    public AppointmentServices(PatientDao patientDao, AppointmentDao appointmentDao, SlotDao slotDao, DoctorDao doctorDao) {
        this.patientDao = patientDao;
        this.appointmentDao = appointmentDao;
        this.slotDao = slotDao;
        this.doctorDao = doctorDao;
    }

    public PatientAppointmentResponseDto bookAnAppointment(Long slotId, String reason){
        UserDetails userDetails=(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String patientEmail=userDetails.getUsername();

        Patient patient=patientDao.findByEmail(patientEmail);
        Slot slot=slotDao.findById(slotId);
        Doctor doctor=slot.getDoctor();

        if(slot==null) throw new RuntimeException("Enter valid slotId!");

        Appointment appointment=Appointment.builder()
                .slot(slot)
                .patient(patient)
                .appointmentStatus(AppointmentStatus.SCHEDULED)
                .reason(reason)
                .build();

        Appointment apt=appointmentDao.save(appointment);
        slotDao.makeSlotBooked(slot.getId());

        return PatientAppointmentResponseDto.builder()
                .reason(apt.getReason())
                .availableFrom(apt.getSlot().getStart().format(DateTimeFormatter.ofPattern("h:mm a")))
                .availableTo(apt.getSlot().getEnd().format(DateTimeFormatter.ofPattern("h:mm a")))
                .doctorEmail(apt.getSlot().getDoctor().getEmail())
                .doctorName(apt.getSlot().getDoctor().getName())
                .status(apt.getAppointmentStatus())
                .build();
    }

    public String cancelAppointment(Long appointmentId){
        UserDetails userDetails=(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Patient patient=patientDao.findByEmail(userDetails.getUsername());

        Appointment appointment=appointmentDao.cancelAppointment(appointmentId,patient.getEmail());
        slotDao.makeSlotsNonBooked(appointment.getSlot().getId());

        return "Appointment Successfully cancelled!";
    }

    public List<PatientAppointmentResponseDto> getTodayAppointmentsOfPatient(Long patientId){
        return appointmentDao.getTodayAppointmentsOfPatient(patientId).stream().map(
                (each)-> PatientAppointmentResponseDto.builder()
                        .availableFrom(each.getSlot().getStart().format(DateTimeFormatter.ofPattern("h:mm a")))
                        .availableTo(each.getSlot().getEnd().format(DateTimeFormatter.ofPattern("h:mm a")))
                        .doctorEmail(each.getSlot().getDoctor().getEmail())
                        .doctorName(each.getSlot().getDoctor().getName())
                        .status(each.getAppointmentStatus())
                        .reason(each.getReason())
                        .build()
        ).toList();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void cancelAllScheduleAppointment(){
        appointmentDao.cancelAllScheduleAppointments();
    }
}
