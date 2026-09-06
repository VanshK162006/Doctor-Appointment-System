package in.app.doctorAppointmentSystem.dto;

import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlotResponseDto {
    private Long id;
    private String startTime;
    private String endTime;
    private Long doctorId;
    private String doctorEmail;
}
