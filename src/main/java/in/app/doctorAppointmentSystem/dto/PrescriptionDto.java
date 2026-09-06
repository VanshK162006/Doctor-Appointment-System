package in.app.doctorAppointmentSystem.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionDto {
    private String doctorName;
    private String patientName;
    private List<MedicinesRequestDto> medicines;
}
