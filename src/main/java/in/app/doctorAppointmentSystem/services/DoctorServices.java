package in.app.doctorAppointmentSystem.services;

import in.app.doctorAppointmentSystem.Entity.Doctor;
import in.app.doctorAppointmentSystem.Mapper.DoctorMapper;
import in.app.doctorAppointmentSystem.dto.DoctorRequestDto;
import in.app.doctorAppointmentSystem.dto.DoctorResponseDto;
import in.app.doctorAppointmentSystem.dto.LoginDto;
import in.app.doctorAppointmentSystem.repository.DoctorDao;
import in.app.doctorAppointmentSystem.utils.JwtServices;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialNotFoundException;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.concurrent.TimeoutException;

@Service
public class DoctorServices {
    private final PasswordEncoder passwordEncoder;
    private final DoctorDao doctorDao;
    private final AuthenticationProvider authenticationProvider;
    private final JwtServices jwtServices;
    private final SlotServices slotServices;

    public DoctorServices(PasswordEncoder passwordEncoder, DoctorDao doctorDao, AuthenticationProvider authenticationProvider, JwtServices jwtServices, SlotServices slotServices) {
        this.passwordEncoder = passwordEncoder;
        this.doctorDao = doctorDao;
        this.authenticationProvider = authenticationProvider;
        this.jwtServices = jwtServices;
        this.slotServices = slotServices;
    }

    public DoctorResponseDto registerDoctor(DoctorRequestDto dto) {
        Doctor d1 = DoctorMapper.toDoctorEntity(dto);
        String password = encodePassword(d1.getPassword());
        d1.setPassword(password);

        Doctor doctor = doctorDao.findByEmail(d1.getEmail());
        if (doctor == null) {
            LocalTime from=dto.getAvailableFrom();
            LocalTime to=dto.getAvailableTo();

            if(from.isAfter(to) && (!from.plusMinutes(30).isAfter(to))){
                throw new DateTimeException("availableFrom must be 30 minutes earlier then availableTo.");
            }
            Doctor doctor1 = doctorDao.save(d1);
            slotServices.generateSlots(doctor1.getAvailableFrom(),doctor1.getAvailableTo(),doctor1.getId());
            return DoctorMapper.toDoctorResponseDto(doctor1);
        }
        throw new RuntimeException("You already have an Account!");
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
