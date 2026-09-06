package in.app.doctorAppointmentSystem.repository;

import in.app.doctorAppointmentSystem.Entity.Medicines;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class MedicinesDao extends BaseRepository<Medicines,Long>{
    private final SessionFactory sessionFactory;
    public MedicinesDao(SessionFactory sessionFactory) {
        super(sessionFactory, Medicines.class);
        this.sessionFactory=sessionFactory;
    }
}
