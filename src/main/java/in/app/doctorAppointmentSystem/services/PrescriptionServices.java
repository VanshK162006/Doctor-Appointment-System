package in.app.doctorAppointmentSystem.services;

import in.app.doctorAppointmentSystem.Entity.*;
import in.app.doctorAppointmentSystem.dto.MedicinesRequestDto;
import in.app.doctorAppointmentSystem.dto.PrescriptionDto;
import in.app.doctorAppointmentSystem.repository.AppointmentDao;
import in.app.doctorAppointmentSystem.repository.MedicinesDao;
import in.app.doctorAppointmentSystem.repository.PrescriptionDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrescriptionServices {
    private final PrescriptionDao prescriptionDao;
    private final AppointmentDao appointmentDao;
    private final MedicinesDao medicinesDao;

    public PrescriptionServices(PrescriptionDao prescriptionDao, AppointmentDao appointmentDao, MedicinesDao medicinesDao) {
        this.prescriptionDao = prescriptionDao;
        this.appointmentDao = appointmentDao;
        this.medicinesDao = medicinesDao;
    }

    public PrescriptionDto addPrescription(Long appointmentId, List<MedicinesRequestDto> medicinesRequestDtoList) {

        Appointment appointment = appointmentDao.findById(appointmentId);
        Doctor doctor = appointment.getSlot().getDoctor();
        Patient patient = appointment.getPatient();

        // Delete old prescription if exists
        Prescription oldPrescription = prescriptionDao.findPrescriptionByAppointmentId(appointmentId);
        if (oldPrescription != null) {
            prescriptionDao.delete(oldPrescription);
        }

        // Create Prescription first (without medicines)
        Prescription prescription = Prescription.builder()
                .appointment(appointment)
                .forWhatPurpose(appointment.getReason())
                .build();

        Prescription prescription1 = prescriptionDao.save(prescription);   // save to get the ID

        // Now create Medicines and set the prescription (owning side)
        List<Medicines> medicines = medicinesRequestDtoList.stream()
                .map(each -> Medicines.builder()
                        .medicines(each.getMedicineName())
                        .timePattern(each.getTimePattern())
                        .prescription(prescription1)          // ← CRITICAL LINE
                        .build())
                .map(medicinesDao::save)
                .toList();

        // Optional: set the list back (for in-memory consistency)
        prescription.setMedicines(medicines);

        appointmentDao.makeAppointmentComplete(appointmentId, patient.getEmail());

        return PrescriptionDto.builder()
                .patientName(patient.getName())
                .doctorName(doctor.getName())
                .medicines(medicinesRequestDtoList)
                .build();
    }

    public PrescriptionDto getPrescription(Long appointmentId){
        Prescription p1=prescriptionDao.findPrescriptionByAppointmentId(appointmentId);
        List<MedicinesRequestDto> dto=p1.getMedicines().stream().map(
                (a)->{
                    MedicinesRequestDto m1=new MedicinesRequestDto();
                    m1.setMedicineName(a.getMedicines());
                    m1.setTimePattern(a.getTimePattern());
                    return m1;
                }
        ).toList();
        return PrescriptionDto.builder()
                .doctorName(p1.getAppointment().getSlot().getDoctor().getName())
                .patientName(p1.getAppointment().getPatient().getName())
                .medicines(dto)
                .build();
    }
}
