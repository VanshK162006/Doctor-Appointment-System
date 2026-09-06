package in.app.doctorAppointmentSystem.Mapper;

import in.app.doctorAppointmentSystem.Entity.Doctor;
import in.app.doctorAppointmentSystem.dto.DoctorRequestDto;
import in.app.doctorAppointmentSystem.dto.DoctorResponseDto;

public class DoctorMapper {
    public static Doctor toDoctorEntity(DoctorRequestDto dto) {
        return Doctor.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .Address(dto.getAddress())
                .phoneNo(dto.getPhone())
                .specialization(dto.getSpecialization())
                .yearsOfExperience(dto.getYearsOfExperience())
                .availableFrom(dto.getAvailableFrom())
                .availableTo(dto.getAvailableTo())
                .build();
    }

    public static DoctorResponseDto toDoctorResponseDto(Doctor d1){
        return DoctorResponseDto.builder()
                .name(d1.getName())
                .email(d1.getEmail())
                .specialization(d1.getSpecialization())
                .phone(d1.getPhoneNo())
                .address(d1.getAddress())
                .yearsOfExperience(d1.getYearsOfExperience())
                .id(d1.getId())
                .availableFrom(d1.getAvailableFrom())
                .availableTo(d1.getAvailableTo())
                .build();
    }
}
