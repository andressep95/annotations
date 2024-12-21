package cl.playground.annotations.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_products")
public class UserProduct {
    @EmbeddedId
    private UserProductId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(
        name = "user_id",
        foreignKey = @ForeignKey(name = "fk_userproduct_user")
    )
    private User user;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(
        name = "product_id",
        foreignKey = @ForeignKey(name = "fk_userproduct_product")
    )
    private Product product;

    @Column(
        name = "quantity",
        nullable = false
    )
    private int quantity;

    @Column(
        name = "order_date",
        columnDefinition = "DATE DEFAULT CURRENT_DATE"
    )
    private LocalDate orderDate;
}

@Embeddable
class UserProductId implements java.io.Serializable {
    private Long userId;
    private Long productId;

}