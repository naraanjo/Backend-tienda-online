package com.ecommerce.naranjo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ecommerce.naranjo.model.DatosFiscales;
import java.util.Optional;

/**
 * Repositorio de acceso a datos para la entidad {@link DatosFiscales}.
 * Extiende {@link JpaRepository} para proporcionar operaciones CRUD básicas
 * y consultas adicionales definidas mediante métodos de Spring Data JPA.
 * 
 * <p>
 * Proporciona métodos para buscar datos fiscales por NIF/CIF, evitando la necesidad
 * de implementar la lógica manualmente.
 * </p>
 * 
 * <p>
 * Spring Data JPA genera automáticamente la implementación en tiempo de ejecución,
 * por lo que no es necesario crear una clase {@code DatosFiscalesRepositoryImpl}
 * a menos que se requiera lógica personalizada compleja.
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
public interface DatosFiscalesRepository extends JpaRepository<DatosFiscales, Long> {

    /**
     * Busca los datos fiscales de una persona por su NIF o CIF.
     * 
     * <p>
     * Este método es un ejemplo de "consulta derivada" de Spring Data JPA, donde
     * el nombre del método genera automáticamente la consulta SQL correspondiente.
     * </p>
     *
     * @param nifCif NIF o CIF de la persona cuyos datos fiscales se desean buscar.
     * @return Un {@link Optional} que contiene los datos fiscales si existen, o vacío si no.
     * @author Álvaro Naranjo
     */
    Optional<DatosFiscales> findByNifCif(String nifCif);
}
