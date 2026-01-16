package com.ecommerce.naranjo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.naranjo.dto.ArticuloDTO;
import com.ecommerce.naranjo.model.Articulo;
import com.ecommerce.naranjo.repository.ArticuloRepository;
import com.ecommerce.naranjo.utils.ArticuloMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de lógica de negocio para la gestión del catálogo de {@link Articulo}.
 *
 * <p>
 * Esta clase centraliza todas las reglas de negocio relacionadas con los artículos:
 * validación de precios y stock, persistencia, consultas y filtrados.
 * Actúa como intermediario entre los controladores y la capa de persistencia.
 * </p>
 *
 * <p>
 * Utiliza {@link ArticuloRepository} para el acceso a datos y {@link ArticuloMapper}
 * para la conversión entre entidades y DTOs.
 * </p>
 *
 * <p>
 * Todos los métodos de escritura se ejecutan dentro de transacciones para garantizar
 * consistencia e integridad de los datos.
 * </p>
 *
 * @author Álvaro Naranjo
 * @version 1.0
 * @since 2026-01-04
 */
@Service
public class ArticuloService {

    @Autowired
    private ArticuloRepository articuloRepository;

    @Autowired
    private ArticuloMapper articuloMapper;

    /**
     * Crea o actualiza un artículo en el catálogo.
     *
     * <p>
     * Aplica las siguientes reglas de negocio:
     * </p>
     * <ul>
     *   <li>El precio no puede ser nulo ni negativo.</li>
     *   <li>El stock no puede ser negativo.</li>
     * </ul>
     * 
     *
     * <p>
     * Convierte el DTO a entidad, persiste la información y devuelve el artículo
     * almacenado en formato DTO.
     * </p>
     *
     * @param articuloDTO DTO con la información del artículo a guardar.
     * @return DTO del artículo persistido con su identificador asignado.
     * @throws IllegalArgumentException si el precio o el stock son inválidos.
     *
     * @author Álvaro Naranjo
     */
    @Transactional
    public ArticuloDTO guardarArticulo(ArticuloDTO articuloDTO) {
        
        // Validaciones necesarias
        if (articuloDTO.getPvpActual() == null || articuloDTO.getPvpActual().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio (PVP) no puede ser negativo ni nulo.");
        }
        
        if (articuloDTO.getStock() == null || articuloDTO.getStock() < 0) {
            throw new IllegalArgumentException("El stock inicial no puede ser negativo.");
        }

        // Paso de DTO a Entidad
        Articulo entidad = articuloMapper.toEntity(articuloDTO);

        // Guardado en base de datos
        Articulo guardado = articuloRepository.save(entidad);

        // 4. Retorno convertido
        return articuloMapper.toDTO(guardado);
    }

    /**
     * Obtiene el listado completo de artículos registrados en el sistema.
     *
     * @return Lista de {@link ArticuloDTO} con todos los artículos.
     *
     * @author Álvaro Naranjo
     */
    @Transactional(readOnly = true)
    public List<ArticuloDTO> listarTodos() {
        return articuloRepository.findAll().stream()
                .map(articuloMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca un artículo por su identificador único.
     *
     * @param id Identificador del artículo.
     * @return DTO del artículo encontrado.
     * @throws RuntimeException si no existe un artículo con el ID indicado.
     *
     * @author Álvaro Naranjo
     */
    @Transactional(readOnly = true)
    public ArticuloDTO buscarPorId(Long id) {
        Articulo articulo = articuloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artículo no encontrado con ID: " + id));
        return articuloMapper.toDTO(articulo);
    }

    /**
     * Busca artículos cuyo nombre contenga el texto indicado.
     *
     * <p>
     * La búsqueda es insensible a mayúsculas y minúsculas.
     * </p>
     *
     * @param textoBusqueda Texto parcial del nombre del artículo.
     * @return Lista de artículos que coinciden con el criterio.
     *
     * @author Álvaro Naranjo
     */
    @Transactional(readOnly = true)
    public List<ArticuloDTO> buscarPorNombre(String textoBusqueda) {
        return articuloRepository.findByNombreContainingIgnoreCase(textoBusqueda).stream()
                .map(articuloMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene únicamente los artículos con stock disponible.
     *
     * <p>
     * Se consideran disponibles los artículos con stock mayor que cero.
     * </p>
     *
     * @return Lista de artículos disponibles.
     *
     * @author Álvaro Naranjo
     */
    @Transactional(readOnly = true)
    public List<ArticuloDTO> listarSoloDisponibles() {
        return articuloRepository.findByStockGreaterThan(0).stream()
                .map(articuloMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Elimina un artículo del sistema de forma permanente,
     * en caso de que no este en ninguna compra.
     *
     * @param id Identificador del artículo a eliminar.
     * @throws RuntimeException si el artículo no existe.
     *
     * @author Álvaro Naranjo
     */
    @Transactional
    public void borrarArticulo(Long id) {
        if (!articuloRepository.existsById(id)) {
             throw new RuntimeException("No se puede borrar. ID no existe: " + id);
        }
        try {
            // 2. Intentamos borrar
            articuloRepository.deleteById(id);
            
        } catch (DataIntegrityViolationException e) {
            //Notifico que el articulo no se puede borrar porque pertenece a alguna compra
            throw new IllegalStateException("No se puede eliminar el artículo porque ya forma parte de una o más compras realizadas. ");
        }    }
}
