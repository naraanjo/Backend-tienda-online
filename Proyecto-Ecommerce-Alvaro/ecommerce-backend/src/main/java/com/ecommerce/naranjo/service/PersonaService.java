package com.ecommerce.naranjo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.naranjo.dto.PersonaDTO;
import com.ecommerce.naranjo.model.DatosFiscales;
import com.ecommerce.naranjo.model.Persona;
import com.ecommerce.naranjo.repository.PersonaRepository;
import com.ecommerce.naranjo.utils.DatosFiscalesMapper;
import com.ecommerce.naranjo.utils.PersonaMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de lógica de negocio para la gestión de {@link Persona}.
 * * <p>
 * Se encarga de aplicar las reglas de negocio asociadas, como la unicidad del
 * email, la persistencia transaccional de personas y sus datos fiscales, y la
 * conversión entre entidades y DTOs mediante mappers.
 * </p>
 * * <p>
 * Utiliza {@link PersonaRepository} para operaciones CRUD y los mappers
 * {@link PersonaMapper} y {@link DatosFiscalesMapper} para la conversión DTO ↔
 * Entidad.
 * </p>
 * * <p>
 * La relación bidireccional entre {@link Persona} y {@link DatosFiscales} se
 * gestiona manualmente al crear personas para asegurar la integridad de la
 * clave foránea y la consistencia de los datos.
 * </p>
 * * <p>
 * Todos los métodos de escritura son transaccionales, asegurando que cualquier
 * error provoque rollback automático.
 * </p>
 * * @author Álvaro Naranjo
 * @version 1.0
 * @since 2026-01-04
 */
@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private PersonaMapper personaMapper;

    @Autowired
    private DatosFiscalesMapper datosFiscalesMapper;

    /**
     * <p>
     * Reglas de negocio aplicadas:
     * </p>
     * <ul>
     * <li>El email de la persona debe ser único.</li>
     * <li>Si se proporcionan datos fiscales, se vinculan bidireccionalmente
     * con la persona.</li>
     * </ul>
     * 
     * * <p>
     * La conversión de {@link PersonaDTO} a {@link Persona} se realiza mediante
     * {@link PersonaMapper}, y los {@link DatosFiscales} se mapean mediante
     * {@link DatosFiscalesMapper}.
     * </p>
     * * @param personaDTO DTO con los datos de la persona a registrar.
     * @return DTO de la persona registrada, incluyendo el ID generado y los
     * datos fiscales asociados.
     * @throws IllegalArgumentException si el email ya está registrado.
     * @author Álvaro Naranjo
     */
    @Transactional
    public PersonaDTO crearPersona(PersonaDTO personaDTO) {

        if (personaRepository.existsByEmail(personaDTO.getEmail())) {
            throw new IllegalArgumentException("El email " + personaDTO.getEmail() + " ya está registrado.");
        }

        // Conversión de DTO a entidad
        Persona personaEntity = personaMapper.toEntity(personaDTO);

        // Gestión manual de la relación bidireccional con DatosFiscales
        if (personaDTO.getDatosFiscales() != null) {
            DatosFiscales fiscalesEntity = datosFiscalesMapper.toEntity(personaDTO.getDatosFiscales());
            personaEntity.setDatosFiscales(fiscalesEntity);
            fiscalesEntity.setPersona(personaEntity); // Vinculación de la FK
        }

        // Persistencia en cascada
        Persona personaGuardada = personaRepository.save(personaEntity);

        // Respuesta con la persona insertada pero en formato DTO
        return personaMapper.toDTO(personaGuardada);
    }

    /**
     * Obtiene el listado completo de personas registradas en el sistema.
     * * <p>
     * Los datos se devuelven como una lista de {@link PersonaDTO}, incluyendo
     * sus datos fiscales anidados.
     * </p>
     * * @return Lista de {@link PersonaDTO} con todas las personas.
     * @author Álvaro Naranjo
     */
    @Transactional(readOnly = true)
    public List<PersonaDTO> listarTodas() {
        return personaRepository.findAll()
                .stream()
                .map(personaMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca una persona por su identificador único.
     * * @param id Identificador de la persona.
     * @return DTO de la persona encontrada, incluyendo datos fiscales.
     * @throws RuntimeException si no se encuentra ninguna persona con el ID
     * proporcionado.
     * @author Álvaro Naranjo
     */
    @Transactional(readOnly = true)
    public PersonaDTO buscarPorId(Long id) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con ID: " + id));
        return personaMapper.toDTO(persona);
    }

    /**
     * Realiza una baja lógica (anonimización) del cliente.
     * * <p>
     * MOTIVO: No podemos eliminar físicamente la persona de la base de datos porque podría
     * tener compras asociadas y esto violaría restricciones de integridad referencial
     * o se perdería el historial de transacciones.
     * </p>
     * * <p>
     * SOLUCIÓN: Se sobrescriben los datos personales y fiscales con valores genéricos
     * para mantener el registro en la base de datos sin exponer información sensible.
     * </p>
     * * <p>
     * Reglas aplicadas durante la anonimización:
     * </p>
     * <ul>
     * <li>Se reemplaza el nombre completo por "USUARIO_ELIMINADO_{ID}" para conservar un identificador interno.</li>
     * <li>El email se reemplaza por "deleted_{ID}@ecommerce.local" para cumplir con la restricción de unicidad.</li>
     * <li>Los datos fiscales, si existen, se rellenan con valores genéricos: NIF/CIF, calle, ciudad, código postal y teléfono.</li>
     * </ul>
     * 
     * * <p>
     * La operación es transaccional: todos los cambios se aplican de manera atómica,
     * y JPA detecta automáticamente los cambios en la entidad Persona y sus DatosFiscales
     * relacionados para realizar los UPDATE necesarios.
     * </p>
     * * @param id Identificador único de la persona a anonimizar.
     * @throws RuntimeException si no se encuentra ninguna persona con el ID proporcionado.
     * @author Álvaro Naranjo
     */
    @Transactional
    public void darDeBajaCliente(Long id) {
        //  Buscar la persona
        Persona persona = personaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se puede dar de baja. Cliente no encontrado con ID: " + id));

        //  Anonimizar datos de la PERSONA
        persona.setNombreCompleto("USUARIO_ELIMINADO_" + id); 
        persona.setEmail("deleted_" + id + "@ecommerce.local"); 

        // Anonimizar DATOS FISCALES (si existen)
        // No uso ("") para evitar posibles errores en oracle por campos NULL = ("")
        if (persona.getDatosFiscales() != null) {
            DatosFiscales df = persona.getDatosFiscales();
            df.setNifCif("ANONIMO"); 
            df.setCalle("DIRECCION_BORRADA");
            df.setCiudad("BORRADO");
            df.setCodigoPostal("00000");
            df.setTelefono("000000000");
        }

        // Guardar los cambios
        personaRepository.save(persona);

        System.out.println("--> [SERVICE] Cliente " + id + " anonimizado correctamente.");
    }
}