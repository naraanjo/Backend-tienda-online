package com.ecommerce.naranjo.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.ecommerce.naranjo.dto.DatosFiscalesDTO;
import com.ecommerce.naranjo.model.DatosFiscales;

/**
 * Mapper de la entidad {@link DatosFiscales} a su DTO {@link DatosFiscalesDTO} y viceversa.
 * Utiliza MapStruct para generar automáticamente la implementación en tiempo de compilación,
 * facilitando la conversión entre entidades y DTOs sin escribir código manual.
 *
 * <p>
 * La conversión hacia DTO incluye todos los campos de {@code DatosFiscales}.
 * La conversión hacia entidad ignora la relación {@code persona} para romper ciclos
 * infinitos en relaciones bidireccionales y evitar errores de StackOverflow.
 * </p>
 *
 * <p>
 * Este mapper se registra como componente Spring con {@code componentModel = "spring"},
 * por lo que puede inyectarse en servicios mediante {@code @Autowired}.
 * </p>
 * 
 * @author Álvaro Naranjo
 * @version 1.0
 * @since 2026-01-04
 */
@Mapper(componentModel = "spring")
public interface DatosFiscalesMapper {

    /**
     * Convierte una entidad {@link DatosFiscales} a su DTO {@link DatosFiscalesDTO}.
     *
     * @param entity Entidad de datos fiscales a convertir.
     * @return DTO correspondiente con los campos copiados de la entidad.
     * @author Álvaro Naranjo
     */
    DatosFiscalesDTO toDTO(DatosFiscales entity);

    /**
     * Convierte un DTO {@link DatosFiscalesDTO} a su entidad {@link DatosFiscales}.
     * 
     * <p>
     * La relación {@code persona} se ignora para evitar ciclos infinitos
     * al mapear relaciones bidireccionales (1:1) entre {@link DatosFiscales} y {@link com.ecommerce.naranjo.model.Persona}.
     * </p>
     *
     * @param dto DTO de datos fiscales a convertir.
     * @return Entidad correspondiente con los campos copiados del DTO, 
     *         sin asignar la relación {@code persona}.
     * @author Álvaro Naranjo
     */
    @Mapping(target = "persona", ignore = true)
    DatosFiscales toEntity(DatosFiscalesDTO dto);
}
