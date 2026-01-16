package com.ecommerce.naranjo.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

/**
 * Entidad que representa un producto disponible para la venta en el sistema de ecommerce.
 * Mapea la tabla {@code ARTICULO} de la base de datos Oracle.
 *
 * <p>
 * Incluye información básica como nombre, descripción, precio y stock disponible.
 * El precio se gestiona con {@link BigDecimal} para mantener precisión.
 * </p>
 *
 * <p>
 * Esta entidad se puede asociar a otras entidades como {@link com.ecommerce.naranjo.model.Compra}
 * si se implementa un histórico de compras o pedidos.
 * </p>
 * 
 * <p>
 * Se recomienda usar esta entidad en capas de servicio y repositorio para operaciones
 * CRUD y en controladores mediante DTOs para exponer los datos de forma segura.
 * </p>
 * 
 * @author Álvaro Naranjo
 * @version 1.0
 * @since 2026-01-04
 */
@Entity
@Table(name = "ARTICULO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Articulo {

    /**
     * Identificador único del artículo.
     * Generado automáticamente mediante secuencia global.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    /**
     * Nombre del artículo.
     */
    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;

    /**
     * Descripción detallada del artículo.
     * Campo de tipo LOB para permitir descripciones extensas.
     */
    @Lob
    @Column(name = "DESCRIPCION", nullable = false)
    private String descripcion;

    /**
     * Precio de venta al público actual del artículo.
     * Se usa {@link BigDecimal} para garantizar precisión 
     * (corresponde a NUMBER(10,2) en Oracle).
     */
    @Column(name = "PVP_ACTUAL", nullable = false, precision = 10, scale = 2)
    private BigDecimal pvpActual;

    /**
     * Cantidad de unidades físicas disponibles en almacén.
     * No puede ser nulo.
     */
    @Builder.Default
    @Column(name = "STOCK", nullable = false)
    private Integer stock = 0; // Por defecto stock = 0

}
