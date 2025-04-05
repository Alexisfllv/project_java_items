package corporation.proyect.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "item_details")
public class ItemDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private Integer id;

    @Column(name = "detail_description", nullable = false, length = 255)
    private String description;  // Descripción del item

    @Column(name = "detail_quantity", nullable = false)
    private Integer quantity;  // Cantidad extra o modificada

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)  // FK hacia items
    private Item item;  // Relación con la tabla Items
}
