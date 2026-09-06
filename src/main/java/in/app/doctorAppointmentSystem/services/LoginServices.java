package in.app.doctorAppointmentSystem.services;

import in.app.doctorAppointmentSystem.dto.LoginDto;
import in.app.doctorAppointmentSystem.utils.JwtServices;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialNotFoundException;

@Service
public class LoginServices {
    private final AuthenticationProvider authenticationProvider;
    private final JwtServices jwtServices;

    public LoginServices(AuthenticationProvider authenticationProvider, JwtServices jwtServices) {
        this.authenticationProvider = authenticationProvider;
        this.jwtServices = jwtServices;
    }

    public String login(LoginDto loginDto) throws CredentialNotFoundException {
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();

        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authResult = authenticationProvider.authenticate(authentication);

            // If we reach here, authentication succeeded
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authResult);
            SecurityContextHolder.setContext(context);

            return jwtServices.generateToken((UserDetails) authResult.getPrincipal());

        } catch (BadCredentialsException | UsernameNotFoundException ex) {
            // This is the real failure path
            System.out.println("Login failed: " + ex.getMessage());   // or use a logger
            throw new CredentialNotFoundException("Invalid Email or Password");  // or a custom exception
        }
    }
}
