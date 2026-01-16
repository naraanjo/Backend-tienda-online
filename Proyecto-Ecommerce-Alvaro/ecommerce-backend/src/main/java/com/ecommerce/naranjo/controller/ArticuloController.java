package com.ecommerce.naranjo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.ecommerce.naranjo.dto.ArticuloDTO;
import com.ecommerce.naranjo.service.ArticuloService;

import java.util.List;

/**
 * Controlador responsable de la gestin de los articulos *
 * <p>
 * Esta clase actúa como punto de entrada de la aplicación para todas las
 * operaciones relacionadas con productos. Orquesta las peticiones de la capa
 * de presentación y delega la lógica de negocio en {@link ArticuloService}.
 * </p>
 *
 * <p>
 * El controlador opera exclusivamente con objetos {@link ArticuloDTO}, evitando
 * exponer directamente las entidades de persistencia.
 * </p>
 *
 * @author Álvaro Naranjo
 * @version 1.0
 * @since 2026-01-04
 */
@Controller
public class ArticuloController {

    @Autowired
    private ArticuloService articuloService;

    /**
     * Crea o actualiza un artículo dentro del catálogo.
     *
     * <p>
     * Recibe los datos del artículo desde la capa de presentación, los envía al
     * servicio para su validación y persistencia y devuelve el resultado
     * completamente actualizado.
     * </p>
     *
     * @param articuloDTO Información del artículo a registrar o actualizar.
     * @return Artículo persistido con su identificador generado o actualizado.
     *
     * @author Álvaro Naranjo
     */
    public ArticuloDTO guardarArticulo(ArticuloDTO articuloDTO) {
        System.out.println("--> [ARTICULO-CONTROLLER] Guardando producto: " + articuloDTO.getNombre());
        try {
            return articuloService.guardarArticulo(articuloDTO);
        } catch (IllegalArgumentException e) {
            System.err.println("Error de validación: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Obtiene el listado completo del catálogo de artículos.
     *
     * <p>
     * Incluye tanto artículos disponibles como aquellos sin stock.
     * </p>
     *
     * @return Lista completa de artículos del sistema.
     *
     * @author Álvaro Naranjo
     */
    public List<ArticuloDTO> listarTodos() {
        System.out.println("--> [ARTICULO-CONTROLLER] Listando catálogo completo.");
        return articuloService.listarTodos();
    }

    /**
     * Obtiene únicamente los artículos disponibles para la venta.
     *
     * <p>
     * Se consideran disponibles aquellos artículos cuyo stock es mayor que cero.
     * </p>
     *
     * @return Lista de artículos con stock disponible.
     *
     * @author Álvaro Naranjo
     */
    public List<ArticuloDTO> listarDisponiblesParaVenta() {
        System.out.println("--> [ARTICULO-CONTROLLER] Filtrando productos con stock...");
        return articuloService.listarSoloDisponibles();
    }

    /**
     * Busca artículos cuyo nombre contenga el texto indicado.
     *
     * <p>
     * La búsqueda es flexible y no distingue entre mayúsculas y minúsculas.
     * </p>
     *
     * @param texto Texto introducido por el usuario como criterio de búsqueda.
     * @return Lista de artículos que coinciden con el criterio proporcionado.
     *
     * @author Álvaro Naranjo
     */
    public List<ArticuloDTO> buscarPorNombre(String texto) {
        System.out.println("--> [ARTICULO-CONTROLLER] Buscando coincidencia con: '" + texto + "'");
        return articuloService.buscarPorNombre(texto);
    }

    /**
     * Obtiene el detalle completo de un artículo a partir de su identificador.
     *
     * @param id Identificador del artículo.
     * @return Artículo correspondiente al identificador indicado.
     *
     * @author Álvaro Naranjo
     */
    public ArticuloDTO obtenerPorId(Long id) {
        return articuloService.buscarPorId(id);
    }
    
    /**
     * Elimina un artículo del catálogo de forma permanente.
     *
     * @param id Identificador del artículo a eliminar.
     *
     * @author Álvaro Naranjo
     */
    public void eliminarArticulo(Long id) {
        System.out.println("--> [ARTICULO-CONTROLLER] Eliminando producto ID: " + id);
        try {
            articuloService.borrarArticulo(id);
        } catch (Exception e) {
            System.err.println("No se pudo borrar: " );
            throw e;
        }
    }
}
