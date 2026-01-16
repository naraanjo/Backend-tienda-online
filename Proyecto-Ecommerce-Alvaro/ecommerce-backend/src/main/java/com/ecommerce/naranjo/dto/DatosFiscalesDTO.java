package com.ecommerce.naranjo.dto;

import lombok.Data;

/**
 * Data Transfer Object (DTO) para la entidad {@link com.ecommerce.naranjo.model.DatosFiscales}.
 * Se utiliza para transferir información de los datos fiscales de una persona
 * entre capas de la aplicación (controlador, servicio, presentación) sin exponer
 * directamente la entidad.
 * 
 * <p>
 * Incluye información de identificación fiscal, dirección y contacto telefónico.
 * Se utilizan tipos objeto (Long, String) para permitir valores nulos en caso
 * de campos opcionales o datos incompletos.
 * </p>
 *
 * <p>
 * Se recomienda usar este DTO en las capas de servicio y controladores para
 * proteger la entidad original y manejar validaciones y transformaciones de manera centralizada.
 * </p>
 * 
 * @author Álvaro Naranjo
 * @version 1.0
 * @since 2026-01-04
 */
@Data
public class DatosFiscalesDTO {

    /**
     * Identificador único de los datos fiscales.
     * Puede ser nulo si el registro aún no ha sido persistido.
     * @author Álvaro Naranjo
     */
    private Long id;

    /**
     * Número de identificación fiscal (NIF/CIF) de la persona.
     * @author Álvaro Naranjo
     */
    private String nifCif;

    /**
     * Calle de la dirección fiscal.
     * @author Álvaro Naranjo
     */
    private String calle;

    /**
     * Ciudad de residencia fiscal.
     * @author Álvaro Naranjo
     */
    private String ciudad;

    /**
     * Código postal de la dirección fiscal.
     * @author Álvaro Naranjo
     */
    private String codigoPostal;

    /**
     * Número de teléfono de contacto.
     * @author Álvaro Naranjo
     */
    private String telefono;
}
