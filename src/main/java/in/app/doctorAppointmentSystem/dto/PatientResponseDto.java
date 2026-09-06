package in.app.doctorAppointmentSystem.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientResponseDto {
    private Long id;
    private String name;
    private String email;
    private Long phone;
}
