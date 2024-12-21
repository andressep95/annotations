package cl.playground.annotations.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_user_name",
            columnNames = {"name"}
        ),
        @UniqueConstraint(
            name = "uk_user_email",
            columnNames = {"email"}
        )
    }
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "name",
        length = 100,
        nullable = false
    )
    private String name;

    @Column(
        name = "email",
        length = 150,
        nullable = false
    )
    private String email;

    @Column(
        name = "created_at",
        columnDefinition = "DATE DEFAULT CURRENT_DATE"
    )
    private LocalDate createdAt;

    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Set<UserProduct> userProducts;
}