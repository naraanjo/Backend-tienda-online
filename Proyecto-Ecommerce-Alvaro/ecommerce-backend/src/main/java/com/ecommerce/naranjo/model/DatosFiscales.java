package com.ecommerce.naranjo.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa los datos fiscales de una {@link Persona} en el sistema de ecommerce.
 * Mapea la tabla DATOS_FISCALES de la base de datos.
 *
 * <p>
 * Incluye información de identificación fiscal, dirección y contacto telefónico.
 * Mantiene una relación uno a uno bidireccional con la entidad {@link Persona}.
 * </p>
 * 
 * <p>
 * Cada registro de DatosFiscales pertenece a exactamente una Persona.
 * </p>
 * 
 * @author Álvaro Naranjo
 * @version 1.0
 * @since 2026-01-04
 */
@Entity
@Table(name = "DATOS_FISCALES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatosFiscales {

    /**
     * Identificador único de los datos fiscales.
     * Generado automáticamente mediante secuencia global.
     * @author Álvaro Naranjo
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Número de identificación fiscal (NIF/CIF) de la persona.
     * @author Álvaro Naranjo
     */
    @Column(name = "NIF_CIF", nullable = false, length = 20)
    private String nifCif;

    /**
     * Calle de la residencia fiscal.
     * @author Álvaro Naranjo
     */
    @Column(name = "CALLE", nullable = false, length = 150)
    private String calle;

    /**
     * Ciudad de residencia fiscal.
     * @author Álvaro Naranjo
     */
    @Column(name = "CIUDAD", nullable = false, length = 100)
    private String ciudad;

    /**
     * Código postal de la dirección fiscal.
     * @author Álvaro Naranjo
     */
    @Column(name = "CODIGO_POSTAL", nullable = false, length = 10)
    private String codigoPostal;

    /**
     * Teléfono de contacto opcional.
     * @author Álvaro Naranjo
     */
    @Column(name = "TELEFONO", nullable = false, length = 20)
    private String telefono;

    /**
     * Persona asociada a estos datos fiscales.
     * Relación uno a uno bidireccional.
     *
     * <p>
     * Es el dueño de la relación en la base de datos, ya que contiene la columna
     * {@code PERSONA_ID} que es clave foránea hacia {@link Persona}.
     * </p>
     *
     * <p>
     * Se carga de manera diferida (lazy) para optimizar rendimiento y evitar
     * consultas innecesarias al cargar la entidad.
     * </p>
     * 
     * @author Álvaro Naranjo
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID", nullable = false, unique = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Persona persona;
}
