package com.ecommerce.naranjo.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) para la entidad {@link com.ecommerce.naranjo.model.Persona}.
 * Se utiliza para transferir información de una persona entre capas de la aplicación
 * (controlador, servicio, presentación) sin exponer directamente la entidad.
 * 
 * <p>
 * Incluye información básica de la persona, fecha de registro y un DTO anidado
 * de sus datos fiscales. Este DTO facilita la serialización y deserialización
 * en las API REST y protege la entidad original de modificaciones directas.
 * </p>
 * 
 * <p>
 * El campo {@code fechaRegistro} se considera de solo lectura y refleja
 * la fecha y hora en que la persona fue registrada en el sistema.
 * </p>
 * 
 * <p>
 * Se recomienda utilizar este DTO en controladores y servicios para
 * enviar y recibir información de manera segura, evitando exponer la entidad completa.
 * </p>
 * 
 * @author Álvaro Naranjo
 * @version 1.0
 * @since 2026-01-04
 */
@Data
public class PersonaDTO {

    /**
     * Identificador único de la persona.
     * Puede ser nulo si el registro aún no ha sido persistido.
     * @author Álvaro Naranjo
     */
    private Long id;

    /**
     * Nombre completo de la persona.
     * @author Álvaro Naranjo
     */
    private String nombreCompleto;

    /**
     * Correo electrónico de la persona.
     * @author Álvaro Naranjo
     */
    private String email;

    /**
     * Fecha y hora en que la persona fue registrada en el sistema.
     * Campo de solo lectura en la mayoría de los casos.
     * @author Álvaro Naranjo
     */
    private LocalDateTime fechaRegistro;

    /**
     * Datos fiscales asociados a la persona.
     * Se representa mediante un DTO anidado {@link DatosFiscalesDTO}.
     * @author Álvaro Naranjo
     */
    private DatosFiscalesDTO datosFiscales;
}
