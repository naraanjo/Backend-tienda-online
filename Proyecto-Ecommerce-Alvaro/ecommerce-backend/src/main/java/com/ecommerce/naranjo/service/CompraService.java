package com.ecommerce.naranjo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.naranjo.dto.CompraDTO;
import com.ecommerce.naranjo.dto.LineaCompraDTO;
import com.ecommerce.naranjo.model.*;
import com.ecommerce.naranjo.repository.ArticuloRepository;
import com.ecommerce.naranjo.repository.CompraRepository;
import com.ecommerce.naranjo.repository.PersonaRepository;
import com.ecommerce.naranjo.utils.CompraMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio encargado de la lógica de negocio relacionada con las compras.
 * Gestiona la creación de pedidos, validación de stock, cálculo de totales
 * y cambios de estado de las transacciones.
 * * <p>
 * Este servicio actúa como coordinador entre los repositorios de Compra, Artículo
 * y Persona para garantizar la integridad de las operaciones de venta.
 * </p>
 * * @author Álvaro Naranjo
 * @version 1.0
 * @since 2026-01-04
 */
@Service
public class CompraService {

	@Autowired
	private CompraRepository compraRepository;

	@Autowired
	private ArticuloRepository articuloRepository; // Necesario para buscar precios y stock

	@Autowired
	private PersonaRepository personaRepository; // Necesario para buscar al cliente

	@Autowired
	private CompraMapper compraMapper;

	/**
	 * Procesa una compra completa: Valida usuario. Valida stock y precios de cada
	 * artículo. Resta stock. Guarda la compra y sus líneas en una sola transacción.
	 * * <p>
	 * Este método es transaccional; si falla cualquier validación o el guardado,
	 * se hace rollback de toda la operación (incluida la resta de stock).
	 * </p>
	 * * @param compraDTO Objeto con los datos del carrito de compra.
	 * @return El DTO de la compra procesada y guardada.
	 * @throws RuntimeException Si el cliente no existe, el carrito está vacío o no hay stock suficiente.
	 * @author Álvaro Naranjo
	 */
	@Transactional // Se asegura que todo se guarde o nada se guarde
	public CompraDTO crearCompra(CompraDTO compraDTO) {

		
		// Ignoramos el objeto Persona del DTO, buscamos el real por ID.
		Persona cliente = personaRepository.findById(compraDTO.getPersonaId())
				.orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + compraDTO.getPersonaId()));

		// El Mapper copia: calle, ciudad, código postal.
		Compra compra = compraMapper.toEntity(compraDTO);

		// Relleno manual de seguridad:
		compra.setPersona(cliente); // Cliente de la compra real, seguro. No el del DTO ( puede sufrir manupulación
									// )
		compra.setFechaCompra(LocalDateTime.now()); // Fecha de la compra
		compra.setEstado(EstadoCompra.PENDIENTE); // Estado inicial obligatorio

		// Valido que el pedido tenga lineas
		if (compraDTO.getLineas() == null || compraDTO.getLineas().isEmpty()) {
			throw new RuntimeException("No se puede hacer un pedido vacío.");
		}

		// Proceso cada linea del DTO recibido
		for (LineaCompraDTO lineaDTO : compraDTO.getLineas()) {

			// Busco el articulo en la base de datos par obtener los datos seguros
			Articulo articuloReal = articuloRepository.findById(lineaDTO.getArticuloId())
					.orElseThrow(() -> new RuntimeException("Artículo no existe ID: " + lineaDTO.getArticuloId()));

			// Compruebo que hay stock suficiente
			if (articuloReal.getStock() < lineaDTO.getCantidad()) {
				throw new RuntimeException("Stock insuficiente para: " + articuloReal.getNombre() + ". Solicitado: "
						+ lineaDTO.getCantidad() + ", Disponible: " + articuloReal.getStock());
			}

			// Actualizo el stock restando la cantidad comprada
			articuloReal.setStock(articuloReal.getStock() - lineaDTO.getCantidad());
			articuloRepository.save(articuloReal); // Guardamos el nuevo stock

			// Congelo el precio al que se realizo la compra
			LineaCompra lineaEntity = new LineaCompra();
			lineaEntity.setArticulo(articuloReal);
			lineaEntity.setCantidad(lineaDTO.getCantidad());
			lineaEntity.setPrecioSnapshot(articuloReal.getPvpActual()); 

			// Agrego la linea de compra a su correspondiente compra
			compra.agregarLinea(lineaEntity);
		}

		// --- PASO 4: GUARDAR TODO ---
		// Gracias al cascade={PERSIST, MERGE}, al guardar 'compra' se guardan las
		// líneas.
		Compra compraGuardada = compraRepository.save(compra);

		System.out.println("Compra registrada con éxito. ID: " + compraGuardada.getId());

		// Devolvemos el DTO completo con el ID generado y el total calculado
		return compraMapper.toDTO(compraGuardada);
	}

	/**
	 * Obtiene el listado de compras realizadas por un cliente específico.
	 * * @param personaId Identificador del cliente.
	 * @return Lista de compras del cliente en formato DTO.
	 * @author Álvaro Naranjo
	 */
	@Transactional(readOnly = true)
	public List<CompraDTO> listarPorCliente(Long personaId) {
		return compraRepository.findByPersonaId(personaId).stream().map(compraMapper::toDTO)
				.collect(Collectors.toList());
	}

	// 2. Método de Lectura: Listar por Estado
	/**
	 * Obtiene el listado de compras filtradas por su estado actual.
	 * * @param estado El estado de la compra (ej. PENDIENTE, ENVIADO).
	 * @return Lista de compras que coinciden con el estado indicado.
	 * @author Álvaro Naranjo
	 */
	@Transactional(readOnly = true)
	public List<CompraDTO> listarPorEstado(EstadoCompra estado) {
		return compraRepository.findByEstado(estado).stream().map(compraMapper::toDTO)
				.collect(Collectors.toList());
	}

	// 3. Método de Escritura: Cancelar Compra (Baja Lógica)
	/**
	 * Cancela una compra existente cambiando su estado a CANCELADO (Baja lógica).
	 * * <p>
	 * Realiza validaciones previas para asegurar que el pedido no haya sido
	 * enviado o entregado antes de permitir la cancelación.
	 * </p>
	 * * @param id Identificador de la compra a cancelar.
	 * @throws RuntimeException Si la compra no existe o si su estado actual impide la cancelación.
	 * @author Álvaro Naranjo
	 */
	@Transactional
	public void cancelarCompra(Long id) {
		Compra compra = compraRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Compra no encontrada: " + id));

		// Validación: No se puede cancelar si ya se envió
		if (compra.getEstado() == EstadoCompra.ENVIADO || compra.getEstado() == EstadoCompra.ENTREGADO) {
			throw new RuntimeException("No se puede cancelar un pedido que ya ha salido del almacén.");
		}

		// Cambio el estado a CANCELADO
		compra.setEstado(EstadoCompra.CANCELADO);
		compraRepository.save(compra);
		System.out.println("Compra ID " + id + " ha sido CANCELADA.");
	}

}