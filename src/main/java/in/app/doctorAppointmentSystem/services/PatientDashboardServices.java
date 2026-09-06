package in.app.doctorAppointmentSystem.services;

import in.app.doctorAppointmentSystem.Entity.Doctor;
import in.app.doctorAppointmentSystem.Entity.Patient;
import in.app.doctorAppointmentSystem.Mapper.DoctorMapper;
import in.app.doctorAppointmentSystem.dto.DoctorResponseDto;
import in.app.doctorAppointmentSystem.dto.PatientAppointmentResponseDto;
import in.app.doctorAppointmentSystem.repository.AppointmentDao;
import in.app.doctorAppointmentSystem.repository.DoctorDao;
import in.app.doctorAppointmentSystem.repository.PatientDao;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientDashboardServices {
    private final DoctorDao doctorDao;
    private final PatientDao patientDao;
    private final AppointmentDao appointmentDao;

    public PatientDashboardServices(DoctorDao doctorDao, PatientDao patientDao, AppointmentDao appointmentDao) {
        this.doctorDao = doctorDao;
        this.patientDao = patientDao;
        this.appointmentDao = appointmentDao;
    }

    public List<DoctorResponseDto> getAllDoctors() {
        List<Doctor> doctors = doctorDao.findAll();
        return doctors.stream().map(
                (d1) -> DoctorMapper.toDoctorResponseDto(d1)
        ).collect(Collectors.toList());
    }

    public List<PatientAppointmentResponseDto> getTodayAppointment() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Patient patient = patientDao.findByEmail(userDetails.getUsername());

        return appointmentDao.getTodayAppointmentsOfPatient(patient.getId()).stream().map(
                (each) -> PatientAppointmentResponseDto.builder()
                        .id(each.getId())
                        .availableFrom(each.getSlot().getStart().format(DateTimeFormatter.ofPattern("h:mm a")))
                        .availableTo(each.getSlot().getEnd().format(DateTimeFormatter.ofPattern("h:mm a")))
                        .doctorEmail(each.getSlot().getDoctor().getEmail())
                        .doctorName(each.getSlot().getDoctor().getName())
                        .status(each.getAppointmentStatus())
                        .reason(each.getReason())
                        .build()
        ).toList();
    }
}
