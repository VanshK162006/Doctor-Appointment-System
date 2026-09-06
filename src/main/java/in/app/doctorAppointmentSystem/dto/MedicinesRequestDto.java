package in.app.doctorAppointmentSystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MedicinesRequestDto {
    private String medicineName;
    private String timePattern;
}
