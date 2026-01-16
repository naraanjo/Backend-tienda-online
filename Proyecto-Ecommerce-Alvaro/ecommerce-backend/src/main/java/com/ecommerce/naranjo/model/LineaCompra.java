package com.ecommerce.naranjo.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "LINEA_COMPRA") 
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class LineaCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CANTIDAD", nullable = false)
    private Integer cantidad;

    /**
     * Precio congelado en el momento de la compra, DE UN ARTICULO no de la compra, precio unitario.
     * Mapea la columna PRECIO_SNAPSHOT de tu script.
     */
    @Column(name = "PRECIO_SNAPSHOT", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioSnapshot;

    // --- RELACIONES ---

    /**
     * Referencia a la cabecera de la compra.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPRA_ID", nullable = false)
    @ToString.Exclude
    private Compra compra;

    /**
     * Producto comprado.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ARTICULO_ID", nullable = false)
    @ToString.Exclude
    private Articulo articulo;
}