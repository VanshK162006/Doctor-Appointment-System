package in.app.doctorAppointmentSystem.repository;

import in.app.doctorAppointmentSystem.Entity.Slot;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import javax.management.Query;
import java.util.List;

@Repository
public class SlotDao extends BaseRepository<Slot, Long> {
    private final SessionFactory sessionFactory;

    public SlotDao(SessionFactory sessionFactory) {
        super(sessionFactory, Slot.class);
        this.sessionFactory = sessionFactory;
    }

    public void makeSlotsNonBooked() {
        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            // HQL bulk update – one SQL statement, very fast
            int updatedCount = session.createQuery(
                            "UPDATE Slot s SET s.isBooked = false WHERE s.isBooked = true")
                    .executeUpdate();

            tx.commit();

            System.out.println(updatedCount + " slots were unbooked");

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public void makeSlotsNonBooked(Long slotId) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            // HQL bulk update – one SQL statement, very fast
            int updatedCount = session.createQuery(
                            "UPDATE Slot s SET s.isBooked = false WHERE s.id=?1")
                    .setParameter(1,slotId)
                    .executeUpdate();

            tx.commit();

            System.out.println(updatedCount + " slots were unbooked");

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public List<Slot> getAvailableSlots(Long doctorId) {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery(
                    "From Slot s where s.isBooked=?1 AND s.doctor.id=?2", Slot.class
            ).setParameter(1, false).setParameter(2, doctorId).getResultList();
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }

    public void makeSlotBooked(Long slotId) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            // HQL bulk update – one SQL statement, very fast
            int updatedCount = session.createQuery(
                            "UPDATE Slot s SET s.isBooked = true WHERE s.id = ?1").setParameter(1, slotId)
                    .executeUpdate();

            tx.commit();

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }

    }
}
