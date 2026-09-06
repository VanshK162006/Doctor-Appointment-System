package in.app.doctorAppointmentSystem.services;

import in.app.doctorAppointmentSystem.Entity.Doctor;
import in.app.doctorAppointmentSystem.Entity.Patient;
import in.app.doctorAppointmentSystem.repository.DoctorDao;
import in.app.doctorAppointmentSystem.repository.PatientDao;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.security.auth.login.CredentialNotFoundException;
import java.util.Collection;
import java.util.List;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    private final PatientDao patientDao;
    private final DoctorDao doctorDao;

    public UserDetailsServiceImpl(PatientDao patientDao,DoctorDao doctorDao) {
        this.patientDao = patientDao;
        this.doctorDao=doctorDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //For Patient
        Patient p1 = patientDao.findByEmail(username);
        if(p1!=null){
            return new UserDetailsImpl(p1);
        }

        //For Doctor;
        Doctor d1=doctorDao.findByEmail(username);
        if(d1!=null){
            return new UserDetailsImpl(d1);
        }

        throw new UsernameNotFoundException("Invalid Email or Password");
    }
}

class UserDetailsImpl implements org.springframework.security.core.userdetails.UserDetails {
    private final Patient patient;
    private final Doctor doctor;

    UserDetailsImpl(Patient patient) {
        this.patient = patient;
        this.doctor = null;
    }

    UserDetailsImpl(Doctor doctor) {
        this.doctor = doctor;
        this.patient = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

//      For Patient
        if (patient != null) {
            GrantedAuthority authority1 = new SimpleGrantedAuthority("CRUD_APPOINTMENT");
            GrantedAuthority authority2 = new SimpleGrantedAuthority("ROLE_PATIENT");
            return List.of(authority2, authority1);
        }
//      For Doctor
        else {
            GrantedAuthority authority1 = new SimpleGrantedAuthority("ROLE_DOCTOR");
            return List.of(authority1);
        }
    }

    @Override
    public @Nullable String getPassword() {
//      For Patient
        if (patient != null) return patient.getPassword();
//      For Doctor
        return doctor.getPassword();
    }

    @Override
    public String getUsername() {
//      For Patient
        if (patient != null) return patient.getEmail();
//      For Doctor
        return doctor.getEmail();
    }
}
