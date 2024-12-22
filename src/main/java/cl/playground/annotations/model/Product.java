package cl.playground.annotations.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "products",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_product_name",
        columnNames = {"name"}
    )
)
public class Product {
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
        name = "price",
        precision = 10,
        scale = 2
    )
    private BigDecimal price;

    @Column(
        name = "stock",
        columnDefinition = "INTEGER DEFAULT 0"
    )
    private Integer stock;

    @Column(
        name = "created_at",
        columnDefinition = "DATE DEFAULT CURRENT_DATE"
    )
    private LocalDate createdAt;

    @OneToMany(
        mappedBy = "product",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Set<UserProduct> userProducts;
}