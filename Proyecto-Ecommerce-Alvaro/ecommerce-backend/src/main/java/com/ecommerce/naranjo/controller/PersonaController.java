package com.ecommerce.naranjo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.ecommerce.naranjo.dto.PersonaDTO;
import com.ecommerce.naranjo.service.PersonaService;
import java.util.List;

/**
 * Controlador principal para la gestión de {@link PersonaDTO}.
 * Actúa como punto de entrada para las operaciones relacionadas con clientes.
 * 
 * <p>
 * Esta clase oculta la complejidad de la lógica de negocio y persistencia.
 * La aplicación externa solo interactúa con DTOs a través de estos métodos.
 * </p>
 * 
 * @author Álvaro Naranjo
 * @version 1.0
 * @since 2026-01-04
 */
@Controller
public class PersonaController {

    @Autowired
    private PersonaService personaService;

    /**
     * Crea un nuevo cliente en el sistema.
     * 
     * <p>
     * Este método recibe un {@link PersonaDTO} con los datos personales y
     * fiscales del cliente, delega la creación al servicio y devuelve el DTO
     * persistido con el ID generado y la fecha de registro.
     * </p>
     * 
     * @param personaDTO DTO con los datos del cliente a crear.
     * @return DTO del cliente creado, incluyendo ID y fecha de registro.
     * @author Álvaro Naranjo
     */
    public PersonaDTO crearCliente(PersonaDTO personaDTO) {
        System.out.println("--> [CONTROLLER] Solicitando creación de cliente: " + personaDTO.getEmail());
        return personaService.crearPersona(personaDTO);
    }

    /**
     * Obtiene el listado completo de clientes registrados.
     * 
     * <p>
     * Devuelve una lista de {@link PersonaDTO} incluyendo los datos fiscales
     * asociados a cada cliente.
     * </p>
     * 
     * @return Lista de DTOs con todos los clientes registrados.
     * @author Álvaro Naranjo
     */
    public List<PersonaDTO> obtenerTodosLosClientes() {
        System.out.println("--> [CONTROLLER] Listando todos los clientes...");
        return personaService.listarTodas();
    }

    /**
     * Realiza la baja lógica (anonimización) de un cliente, sin eliminar su historial de facturación.
     * 
     * <p>
     * Este método recibe el ID de un cliente y delega la anonimización de sus datos
     * al servicio correspondiente. Los datos personales y fiscales se sobrescriben
     * con valores genéricos, pero la entidad permanece en la base de datos para mantener
     * integridad referencial y registros históricos.
     * </p>
     * 
     * @param id Identificador único del cliente a anonimizar.
     * @throws RuntimeException Si ocurre algún error durante la operación de baja.
     * @author Álvaro Naranjo
     */
    public void darDeBajaCliente(Long id) {
        System.out.println("--> [CONTROLLER] Procesando baja para ID: " + id);
        try {
            personaService.darDeBajaCliente(id); 
        } catch (Exception e) {
            System.err.println("Error al dar de baja: " + e.getMessage());
            throw e; 
        }
    }
}
