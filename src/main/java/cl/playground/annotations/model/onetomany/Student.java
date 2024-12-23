package cl.playground.annotations.model.onetomany;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "students",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_student_enrollment",
            columnNames = {"enrollment_number"}
        ),
        @UniqueConstraint(
            name = "uk_student_email",
            columnNames = {"email"}
        )
    }
)
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "enrollment_number",
        length = 20,
        nullable = false
    )
    private String enrollmentNumber;

    @Column(
        name = "first_name",
        length = 50,
        nullable = false
    )
    private String firstName;

    @Column(
        name = "last_name",
        length = 50,
        nullable = false
    )
    private String lastName;

    @Column(
        name = "email",
        length = 100,
        nullable = false
    )
    private String email;

    @Column(
        name = "current_semester",
        columnDefinition = "INTEGER DEFAULT 1"
    )
    private Integer currentSemester;

    @Column(
        name = "gpa",
        precision = 3,
        scale = 2
    )
    private BigDecimal gpa;

    @Column(
        name = "active",
        columnDefinition = "BOOLEAN DEFAULT true"
    )
    private Boolean active;

    @Column(
        name = "admission_date",
        nullable = false
    )
    private LocalDate admissionDate;

    @Column(
        name = "created_at",
        columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    )
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "faculty_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_student_faculty")
    )
    private Faculty faculty;
}
