package in.app.doctorAppointmentSystem.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class BaseRepository<ENTITY, ID> {
    private final SessionFactory sessionFactory;
    private final Class<ENTITY> entity;

    public BaseRepository(SessionFactory sessionFactory, Class<ENTITY> entity) {
        this.sessionFactory = sessionFactory;
        this.entity = entity;
    }


    public ENTITY save(ENTITY object) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.persist(object);
            transaction.commit();
            return object; // ✅ return the saved entity
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public void delete(ENTITY object) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.remove(object);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public ENTITY update(ENTITY newObject) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.merge(newObject);
            transaction.commit();
            return newObject; // ✅ return the saved entity
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public ENTITY findById(ID id) {
        Session session = sessionFactory.openSession();

        try {
            return session.find(entity,id);
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }
}
