package com.ecommerce.naranjo.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class LineaCompraDTO {

    private Long id;
    
    // Datos del Artículo (Para no tener que cargar el objeto Articulo completo)
    private Long articuloId;
    // Nombre del artículo en el momento de la compra (snapshot), evito realizar una consulta extra
    // Para saber el nombre del articulo que se compro
    private String articuloNombre; 
    
    private Integer cantidad;
    
    // El precio histórico (snapshot)
    private BigDecimal precioUnitario;
    
    // Campo calculado para facilitar la vida al Frontend (Precio * Cantidad)
    private BigDecimal subtotal;
}