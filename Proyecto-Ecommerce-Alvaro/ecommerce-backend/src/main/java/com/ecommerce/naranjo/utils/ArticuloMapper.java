package com.ecommerce.naranjo.utils;

import org.mapstruct.Mapper;
import com.ecommerce.naranjo.dto.ArticuloDTO;
import com.ecommerce.naranjo.model.Articulo;

/**
 * Mapper de la entidad {@link Articulo} a su DTO {@link ArticuloDTO} y viceversa.
 * Utiliza MapStruct para generar automáticamente la implementación en tiempo de compilación,
 * facilitando la conversión entre entidad y DTO sin escribir código manual.
 * 
 * <p>
 * La conversión hacia DTO copia todos los campos de {@code Articulo} incluyendo
 * {@code id, nombre, descripcion, pvpActual y stock}.
 * La conversión hacia entidad realiza el mapeo inverso.
 * </p>
 * 
 * <p>
 * Este mapper se registra como componente Spring mediante {@code componentModel = "spring"},
 * por lo que puede inyectarse en servicios con {@code @Autowired}.
 * </p>
 * 
 * <p>
 * Dado que {@link Articulo} no tiene relaciones bidireccionales complejas,
 * no es necesario ignorar ningún campo ni gestionar manualmente vínculos.
 * </p>
 * 
 * @author Álvaro Naranjo
 * @version 1.0
 * @since 2026-01-04
 */
@Mapper(componentModel = "spring")
public interface ArticuloMapper {

    /**
     * Convierte una entidad {@link Articulo} a su DTO {@link ArticuloDTO}.
     * 
     * @param articulo Entidad a convertir.
     * @return DTO correspondiente con todos los campos de la entidad.
     * @author Álvaro Naranjo
     */
    ArticuloDTO toDTO(Articulo articulo);

    /**
     * Convierte un DTO {@link ArticuloDTO} a su entidad {@link Articulo}.
     * 
     * @param articuloDTO DTO a convertir.
     * @return Entidad correspondiente con todos los campos del DTO asignados.
     * @author Álvaro Naranjo
     */
    Articulo toEntity(ArticuloDTO articuloDTO);
}
