package com.ecommerce.naranjo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ecommerce.naranjo.model.Compra;
import com.ecommerce.naranjo.model.EstadoCompra;

import java.util.List;

/**
 * Repositorio de acceso a datos para la entidad {@link Compra}.
 * Proporciona las operaciones CRUD estándar y consultas especializadas
 * relacionadas con el historial de compras del sistema.
 *
 * <p>
 * Esta interfaz utiliza Spring Data JPA para generar automáticamente
 * las implementaciones de acceso a base de datos en tiempo de ejecución,
 * permitiendo interactuar con la tabla {@code COMPRA} sin necesidad de
 * escribir código SQL manual.
 * </p>
 *
 * <p>
 * Los métodos definidos permiten consultar compras por cliente y por
 * estado del pedido, facilitando la construcción de funcionalidades
 * como historial de pedidos, seguimiento y gestión administrativa.
 * </p>
 *
 * @author Álvaro Naranjo
 * @version 1.0
 * @since 2026-01-04
 */
@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

    /**
     * Obtiene todas las compras realizadas por un cliente concreto.
     *
     * <p>
     * Esta consulta se resuelve mediante el identificador de la persona
     * asociada a la compra.
     * </p>
     *
     * @param personaId identificador del cliente
     * @return listado de compras realizadas por el cliente indicado
     * @author Álvaro Naranjo
     */
    List<Compra> findByPersonaId(Long personaId);

    /**
     * Obtiene las compras que se encuentran en un estado determinado.
     *
     * <p>
     * El uso del enumerado {@link EstadoCompra} garantiza coherencia entre
     * la capa de aplicación y el valor almacenado en la base de datos,
     * evitando errores de escritura y problemas de consistencia.
     * </p>
     *
     * @param estado estado de la compra a filtrar
     * @return listado de compras que se encuentran en el estado indicado
     * @author Álvaro Naranjo
     */
    List<Compra> findByEstado(EstadoCompra estado);
}
