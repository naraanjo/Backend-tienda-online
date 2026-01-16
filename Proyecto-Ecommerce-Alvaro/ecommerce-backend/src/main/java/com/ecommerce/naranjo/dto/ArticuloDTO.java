package com.ecommerce.naranjo.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) para la entidad {@link com.ecommerce.naranjo.model.Articulo}.
 * Se utiliza para transferir información de un artículo entre capas de la aplicación
 * (controlador, servicio, presentación) sin exponer directamente la entidad.
 * 
 * <p>
 * Incluye información básica del artículo: nombre, descripción, precio y stock disponible.
 * Permite la serialización/deserialización segura y evita modificaciones directas
 * sobre la entidad original.
 * </p>
 * 
 * <p>
 * El campo {@code descripcion} puede contener grandes cantidades de texto (CLOB en base de datos),
 * mientras que {@code pvpActual} utiliza {@link BigDecimal} para mantener precisión monetaria exacta.
 * </p>
 * 
 * <p>
 * Se recomienda utilizar este DTO en controladores y servicios para enviar y recibir información
 * de artículos sin exponer la entidad completa.
 * </p>
 * 
 * @author Álvaro Naranjo
 * @version 1.0
 * @since 2026-01-04
 */
@Data
public class ArticuloDTO {

    /**
     * Identificador único del artículo.
     * Puede ser nulo si el artículo aún no ha sido persistido.
     */
    private Long id;

    /**
     * Nombre del artículo.
     */
    private String nombre;

    /**
     * Descripción del artículo.
     * 
     * <p>
     * En base de datos se puede mapear como {@code CLOB} para soportar textos largos.
     * </p>
     */
    private String descripcion;

    /**
     * Precio de venta al público del artículo.
     * Se usa {@link BigDecimal} para garantizar precisión exacta.
     */
    private BigDecimal pvpActual;

    /**
     * Cantidad de unidades disponibles en stock.
     */
    private Integer stock;
}
