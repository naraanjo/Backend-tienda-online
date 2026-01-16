package com.ecommerce.naranjo.dto;

import com.ecommerce.naranjo.model.EstadoCompra;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CompraDTO {

    private Long id;
    
    private Long personaId;
    
    // Nombre de la persona, para evitar una consulta adicional de quien hizo la compra
    private String personaNombre; 
    
    private LocalDateTime fechaCompra;
    private EstadoCompra estado;
    
    // Direccion completa de envio
    private String calle;
    private String ciudad;
    private String codigoPostal;
    
    private BigDecimal total;
    
    private List<LineaCompraDTO> lineas = new ArrayList<>();
}