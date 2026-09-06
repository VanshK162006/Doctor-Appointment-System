package in.app.doctorAppointmentSystem.services;

import in.app.doctorAppointmentSystem.Entity.Patient;
import in.app.doctorAppointmentSystem.Mapper.PatientMapper;
import in.app.doctorAppointmentSystem.dto.LoginDto;
import in.app.doctorAppointmentSystem.dto.PatientRequestDto;
import in.app.doctorAppointmentSystem.dto.PatientResponseDto;
import in.app.doctorAppointmentSystem.repository.PatientDao;
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

@Service
public class PatientService {
    private final PatientDao patientDao;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationProvider authenticationProvider;
    private final JwtServices jwtServices;

    public PatientService(PatientDao patientDao, PasswordEncoder passwordEncoder, AuthenticationProvider authenticationProvider, JwtServices jwtServices) {
        this.patientDao = patientDao;
        this.passwordEncoder = passwordEncoder;
        this.authenticationProvider = authenticationProvider;
        this.jwtServices = jwtServices;
    }

    public PatientResponseDto register(PatientRequestDto patientRequestDto) {
        Patient p1 = PatientMapper.toPatientEntity(patientRequestDto);
        p1.setPassword(encodePassword(patientRequestDto.getPassword()));

        if(p1.getPhone().toString().length()!=10) throw new RuntimeException("Mobile Number length must be 10.");
        Patient p2 = patientDao.findByEmail(p1.getEmail());
        if(p2==null){
            return PatientMapper.toPatientResponseDto(
                    patientDao.save(p1)
            );
        } else {
            throw new RuntimeException("You already have an Account!");
        }
    }

    private String encodePassword(String password){
        return passwordEncoder.encode(password);
    }

}
