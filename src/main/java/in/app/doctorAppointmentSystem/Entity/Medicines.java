package in.app.doctorAppointmentSystem.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medicines {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String medicines;
    @Column(nullable = false)
    private String timePattern;
    @ManyToOne
    @JsonIgnore
    private Prescription prescription;
}
