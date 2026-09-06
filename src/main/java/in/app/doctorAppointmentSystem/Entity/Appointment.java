package in.app.doctorAppointmentSystem.Entity;

import in.app.doctorAppointmentSystem.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Patient patient;

    @OneToOne
    private Slot slot;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime bookedAt;

    @Enumerated(value = EnumType.STRING)
    private AppointmentStatus appointmentStatus;

    @Column(nullable = false)
    private String reason;
}
