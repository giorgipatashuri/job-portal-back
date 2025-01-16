package by.giorgi.jobportalback.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "experiences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean currentlyWorking;

    @Column(columnDefinition = "TEXT")
    private String workSummary;

    @ManyToOne
    @JoinColumn(name = "cv_id")
    private Cv cv;
}