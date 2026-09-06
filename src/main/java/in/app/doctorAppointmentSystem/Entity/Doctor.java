package in.app.doctorAppointmentSystem.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(unique = true,nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String specialization;
    @Column(nullable = false)
    private Long yearsOfExperience;
    @Column(nullable = false,unique = true)
    private Long phoneNo;
    @Column(nullable = false)
    private String Address;
    @Column(nullable = false)
    private LocalTime availableFrom;
    @Column(nullable = false)
    private LocalTime availableTo;


    @OneToMany(mappedBy = "doctor",cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Slot> slot;
}
