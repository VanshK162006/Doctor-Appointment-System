package in.app.doctorAppointmentSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootApplication
@EnableScheduling
public class DoctorAppointmentSystemApplication {

	public static void main(String[] args) {

		SpringApplication.run(DoctorAppointmentSystemApplication.class, args);

	}

}
