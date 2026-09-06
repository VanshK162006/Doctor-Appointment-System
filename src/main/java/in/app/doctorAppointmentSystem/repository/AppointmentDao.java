package in.app.doctorAppointmentSystem.repository;

import in.app.doctorAppointmentSystem.AppointmentStatus;
import in.app.doctorAppointmentSystem.Entity.Appointment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AppointmentDao extends BaseRepository<Appointment, Long> {
    private final SessionFactory sessionFactory;

    public AppointmentDao(SessionFactory sessionFactory) {
        super(sessionFactory, Appointment.class);
        this.sessionFactory = sessionFactory;
    }

    public Appointment cancelAppointment(Long appointmentId, String patientEmail) {
        Session session = sessionFactory.openSession();
        Transaction t1 = null;
        try {
            t1 = session.beginTransaction();
            session.createQuery(
                            "Update Appointment a Set a.appointmentStatus=?1 where a.id=?2 AND a.patient.email=?3"
                    ).setParameter(1, AppointmentStatus.CANCELLED)
                    .setParameter(2, appointmentId)
                    .setParameter(3, patientEmail)
                    .executeUpdate();
            t1.commit();
            return findById(appointmentId);
        } catch (Exception e) {
            if (t1 != null) {
                t1.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    public List<Appointment> getTodayAppointmentsOfPatient(Long patientId) {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery(
                    "From Appointment a where a.patient.id=?1", Appointment.class
            ).setParameter(1, patientId).getResultList();
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }

    public void cancelAllScheduleAppointments() {
        Session session = sessionFactory.openSession();
        Transaction t1 = null;
        try {
            t1 = session.beginTransaction();
            session.createQuery(
                            "Update Appointment a Set a.appointmentStatus=?1 where a.appointmentStatus=?2"
                    ).setParameter(1, AppointmentStatus.CANCELLED)
                    .setParameter(2,AppointmentStatus.SCHEDULED)
                    .executeUpdate();
            t1.commit();
        } catch (Exception e) {
            if (t1 != null) {
                t1.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }


    public List<Appointment> getTodayAppointmentsOfDoctor(Long doctorId) {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery(
                    "From Appointment a where a.slot.doctor.id=?1 AND a.appointmentStatus=?2", Appointment.class
            ).setParameter(1, doctorId).setParameter(2, AppointmentStatus.SCHEDULED).getResultList();
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }

    public void makeAppointmentComplete(Long appointmentId, String patientEmail) {
        Session session = sessionFactory.openSession();
        Transaction t1 = null;
        try {
            t1 = session.beginTransaction();
            session.createQuery(
                            "Update Appointment a Set a.appointmentStatus=?1 where a.id=?2 AND a.patient.email=?3"
                    ).setParameter(1, AppointmentStatus.COMPLETED)
                    .setParameter(2, appointmentId)
                    .setParameter(3, patientEmail)
                    .executeUpdate();
            t1.commit();
        } catch (Exception e) {
            if (t1 != null) {
                t1.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }
}
