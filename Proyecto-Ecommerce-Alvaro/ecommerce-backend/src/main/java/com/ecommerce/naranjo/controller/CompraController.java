package com.ecommerce.naranjo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.ecommerce.naranjo.dto.CompraDTO;
import com.ecommerce.naranjo.model.EstadoCompra;
import com.ecommerce.naranjo.service.CompraService;

import java.util.List;

/**
 * Controlador encargado de gestionar el flujo de las operaciones de compra.
 * Actúa como punto de entrada para procesar ventas, consultar historiales
 * y gestionar los estados de los pedidos.
 * * <p>
 * Delega la lógica de negocio compleja en {@link CompraService} y maneja
 * las excepciones básicas para la capa de presentación.
 * </p>
 * * @author Álvaro Naranjo
 * @version 1.0
 * @since 2026-01-04
 */
@Controller
public class CompraController {

    @Autowired
    private CompraService compraService;

    /**
     * Recibe un carrito de la compra (DTO) y procesa la venta.
     * * <p>
     * Este método coordina la creación del ticket de compra, delegando la validación
     * de stock y precios al servicio. Si ocurre algún error durante el proceso,
     * captura la excepción para loguearla y la relanza para notificar al usuario.
     * </p>
     * * @param compraDTO Objeto de transferencia con los datos del cliente y líneas de compra.
     * @return El ticket generado (DTO) con ID asignado y totales calculados.
     * @throws RuntimeException Si hay problemas de stock, validación o persistencia.
     * * @author Álvaro Naranjo
     */
    public CompraDTO procesarCompra(CompraDTO compraDTO) {
        System.out.println("--> [COMPRA-CONTROLLER] Iniciando proceso de compra...");
        try {
            // Delegamos la lógica compleja al servicio
            CompraDTO ticket = compraService.crearCompra(compraDTO);
            System.out.println(" Venta finalizada. Ticket Nº: " + ticket.getId());
            return ticket;
        } catch (RuntimeException e) {
            System.err.println(" Error en la compra: " + e.getMessage());
            // Re-lanzamos para que la pantalla pueda mostrar un mensaje de alerta al usuario
            throw e; 
        }
    }

    /**
     * Obtiene el historial de compras de un cliente específico.
     * * <p>
     * Recupera todas las compras asociadas a un identificador de persona,
     * independientemente de su estado actual.
     * </p>
     * * @param personaId Identificador único del cliente.
     * @return Lista de DTOs con las compras realizadas por el cliente.
     * * @author Álvaro Naranjo
     */
    public List<CompraDTO> verHistorialCliente(Long personaId) {
        System.out.println("--> [COMPRA-CONTROLLER] Buscando historial del cliente ID: " + personaId);
        return compraService.listarPorCliente(personaId);
    }

    /**
     * Filtra los pedidos existentes según su estado actual.
     * * <p>
     * Útil para operaciones administrativas o de seguimiento, como ver todos
     * los pedidos pendientes de envío.
     * </p>
     * * @param estado El estado por el cual se desea filtrar (ej: PENDIENTE, ENVIADO).
     * @return Lista de compras que coinciden con el estado proporcionado.
     * * @author Álvaro Naranjo
     */
    public List<CompraDTO> verPorEstado(EstadoCompra estado) {
        return compraService.listarPorEstado(estado);
    }
    
    /**
     * Anula un pedido realizando una baja lógica.
     * * <p>
     * Este método cambia el estado del pedido a CANCELADO. No se permite el borrado
     * físico de la compra para mantener la integridad referencial y el histórico
     * de auditoría (ON DELETE RESTRICT).
     * </p>
     * * @param idCompra Identificador único de la compra a cancelar.
     * * @author Álvaro Naranjo
     */
    public void anularPedido(Long idCompra) {
        System.out.println("--> [COMPRA-CONTROLLER] Solicitando anulación de compra ID: " + idCompra);
        compraService.cancelarCompra(idCompra);
    }
}