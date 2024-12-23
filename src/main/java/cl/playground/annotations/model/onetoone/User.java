package cl.playground.annotations.model.onetoone;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_user_username",
            columnNames = {"username"}
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
        name = "username",
        length = 50,
        nullable = false
    )
    private String username;

    @Column(
        name = "email",
        length = 100,
        nullable = false
    )
    private String email;

    @Column(
        name = "password_hash",
        length = 60,
        nullable = false
    )
    private String passwordHash;

    @Column(
        name = "account_locked",
        columnDefinition = "BOOLEAN DEFAULT false"
    )
    private Boolean accountLocked;

    @Column(
        name = "last_login",
        columnDefinition = "TIMESTAMP"
    )
    private LocalDateTime lastLogin;

    @Column(
        name = "created_at",
        columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    )
    private LocalDateTime createdAt;

    @OneToOne(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY,
        optional = false
    )
    private UserProfile profile;
}