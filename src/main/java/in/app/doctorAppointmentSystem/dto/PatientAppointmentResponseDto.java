package in.app.doctorAppointmentSystem.dto;

import in.app.doctorAppointmentSystem.AppointmentStatus;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientAppointmentResponseDto {
    private Long id;
    private String doctorName;
    private String doctorEmail;
    private String availableFrom;
    private String availableTo;
    private String reason;
    private AppointmentStatus status;
}
