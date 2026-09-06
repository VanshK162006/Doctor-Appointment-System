package in.app.doctorAppointmentSystem.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientRequestDto {
    @NotBlank
    private String name;
    @Email
    private String email;
    @Min(1000000000L)          // example lower bound
    @Max(9999999999L)
    private Long phone;
    @NotBlank
    @Size(min = 8,max = 14)
    private String password;
}
