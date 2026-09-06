package in.app.doctorAppointmentSystem.Mapper;

import in.app.doctorAppointmentSystem.Entity.Patient;
import in.app.doctorAppointmentSystem.dto.PatientRequestDto;
import in.app.doctorAppointmentSystem.dto.PatientResponseDto;

public class PatientMapper {
    public static Patient toPatientEntity(PatientRequestDto patientRequestDto){
        Patient patient=new Patient();
        patient.setName(patientRequestDto.getName());
        patient.setEmail(patientRequestDto.getEmail());
        patient.setPhone(patientRequestDto.getPhone());
        patient.setPassword(patientRequestDto.getPassword());
        return patient;
    }

    public static PatientResponseDto toPatientResponseDto(Patient patient){
        return PatientResponseDto.builder()
                .id(patient.getId())
                .email(patient.getEmail())
                .phone(patient.getPhone())
                .name(patient.getName())
                .build();
    }
}
