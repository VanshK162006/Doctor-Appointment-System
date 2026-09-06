package in.app.doctorAppointmentSystem.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorRequestDto {
    @NotBlank
    private String name;
    @Email
    private String email;
    @NotBlank
    @Size(min=8)
    private String password;
    @Min(1000000000L)          // example lower bound
    @Max(9999999999L)
    private Long phone;
    @NotBlank
    private String specialization;
    @Min(1L)
    private Long yearsOfExperience;
    @NotBlank
    private String address;
    private LocalTime availableFrom;
    private LocalTime availableTo;
}
