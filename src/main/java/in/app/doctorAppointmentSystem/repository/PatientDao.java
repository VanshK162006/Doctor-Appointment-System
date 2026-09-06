package in.app.doctorAppointmentSystem.repository;

import in.app.doctorAppointmentSystem.Entity.Patient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import javax.management.Query;
import java.util.List;

@Repository
public class PatientDao extends BaseRepository<Patient, Long> {
    private final SessionFactory sessionFactory;

    public PatientDao(SessionFactory sessionFactory) {
        super(sessionFactory, Patient.class);
        this.sessionFactory = sessionFactory;
    }

    public Patient findByEmail(String email) {
        Session session = sessionFactory.openSession();

        try {
            List<Patient> list=session.createQuery(
                    "FROM Patient p where p.email=?1",Patient.class
            ).setParameter(1,email).getResultList();

            return list.isEmpty()? null:list.getFirst();
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }
}
