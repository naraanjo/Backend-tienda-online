# Backend Ecommerce - Spring Boot

Este proyecto implementa el backend de una **tienda ecommerce online** utilizando **Spring Boot**, **JPA (Jakarta Persistence API)** y **Docker** con **Oracle Database**. Su objetivo es gestionar usuarios, compras, artículos y la información fiscal asociada, siguiendo buenas prácticas de **arquitectura limpia** y diseño orientado a dominios.

---

## Tecnologías utilizadas

- **Java**  
- **Spring Boot**  
- **Oracle Database** (imagen oficial en Docker)  
- **Maven** para gestión de dependencias  

---

## Funcionalidades principales

### Gestión de usuarios registrados
- Cada persona tiene un identificador único, nombre completo, correo electrónico único y fecha de registro.  
- Validación de correo electrónico único para evitar duplicados.  
- Implementación de **soft delete** para no eliminar físicamente los registros.  

### Información fiscal
- Cada usuario tiene asociada una información fiscal única (NIF/CIF, dirección y teléfono).  
- Relación **uno a uno**: si se elimina un usuario, sus datos fiscales se eliminan automáticamente (**cascade**).  

### Gestión de compras
- Un usuario puede realizar múltiples compras a lo largo del tiempo.  
- Cada compra incluye: fecha, estado (`PENDIENTE`, `ENVIADO`, `ENTREGADO`) y dirección de envío.  
- Relación **uno a muchos** entre usuarios y compras.  

### Gestión de catálogo de artículos
- Cada artículo tiene nombre, descripción, precio actual y stock disponible.  
- Un mismo artículo puede aparecer en muchas compras diferentes.  

### Detalle de compras
- Relación **muchos a muchos** entre compras y artículos con tabla intermedia (`CompraArticulo`).  
- Registro de la **cantidad de unidades** y **precio unitario en el momento de la compra** para congelar precios históricos.  


---

## Base de datos
- **Oracle Database** desplegada mediante Docker.  
- Archivo de ejemplo para **Docker Compose** incluido (`docker-compose.yml`).  
- Tablas generadas automáticamente a partir de las entidades JPA.  

---

## Cómo ejecutar el proyecto
# Levantar Oracle en Docker
docker-compose up -d

# Instalar dependencias y generar la librería del backend
mvn install

# Configurar la conexión a Oracle en application.properties 

# Ejecutar la aplicación
mvn spring-boot:run
