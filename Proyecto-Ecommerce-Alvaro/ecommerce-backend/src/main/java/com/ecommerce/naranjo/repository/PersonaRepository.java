package com.ecommerce.naranjo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ecommerce.naranjo.model.Persona;
import java.util.Optional;

/**
 * Repositorio de acceso a datos para la entidad {@link Persona}.
 * Extiende {@link JpaRepository} para proporcionar operaciones CRUD básicas
 * y consultas adicionales definidas mediante métodos de Spring Data JPA.
 * 
 * <p>
 * Proporciona métodos para buscar personas por email y para verificar existencia
 * de un email en la base de datos, evitando la necesidad de implementar
 * la lógica manualmente.
 * </p>
 * 
 * <p>
 * Spring Data JPA genera automáticamente la implementación en tiempo de ejecución,
 * por lo que no es necesario crear una clase {@code PersonaRepositoryImpl} a menos
 * que se requiera lógica personalizada compleja.
 * </p>
 * 
 * <p>
 * Todos los métodos devuelven resultados dentro del contexto de Spring y se pueden
 * usar de forma segura en servicios y controladores.
 * </p>
 * 
 * @author Álvaro Naranjo
 * @version 1.0
 * @since 2026-01-04
 */
@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {

    /**
     * Busca una persona por su correo electrónico.
     *
     * <p>
     * Este método es una "consulta derivada" de Spring Data JPA, donde el
     * nombre del método genera automáticamente la consulta SQL correspondiente.
     * </p>
     *
     * @param email Correo electrónico de la persona a buscar.
     * @return Un {@link Optional} que contiene la persona si existe, o vacío si no.
     * @author Álvaro Naranjo
     */
    Optional<Persona> findByEmail(String email);

    /**
     * Verifica si existe una persona con un correo electrónico determinado.
     *
     * <p>
     * Método derivado de Spring Data JPA que genera un SELECT COUNT(*) > 0
     * automáticamente en la base de datos.
     * </p>
     *
     * @param email Correo electrónico a verificar.
     * @return {@code true} si existe al menos una persona con el email dado; {@code false} en caso contrario.
     * @author Álvaro Naranjo
     */
    boolean existsByEmail(String email);
}
