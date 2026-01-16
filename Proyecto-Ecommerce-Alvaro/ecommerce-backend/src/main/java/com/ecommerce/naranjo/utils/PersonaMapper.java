package com.ecommerce.naranjo.utils;
import org.springframework.stereotype.Component;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.ecommerce.naranjo.dto.PersonaDTO;
import com.ecommerce.naranjo.model.Persona;

/**
 * Mapper de la entidad {@link Persona} a su DTO {@link PersonaDTO} y viceversa.
 * Utiliza MapStruct para generar automáticamente la implementación en tiempo de compilación,
 * facilitando la conversión entre entidades y DTOs sin escribir código manual.
 *
 * <p>
 * Incluye la conversión de los datos fiscales mediante el uso del {@link DatosFiscalesMapper}.
 * Esto permite mapear correctamente {@link Persona#datosFiscales} hacia el DTO anidado
 * {@link PersonaDTO#datosFiscales}.
 * </p>
 *
 * <p>
 * La conversión de DTO a entidad ignora la relación {@code datosFiscales} para romper
 * ciclos potenciales y permitir la gestión manual de la vinculación de IDs
 * en la capa de servicio. Esto evita problemas de recursión infinita
 * y facilita el control de la persistencia de relaciones bidireccionales.
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
@Mapper(componentModel = "spring", uses = {DatosFiscalesMapper.class})
public interface PersonaMapper {

    /**
     * Convierte una entidad {@link Persona} a su DTO {@link PersonaDTO}.
     *
     * @param persona Entidad a convertir.
     * @return DTO correspondiente con todos los campos de la entidad, incluyendo los datos fiscales anidados.
     * @author Álvaro Naranjo
     */
    PersonaDTO toDTO(Persona persona);

    /**
     * Convierte un DTO {@link PersonaDTO} a su entidad {@link Persona}.
     * 
     * <p>
     * La relación {@code datosFiscales} se ignora en esta conversión automática para evitar
     * ciclos infinitos en la relación bidireccional entre {@link Persona} y {@link com.ecommerce.naranjo.model.DatosFiscales}.
     * La vinculación de {@code datosFiscales} debe gestionarse manualmente en el servicio.
     * </p>
     *
     * @param personaDTO DTO a convertir.
     * @return Entidad correspondiente con los campos básicos asignados, sin asignar la relación {@code datosFiscales}.
     * @author Álvaro Naranjo
     */
    @Mapping(target = "datosFiscales", ignore = true)
    Persona toEntity(PersonaDTO personaDTO);
}
