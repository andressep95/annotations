package cl.playground.annotations.model.onetomany;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "faculties",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_faculty_code",
            columnNames = {"faculty_code"}
        ),
        @UniqueConstraint(
            name = "uk_faculty_name",
            columnNames = {"name"}
        )
    }
)
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "faculty_code",
        length = 10,
        nullable = false
    )
    private String facultyCode;

    @Column(
        name = "name",
        length = 100,
        nullable = false
    )
    private String name;

    @Column(
        name = "max_capacity",
        columnDefinition = "INTEGER DEFAULT 500"
    )
    private Integer maxCapacity;

    @Column(
        name = "foundation_year",
        nullable = false
    )
    private Integer foundationYear;

    @Column(
        name = "annual_budget",
        precision = 12,
        scale = 2
    )
    private BigDecimal annualBudget;

    @Column(
        name = "created_at",
        columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    )
    private LocalDateTime createdAt;

    @OneToMany(
        mappedBy = "faculty",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY
    )
    private List<Student> students;
}