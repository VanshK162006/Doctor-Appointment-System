    package in.app.doctorAppointmentSystem.controller;

    import in.app.doctorAppointmentSystem.dto.LoginDto;
    import in.app.doctorAppointmentSystem.services.LoginServices;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestBody;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;

    import javax.security.auth.login.CredentialNotFoundException;

    @RestController
    @RequestMapping("/api/doctor_appointment_system")
    public class LoginController {
        private final LoginServices loginServices;

        public LoginController(LoginServices loginServices) {
            this.loginServices = loginServices;
        }

        @PostMapping("/login")
        public ResponseEntity<String> login(@RequestBody LoginDto loginDto) throws CredentialNotFoundException {
            String token=loginServices.login(loginDto);
            return ResponseEntity.ok(token);
        }
    }
