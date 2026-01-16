package com.ecommerce.naranjo.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

/**
 * Entidad que representa una Persona dentro del sistema de ecommerce.
 * Mapea la tabla PERSONA de la base de datos.
 *
 * <p>
 * Incluye información personal básica, fecha de registro y una relación
 * uno a uno con los datos fiscales.
 * </p>
 *
 * @author Álvaro Naranjo
 * @version 1.0
 * @since 2026-01-04
 */
@Entity
@Table(name = "PERSONA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Persona {

    /**
     * Identificador único de la persona.
     * Generado automáticamente mediante secuencia.
     * @author Álvaro Naranjo
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    /**
     * Nombre completo de la persona.
     * @author Álvaro Naranjo
     */
    @Column(name = "NOMBRE_COMPLETO", nullable = false, length = 150)
    private String nombreCompleto;

    /**
     * Correo electrónico de la persona.
     * Debe ser único dentro del sistema.
     * @author Álvaro Naranjo
     */
    @Column(name = "EMAIL", nullable = false, length = 100, unique = true)
    private String email;

    /**
     * Fecha y hora en la que se registró la persona en el sistema.
     * Se establece automáticamente al crear el registro.
     * No se puede modificar posteriormente.
     * @author Álvaro Naranjo
     */
    @CreationTimestamp
    @Column(name = "FECHA_REGISTRO", updatable = false, nullable = false)
    private LocalDateTime fechaRegistro;

    /**
     * Datos fiscales asociados a la persona.
     * Relación uno a uno bidireccional.
     * Las operaciones realizadas en Persona se propagan a DatosFiscales (CascadeType.ALL).
     * Se carga de manera diferida (lazy) para optimizar rendimiento.
     * @author Álvaro Naranjo
     */
    @OneToOne(mappedBy = "persona", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private DatosFiscales datosFiscales;

    /**
     * Asocia los datos fiscales a la persona y mantiene sincronizada
     * la relación en memoria.
     * 
     * <p>
     * Este método asegura que la relación bidireccional entre Persona y
     * DatosFiscales se mantiene consistente, evitando inconsistencias en la
     * clave foránea.
     * </p>
     *
     * @param datosFiscales datos fiscales que se desean asociar a la persona
     * @author Álvaro Naranjo
     */
    public void setDatosFiscales(DatosFiscales datosFiscales) {
        this.datosFiscales = datosFiscales;
        if (datosFiscales != null) {
            datosFiscales.setPersona(this);
        }
    }
}
