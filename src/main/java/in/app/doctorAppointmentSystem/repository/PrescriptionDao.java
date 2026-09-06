package in.app.doctorAppointmentSystem.repository;

import in.app.doctorAppointmentSystem.Entity.Prescription;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PrescriptionDao extends BaseRepository<Prescription, Long> {
    private final SessionFactory sessionFactory;

    public PrescriptionDao(SessionFactory sessionFactory) {
        super(sessionFactory, Prescription.class);
        this.sessionFactory=sessionFactory;
    }

    public Prescription findPrescriptionByAppointmentId(Long appointmentId){
        Session session=sessionFactory.openSession();
        try {
            List<Prescription> prescriptionList= session.createQuery(
                    "From Prescription p where p.appointment.id=?1", Prescription.class
            ).setParameter(1,appointmentId).getResultList();

            return prescriptionList.isEmpty()? null:prescriptionList.getFirst();
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }

}
