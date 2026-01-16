package com.ecommerce.naranjo.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ecommerce.naranjo.dto.CompraDTO;
import com.ecommerce.naranjo.dto.LineaCompraDTO;
import com.ecommerce.naranjo.model.Compra;
import com.ecommerce.naranjo.model.LineaCompra;

import java.math.BigDecimal;

/**
 * Mapper encargado de la conversión entre las entidades de dominio relacionadas con compras
 * y sus correspondientes objetos de transferencia de datos.
 *
 * <p>
 * Esta interfaz utiliza MapStruct para generar automáticamente la implementación en tiempo
 * de compilación, permitiendo transformar de forma segura y eficiente las entidades
 * {@link Compra} y {@link LineaCompra} en {@link CompraDTO} y {@link LineaCompraDTO}.
 * </p>
 *
 * <p>
 * El mapeo incluye cálculos derivados como el total de la compra y el subtotal de cada línea,
 * evitando el almacenamiento de valores redundantes en la base de datos y garantizando
 * la coherencia de la información mostrada al usuario.
 * </p>
 *
 * <p>
 * El mapper se registra como componente Spring mediante {@code componentModel = "spring"},
 * permitiendo su inyección directa en la capa de servicios.
 * </p>
 *
 * @author Álvaro Naranjo
 * @version 1.0
 * @since 2026-01-04
 */
@Mapper(componentModel = "spring")
public interface CompraMapper {

    /**
     * Convierte una entidad {@link Compra} en su correspondiente {@link CompraDTO}.
     *
     * <p>
     * Durante el proceso se extrae la información básica del cliente, se calcula el total
     * completo de la compra de forma dinámica y se trasladan los datos de cabecera
     * al DTO sin exponer directamente la entidad persistente.
     * </p>
     *
     * @param entity Entidad de compra a convertir.
     * @return DTO de compra con los datos consolidados.
     * @author Álvaro Naranjo
     */
	// De la persona de la compra coge el id, y lo pone en personaId del DTO de compra
    @Mapping(source = "persona.id", target = "personaId")
    @Mapping(source = "persona.nombreCompleto", target = "personaNombre")
    @Mapping(target = "total", expression = "java(calcularTotal(entity))")
    CompraDTO toDTO(Compra entity);

    /**
     * Convierte una entidad {@link LineaCompra} en su correspondiente {@link LineaCompraDTO}.
     *
     * <p>
     * Se transfieren los datos del artículo asociado, el precio unitario registrado en la compra
     * y se calcula el subtotal de la línea en tiempo de ejecución.
     * </p>
     *
     * @param linea Entidad de línea de compra a convertir.
     * @return DTO de línea de compra con subtotal calculado.
     * @author Álvaro Naranjo
     */
    @Mapping(source = "articulo.id", target = "articuloId")
    @Mapping(source = "articulo.nombre", target = "articuloNombre")
    @Mapping(source = "precioSnapshot", target = "precioUnitario")
    @Mapping(target = "subtotal", expression = "java(calcularSubtotal(linea))")
    LineaCompraDTO toDTO(LineaCompra linea);

    /**
     * Convierte un {@link CompraDTO} en su correspondiente entidad {@link Compra}.
     *
     * <p>
     * Las relaciones complejas como la persona asociada, las líneas de compra y la fecha de la
     * operación se gestionan manualmente en la capa de servicio para mantener el control
     * de la lógica de negocio y la integridad de los datos.
     * </p>
     *
     * @param dto DTO de compra a convertir.
     * @return Entidad de compra parcialmente construida.
     */
    @Mapping(target = "persona", ignore = true)
    @Mapping(target = "lineas", ignore = true)
    @Mapping(target = "fechaCompra", ignore = true)
    Compra toEntity(CompraDTO dto);

    /**
     * Calcula el importe total de una compra a partir de sus líneas asociadas.
     *
     * @param entity Entidad de compra.
     * @return Importe total calculado dinámicamente.
     */
    default BigDecimal calcularTotal(Compra entity) {
        return entity.getTotalCalculado();
    }

    /**
     * Calcula el subtotal de una línea de compra.
     *
     * @param linea Entidad de línea de compra.
     * @return Subtotal de la línea, o cero si los datos son incompletos.
     */
    default BigDecimal calcularSubtotal(LineaCompra linea) {
        if (linea.getPrecioSnapshot() == null || linea.getCantidad() == null) {
            return BigDecimal.ZERO;
        }
        return linea.getPrecioSnapshot().multiply(new BigDecimal(linea.getCantidad()));
    }
}
