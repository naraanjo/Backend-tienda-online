package com.ecommerce.naranjo.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.ecommerce.naranjo.controller.*;
import com.ecommerce.naranjo.dto.*;
import com.ecommerce.naranjo.model.EstadoCompra;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal de entrada para el PROYECTO.
 * 
 */
@SpringBootApplication(scanBasePackages = { "com.ecommerce.naranjo" })
@EnableJpaRepositories(basePackages = "com.ecommerce.naranjo.repository")
@EntityScan(basePackages = "com.ecommerce.naranjo.model")
public class App implements CommandLineRunner {

    // Inyección de dependencias desde la LIBRERÍA (JAR)
    @Autowired private PersonaController personaController;
    @Autowired private ArticuloController articuloController;
    @Autowired private CompraController compraController;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) {
        printHeader("INICIANDO SUITE DE PRUEBAS FINAL (CLIENTE INTEGRATION)");

        try {
            // ==========================================
            // BLOQUE 1: GESTIÓN DE ARTÍCULOS
            // ==========================================
            printSection("1. GESTIÓN DE ARTÍCULOS");

            // 1.1 Altas 
            Long idLaptop = gestionarAltaArticulo("Portátil Gaming", new BigDecimal("1500.00"), 10);
            Long idMouse = gestionarAltaArticulo("Ratón Inalámbrico", new BigDecimal("25.50"), 50);
            Long idTeclado = gestionarAltaArticulo("Teclado Mecánico", new BigDecimal("80.00"), 5); 

            // 1.2 Test Filtro Disponibles
            List<ArticuloDTO> disponibles = articuloController.listarDisponiblesParaVenta();
            System.out.println(" [INFO] Artículos disponibles: " + disponibles.size());

            // 1.3 VALIDACIÓN DE PRECIO NEGATIVO (Unhappy Path)
            System.out.println("\n [TEST] Intentando crear artículo con precio negativo...");
            try {
                ArticuloDTO malo = new ArticuloDTO();
                malo.setNombre("Articulo Error");
                malo.setPvpActual(new BigDecimal("-100.00")); 
                malo.setStock(10);
                articuloController.guardarArticulo(malo);
                System.err.println(" [ERROR] App permitió un precio negativo.");
            } catch (Exception e) {
                System.out.println(" [CHECK] VALIDACIÓN OK: App bloqueó el precio negativo -> " + e.getMessage());
            }

            // ==========================================
            // BLOQUE 2: GESTIÓN DE CLIENTES
            // ==========================================
            printSection("2. GESTIÓN DE CLIENTES");

            Long idCliente = gestionarAltaCliente("Carlos Cliente", "carlos@test.com", "11111111A");

            // 2.1 Test Unicidad Email
            try {
                crearDTOCliente("Impostor", "carlos@test.com", "00000000Z");
            } catch (Exception e) {
                System.out.println(" [CHECK] VALIDACIÓN OK: Bloqueo de email duplicado funcionando.");
            }

            // ==========================================
            // BLOQUE 3: GESTIÓN DE COMPRAS Y CÁLCULOS
            // ==========================================
            printSection("3. GESTIÓN DE COMPRAS Y CÁLCULOS");

            int cantidad = 2;
            garantizarStock(idLaptop, cantidad + 5);

            System.out.println("Creando pedido de " + cantidad + " portátiles...");

            CompraDTO carrito = new CompraDTO();
            carrito.setPersonaId(idCliente);
            carrito.setCalle("Calle Comercio 1");
            carrito.setCiudad("Madrid");
            carrito.setCodigoPostal("28001");
            carrito.setLineas(new ArrayList<>());

            LineaCompraDTO linea = new LineaCompraDTO();
            linea.setArticuloId(idLaptop);
            linea.setCantidad(cantidad);
            carrito.getLineas().add(linea);

            // 3.1 PROCESAR COMPRA
            CompraDTO ticket = compraController.procesarCompra(carrito);

            // 3.2 TEST DE CÁLCULO DE TOTALES
            BigDecimal precioUnitario = new BigDecimal("1500.00");
            BigDecimal totalEsperado = precioUnitario.multiply(new BigDecimal(cantidad));
            
            System.out.println(" [INFO] Total calculado por backend: " + ticket.getTotal() + " EUR");
            
         

            // 3.3 TEST SNAPSHOT
            System.out.println("\nSubiendo precio a 3000 EUR (Test Histórico)...");
            ArticuloDTO laptop = articuloController.obtenerPorId(idLaptop);
            laptop.setPvpActual(new BigDecimal("3000.00"));
            articuloController.guardarArticulo(laptop);

            CompraDTO historico = compraController.verHistorialCliente(idCliente).stream()
                    .filter(c -> c.getId().equals(ticket.getId())).findFirst().orElseThrow();

            if (historico.getTotal().compareTo(totalEsperado) == 0) {
                System.out.println(" [CHECK] SNAPSHOT OK: El precio histórico se mantuvo.");
            }

            // 3.4 TEST DATOS FISCALES
            PersonaDTO clienteRecuperado = personaController.obtenerTodosLosClientes().stream()
                    .filter(p -> p.getId().equals(idCliente)).findFirst().orElse(null);
            
            if(clienteRecuperado != null && "11111111A".equals(clienteRecuperado.getDatosFiscales().getNifCif())) {
                System.out.println(" [CHECK] PERSISTENCIA OK: Datos fiscales guardados correctamente.");
            }

            // 3.5 CANCELACIÓN
            System.out.println("\nCancelando pedido...");
            compraController.anularPedido(ticket.getId());

            // 3.6 TEST ESTADOS
            List<CompraDTO> canceladas = compraController.verPorEstado(EstadoCompra.CANCELADO);
            if (canceladas.stream().anyMatch(c -> c.getId().equals(ticket.getId()))) {
                System.out.println(" [CHECK] ESTADOS OK: Pedido aparece como CANCELADO.");
            }
            
         // ==========================================
         // BLOQUE EXTRA: VALIDACIONES DE STOCK Y ESTADOS
         // ==========================================
         printSection("3.5 (EXTRA) VALIDACIONES AVANZADAS");

         // TEST 1: Stock Insuficiente
         System.out.println(" [TEST] Intentando comprar más stock del disponible...");
         try {
             // El teclado tiene stock 5 (definido al inicio). Intentamos comprar 10.
             CompraDTO pedidoExcesivo = new CompraDTO();
             pedidoExcesivo.setPersonaId(idCliente);
             pedidoExcesivo.setCalle("Calle Falsa");
             pedidoExcesivo.setCiudad("Madrid");
             pedidoExcesivo.setCodigoPostal("28000");
             pedidoExcesivo.setLineas(new ArrayList<>());
             
             LineaCompraDTO linea = new LineaCompraDTO();
             linea.setArticuloId(idTeclado); // Stock actual: 5
             linea.setCantidad(10);          // Pedimos: 10
             pedidoExcesivo.getLineas().add(linea);
             
             compraController.procesarCompra(pedidoExcesivo);
             System.err.println(" [ERROR] El sistema permitió vender sin stock.");
         } catch (Exception e) {
             System.out.println(" [CHECK] STOCK OK: Bloqueo por stock insuficiente funcionando -> " + e.getMessage());
         }

         // TEST 2: Verificación de resta de stock
         ArticuloDTO laptopAntes = articuloController.obtenerPorId(idLaptop);
         // (Aquí asumes que sabes cuánto stock tenía antes de la compra del Bloque 3)
         // Simplemente imprimimos el actual para verificación visual
         System.out.println(" [INFO] Stock actual del Laptop tras la venta: " + laptopAntes.getStock());

            // ==========================================
            // BLOQUE 4: INTEGRIDAD Y LIMPIEZA
            // ==========================================
            printSection("4. INTEGRIDAD Y BAJAS");

            // 4.1 TEST INTEGRIDAD (Debe fallar)
            System.out.println(" [TEST] Intentando borrar artículo VENDIDO (Portátil)...");
            try {
                articuloController.eliminarArticulo(idLaptop);
                System.err.println(" [ERROR] Se borró un artículo con ventas. La integridad falló.");
            } catch (Exception e) {
                System.out.println(" [CHECK] INTEGRIDAD OK: NO SE PUEDE BORRAR UN ARTÍCULO VENDIDO -> " );
            }

            // 4.2 BORRADO EXITOSO (Debe funcionar)
            System.out.println("\n [TEST] Intentando borrar artículo SIN VENTAS (Teclado)...");
            try {
                articuloController.eliminarArticulo(idTeclado);
                boolean existe = articuloController.listarTodos().stream().anyMatch(a -> a.getId().equals(idTeclado));
                if (!existe) {
                    System.out.println(" [CHECK] BORRADO OK: El artículo sin ventas se eliminó correctamente.");
                } 
            } catch (Exception e) {
                System.err.println(" [ERROR] Falló el borrado legítimo: " + e.getMessage());
            }

            // 4.3 BAJA LÓGICA CLIENTE
            System.out.println("\nDando de baja al cliente de prueba...");
            personaController.darDeBajaCliente(idCliente);
            
            PersonaDTO anonimo = personaController.obtenerTodosLosClientes().stream()
                    .filter(p -> p.getId().equals(idCliente)).findFirst().orElse(null);
            if (anonimo != null && anonimo.getNombreCompleto().contains("ELIMINADO")) {
                 System.out.println(" [CHECK] ANONIMIZACIÓN OK: Cliente anonimizado.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        printHeader("FIN PREUBAS");
    }

    // ==========================================
    // HELPERS (Lógica de Cliente) - para pruebas
    // ==========================================

    private Long gestionarAltaArticulo(String nombre, BigDecimal precio, Integer stock) {
        List<ArticuloDTO> existentes = articuloController.buscarPorNombre(nombre);
        for(ArticuloDTO art : existentes) {
            if(art.getNombre().equalsIgnoreCase(nombre)) {
                if (art.getPvpActual().compareTo(precio) != 0 || !art.getStock().equals(stock)) {
                    art.setPvpActual(precio);
                    art.setStock(stock);
                    articuloController.guardarArticulo(art);
                }
                return art.getId();
            }
        }
        ArticuloDTO nuevo = new ArticuloDTO();
        nuevo.setNombre(nombre);
        nuevo.setDescripcion("Test Item");
        nuevo.setPvpActual(precio);
        nuevo.setStock(stock);
        return articuloController.guardarArticulo(nuevo).getId();
    }

    private Long gestionarAltaCliente(String nombre, String email, String nif) {
        try {
            return crearDTOCliente(nombre, email, nif);
        } catch (Exception e) {
            return personaController.obtenerTodosLosClientes().stream()
                    .filter(p -> p.getEmail().equals(email))
                    .findFirst().map(PersonaDTO::getId).orElseThrow();
        }
    }

    private Long crearDTOCliente(String nombre, String email, String nif) {
        PersonaDTO p = new PersonaDTO();
        p.setNombreCompleto(nombre);
        p.setEmail(email);
        DatosFiscalesDTO df = new DatosFiscalesDTO();
        df.setNifCif(nif);
        df.setCalle("Test Calle");
        df.setCiudad("Test Ciudad");
        df.setCodigoPostal("00000");
        df.setTelefono("000000000");
        p.setDatosFiscales(df);
        return personaController.crearCliente(p).getId();
    }

    private void garantizarStock(Long articuloId, int min) {
        ArticuloDTO a = articuloController.obtenerPorId(articuloId);
        if(a.getStock() < min) {
            a.setStock(min);
            articuloController.guardarArticulo(a);
        }
    }

    private void printHeader(String t) { System.out.println("\n=== " + t + " ==="); }
    private void printSection(String t) { System.out.println("\n--- " + t + " ---"); }
}