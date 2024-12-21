package cl.playground.annotations.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint (
            name = "uk_user_name",
            columnNames = {"name"}
        ),
        @UniqueConstraint (
            name = "uk_user_email",
            columnNames = {"email"}
        )
    }
)
public class User {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (
        name = "name",
        length = 100,
        nullable = false
    )
    private String name;

    @Column (
        name = "email",
        length = 150,
        nullable = false
    )
    private String email;

    @Column (
        name = "created_at",
        columnDefinition = "DATE DEFAULT CURRENT_DATE"
    )
    private LocalDate createdAt;

    @ManyToMany
    @JoinTable (
        name = "user_products",
        joinColumns = @JoinColumn (
            name = "user_id",
            nullable = false
        ),
        inverseJoinColumns =
        @JoinColumn (
            name = "product_id",
            nullable = false,
            foreignKey = @ForeignKey (
                name = "fk_userproduct_product",
                foreignKeyDefinition = "FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT"
            )
        ),
        foreignKey = @ForeignKey (
            name = "fk_userproduct_user",
            foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"
        ),
        indexes = {
            @Index (
                name = "idx_userproduct_user",
                columnList = "user_id"
            ),
            @Index (
                name = "idx_userproduct_product",
                columnList = "product_id"
            )
        }
    )
    private Set<Product> products;
}