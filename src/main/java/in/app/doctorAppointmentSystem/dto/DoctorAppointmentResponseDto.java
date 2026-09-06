package in.app.doctorAppointmentSystem.dto;

import jakarta.persistence.NamedStoredProcedureQueries;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class DoctorAppointmentResponseDto {
    private Long id;
    private String availableFrom;
    private String availableTo;
    private PatientResponseDto patientResponseDto;
    private String reason;

}
