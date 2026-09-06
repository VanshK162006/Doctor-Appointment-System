package in.app.doctorAppointmentSystem.config;

import in.app.doctorAppointmentSystem.Entity.*;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class HibernateConfigurtion {

    @Value("${application.db.url}")
    private String dbUrl;

    @Value("${application.db.username}")
    private String dbUsername;

    @Value("${application.db.password}")
    private String dbPassword;

    @Bean
    public SessionFactory sessionFactory() {
        Properties props = new Properties();
        props.put("hibernate.connection.driver_class", "org.postgresql.Driver");
        props.put("hibernate.connection.url", dbUrl);
        props.put("hibernate.connection.username", dbUsername);
        props.put("hibernate.connection.password", dbPassword);
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.put("hibernate.hbm2ddl.auto", "update");
        props.put("hibernate.show_sql", "true");

        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        configuration.setProperties(props);
        configuration.addAnnotatedClass(Patient.class);
        configuration.addAnnotatedClass(Doctor.class);
        configuration.addAnnotatedClass(Slot.class);
        configuration.addAnnotatedClass(Appointment.class);
        configuration.addAnnotatedClass(Prescription.class);
        configuration.addAnnotatedClass(Medicines.class);

        return configuration.buildSessionFactory();
    }

}