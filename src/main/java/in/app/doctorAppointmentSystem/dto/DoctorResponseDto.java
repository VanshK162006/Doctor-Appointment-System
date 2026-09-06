package in.app.doctorAppointmentSystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorResponseDto {
    private Long id;
    private String name;
    private String email;
    private Long phone;
    private String specialization;
    private Long yearsOfExperience;
    private String address;
    private LocalTime availableFrom;
    private LocalTime availableTo;
}
