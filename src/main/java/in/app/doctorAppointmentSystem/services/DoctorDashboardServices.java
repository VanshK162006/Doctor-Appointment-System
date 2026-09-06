package in.app.doctorAppointmentSystem.services;

import in.app.doctorAppointmentSystem.Entity.Doctor;
import in.app.doctorAppointmentSystem.dto.DoctorAppointmentResponseDto;
import in.app.doctorAppointmentSystem.dto.PatientResponseDto;
import in.app.doctorAppointmentSystem.repository.AppointmentDao;
import in.app.doctorAppointmentSystem.repository.DoctorDao;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class DoctorDashboardServices {
    private final AppointmentDao appointmentDao;
    private final DoctorDao doctorDao;

    public DoctorDashboardServices(AppointmentDao appointmentDao, DoctorDao doctorDao) {
        this.appointmentDao = appointmentDao;
        this.doctorDao = doctorDao;
    }

    public List<DoctorAppointmentResponseDto> getTodayAppointment() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Doctor doctor = doctorDao.findByEmail(userDetails.getUsername());
        return appointmentDao.getTodayAppointmentsOfDoctor(doctor.getId()).stream()
                .map(
                        (a1) -> DoctorAppointmentResponseDto.builder()
                                .id(a1.getId())
                                .availableFrom(a1.getSlot().getStart().format(DateTimeFormatter.ofPattern("h:mm a")))
                                .availableTo(a1.getSlot().getEnd().format(DateTimeFormatter.ofPattern("h:mm a")))
                                .reason(a1.getReason())
                                .patientResponseDto(PatientResponseDto.builder()
                                        .id(a1.getPatient().getId())
                                        .email(a1.getPatient().getEmail())
                                        .phone(a1.getPatient().getPhone())
                                        .name(a1.getPatient().getName())
                                        .build())
                                .build()
                ).toList();
    }
}
