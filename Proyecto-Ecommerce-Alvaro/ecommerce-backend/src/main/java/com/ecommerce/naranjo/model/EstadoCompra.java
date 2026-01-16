package com.ecommerce.naranjo.model;

/**
 * Enumeración que representa los estados posibles de una {@link Compra}.
 * 
 * <p>
 * Este enum define el ciclo de vida lógico de un pedido dentro del sistema
 * de ecommerce. Cada valor corresponde directamente a los valores permitidos
 * por la restricción {@code CHECK} definida en la base de datos Oracle sobre
 * la columna {@code ESTADO} de la tabla {@code COMPRA}.
 * </p>
 * 
 * <p>
 * Es obligatorio que los valores de esta enumeración coincidan exactamente
 * en nombre y formato con los valores almacenados en la base de datos para
 * garantizar la integridad y evitar errores de persistencia.
 * </p>
 * 
 * <p>
 * Estados posibles:
 * </p>
 * <ul>
 *   <li>{@link #PENDIENTE} Pedido creado, pendiente de procesamiento.</li>
 *   <li>{@link #ENVIADO} Pedido despachado y en tránsito.</li>
 *   <li>{@link #ENTREGADO} Pedido entregado al cliente.</li>
 *   <li>{@link #CANCELADO} Pedido cancelado antes de su finalización.</li>
 * </ul>
 * 
 * 
 * @author Álvaro Naranjo
 * @version 1.0
 * @since 2026-01-04
 */
public enum EstadoCompra {

    /**
     * Pedido creado y pendiente de procesamiento.
     */
    PENDIENTE,

    /**
     * Pedido despachado y actualmente en tránsito.
     */
    ENVIADO,

    /**
     * Pedido entregado correctamente al cliente.
     */
    ENTREGADO,

    /**
     * Pedido cancelado antes de completarse.
     */
    CANCELADO
}
