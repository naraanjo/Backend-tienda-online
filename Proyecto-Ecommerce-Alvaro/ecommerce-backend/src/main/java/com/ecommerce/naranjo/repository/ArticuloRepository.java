package com.ecommerce.naranjo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ecommerce.naranjo.model.Articulo;
import java.util.List;

/**
 * Repositorio de acceso a datos para la entidad {@link Articulo}.
 * Extiende {@link JpaRepository} para proporcionar operaciones CRUD básicas
 * y consultas adicionales definidas mediante métodos de Spring Data JPA.
 * 
 * <p>
 * Proporciona métodos para buscar artículos por nombre parcial y para filtrar
 * por stock disponible, evitando la necesidad de escribir consultas manuales.
 * </p>
 * 
 * <p>
 * Spring Data JPA genera automáticamente la implementación en tiempo de ejecución,
 * por lo que no es necesario crear una clase {@code ArticuloRepositoryImpl} a menos
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
public interface ArticuloRepository extends JpaRepository<Articulo, Long> {

    /**
     * Busca artículos cuyo nombre contenga el texto indicado, ignorando mayúsculas y minúsculas.
     * 
     * <p>
     * SQL equivalente: <code>SELECT * FROM ARTICULO WHERE UPPER(NOMBRE) LIKE UPPER('%texto%')</code>
     * </p>
     * 
     * @param texto Texto parcial a buscar dentro del nombre del artículo.
     * @return Lista de {@link Articulo} que contienen el texto en su nombre.
     * @author Álvaro Naranjo
     */
    List<Articulo> findByNombreContainingIgnoreCase(String texto);

    /**
     * Busca artículos que tengan stock mayor a la cantidad indicada.
     * 
     * <p>
     * Útil para filtrar artículos disponibles en inventario.
     * SQL equivalente: <code>SELECT * FROM ARTICULO WHERE STOCK > ?</code>
     * </p>
     * 
     * @param cantidad Cantidad mínima de stock requerida.
     * @return Lista de {@link Articulo} con stock mayor al valor proporcionado.
     * @author Álvaro Naranjo

     */
    List<Articulo> findByStockGreaterThan(Integer cantidad);
}
