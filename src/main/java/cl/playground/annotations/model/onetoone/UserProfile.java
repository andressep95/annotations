package cl.playground.annotations.model.onetoone;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_profiles")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        unique = true,
        foreignKey = @ForeignKey(name = "fk_profile_user")
    )
    private User user;

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
        name = "date_of_birth"
    )
    private LocalDate dateOfBirth;

    @Column(
        name = "phone_number",
        length = 20
    )
    private String phoneNumber;

    @Column(
        name = "address",
        length = 200
    )
    private String address;

    @Column(
        name = "bio",
        columnDefinition = "TEXT"
    )
    private String bio;

    @Column(
        name = "profile_picture_url",
        length = 255
    )
    private String profilePictureUrl;

    @Column(
        name = "preferences",
        columnDefinition = "jsonb"
    )
    private String preferences;

    @Column(
        name = "created_at",
        columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    )
    private LocalDateTime createdAt;

    @Column(
        name = "updated_at",
        columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    )
    private LocalDateTime updatedAt;
}