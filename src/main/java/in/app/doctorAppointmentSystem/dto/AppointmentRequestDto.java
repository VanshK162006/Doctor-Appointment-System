package in.app.doctorAppointmentSystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AppointmentRequestDto {
    private String reason;
    private Long slotId;
}
