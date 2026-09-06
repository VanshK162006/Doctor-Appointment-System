package in.app.doctorAppointmentSystem.repository;

import in.app.doctorAppointmentSystem.Entity.Doctor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DoctorDao extends BaseRepository<Doctor, Long> {
    private final SessionFactory sessionFactory;

    public DoctorDao(SessionFactory sessionFactory) {
        super(sessionFactory, Doctor.class);
        this.sessionFactory = sessionFactory;
    }

    public List<Doctor> findAll() {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery(
                    "From Doctor d", Doctor.class
            ).getResultList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            session.close();
        }
    }

    public Doctor findByEmail(String email) {
        Session session = sessionFactory.openSession();
        try {
            List<Doctor> doctors = session.createQuery(
                    "From Doctor d where d.email=?1", Doctor.class
            ).setParameter(1, email).getResultList();

            return doctors.isEmpty() ? null : doctors.getFirst();
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }
}
