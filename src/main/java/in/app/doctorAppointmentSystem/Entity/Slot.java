package in.app.doctorAppointmentSystem.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"id","start","end"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,name = "start_time")
    private LocalTime start;

    @Column(nullable = false,name = "end_time")
    private LocalTime end;

    @Column(nullable = false)
    private boolean isBooked;

    @ManyToOne(fetch = FetchType.EAGER)
    private Doctor doctor;

    @Version
    private Long version;
}
