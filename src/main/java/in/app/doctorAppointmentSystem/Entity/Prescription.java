package in.app.doctorAppointmentSystem.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"doctor", "patient", "appointment_date", "for_what_purpose"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prescription {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Appointment appointment;

    @OneToMany(mappedBy = "prescription",cascade = CascadeType.REMOVE,fetch = FetchType.EAGER)
    List<Medicines> medicines;

    @CreationTimestamp
    @Column(name = "appointment_date")
    private LocalDate appointmentDate;

    @Column(nullable = false,name = "for_what_purpose")
    private String forWhatPurpose;
}
