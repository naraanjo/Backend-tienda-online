package com.ecommerce.naranjo.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

/**
 * Entidad que representa una compra realizada dentro del sistema de ecommerce.
 * Mapea la tabla {@code COMPRA} de la base de datos.
 *
 * <p>
 * La compra actúa como entidad cabecera de un pedido y contiene la información
 * principal de la operación, incluyendo la fecha de realización, el estado del
 * pedido, los datos de envío y las líneas de productos adquiridos.
 * </p>
 *
 * <p>
 * La información de dirección se almacena directamente en la compra para
 * preservar el histórico exacto del envío, incluso si los datos de la persona
 * cambian con el tiempo.
 * </p>
 *
 * <p>
 * La relación con {@link Persona} es de tipo muchos a uno, mientras que la
 * relación con {@link LineaCompra} es de uno a muchos, manteniendo la
 * integridad y consistencia de la estructura del pedido.
 * </p>
 *
 * @author Álvaro Naranjo
 * @version 1.0
 * @since 2026-01-04
 */
@Entity
@Table(name = "COMPRA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compra {

	/**
	 * Identificador único de la compra. Se genera automáticamente mediante
	 * secuencia.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Fecha y hora en la que se realizó la compra. Se establece en el momento de
	 * creación y no puede ser modificada posteriormente.
	 */
	@CreationTimestamp
	@Column(name = "FECHA_COMPRA", nullable = false, updatable = false)
	private LocalDateTime fechaCompra;

	/**
	 * Estado actual de la compra. Controla el ciclo de vida del pedido dentro del
	 * sistema.
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "ESTADO", nullable = false, length = 20)
	private EstadoCompra estado;

	/**
	 * Calle correspondiente a la dirección de envío registrada para la compra.
	 */
	@Column(name = "CALLE", nullable = false, length = 150)
	private String calle;

	/**
	 * Ciudad correspondiente a la dirección de envío registrada para la compra.
	 */
	@Column(name = "CIUDAD", nullable = false, length = 100)
	private String ciudad;

	/**
	 * Código postal de la dirección de envío de la compra.
	 */
	@Column(name = "CODIGO_POSTAL", nullable = false, length = 10)
	private String codigoPostal;

	/**
	 * Persona que realiza la compra. Relación de muchos a uno con carga diferida
	 * para optimizar rendimiento.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSONA_ID", nullable = false)
	@ToString.Exclude
	private Persona persona;

	/**
	 * Conjunto de líneas que componen la compra.
	 *
	 * <p>
	 * Se permite la propagación de operaciones de persistencia y actualización,
	 * manteniendo control estricto sobre las eliminaciones para preservar el
	 * historial.
	 * </p>
	 */
	@OneToMany(mappedBy = "compra", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@ToString.Exclude
	@Builder.Default
	private List<LineaCompra> lineas = new ArrayList<>();

	/**
	 * Añade una línea de compra a la compra actual y mantiene sincronizada la
	 * relación bidireccional en memoria.
	 *
	 * @param linea línea de compra a asociar
	 */
	public void agregarLinea(LineaCompra linea) {
		if (linea != null) {
			linea.setCompra(this);
			this.lineas.add(linea);
		}
	}

	/**
	 * Calcula dinámicamente el importe total de la compra.
	 *
	 * <p>
	 * El total no se persiste en base de datos para evitar inconsistencias y se
	 * obtiene siempre a partir de las líneas asociadas.
	 * </p>
	 *
	 * @return importe total calculado de la compra
	 */
	@Transient

	public BigDecimal getTotalCalculado() {
		if (this.lineas == null || this.lineas.isEmpty()) {
			return BigDecimal.ZERO;
		}
		// Suma el (precio_snapshot * cantidad) de cada línea
		return this.lineas.stream().map(linea -> {
			BigDecimal precio = linea.getPrecioSnapshot() != null ? linea.getPrecioSnapshot() : BigDecimal.ZERO;
			BigDecimal cantidad = linea.getCantidad() != null ? new BigDecimal(linea.getCantidad()) : BigDecimal.ZERO;
			return precio.multiply(cantidad);
		}).reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
