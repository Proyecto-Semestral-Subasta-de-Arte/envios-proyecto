# Microservicio de Logística y Envíos ('envíos')

## Integrantes
* **Gonzalo Hormazábal**
* **Geraldinne González**


## Descripción
Módulo encargado de la cadena de distribución, empaque especial de alta seguridad para obras de arte, asignación de transportistas especializados y seguimiento de rutas.

* **Puerto:** `8087`
* **Base de Datos:** `envios_db` (MySQL)


## Funcionalidades Clave
* Generación de órdenes de despacho y guías de seguimiento tracking.
* Manejo de excepciones personalizadas para estados de envíos no localizados.
* Clasificación interna de transportes certificados.


## Configuración (`application.properties`)
* server.port=8087
* spring.datasource.url=jdbc:mysql://localhost:3306/envios_db
* spring.datasource.username=root
* spring.datasource.password=
* spring.jpa.hibernate.ddl-auto=update
* logging.level.cl.sda1085.envios=DEBUG


## Pasos para Ejecutar

### 1. Preparación de la Base de Datos
Antes de ejecutar el servicio, crear la conexión a la base de datos de MySQL (XAMPP) corriendo en el puerto 3306 y con el nombre 'envios_db'.

### 2. Verificación de Credenciales
Revisar que el archivo application.properties tenga por defecto, usuario root y contraseña vacía.

### 3. Lanzamiento del Microservicio
Ejecutar (run) la clase principal con la anotación @SpringBootApplication (EnviosApplication.java).

### 4. Reglas de Seguridad
Al consumir los endpoints en Postman, ten en cuenta el comportamiento de la cadena de filtros de seguridad:

* Generación de Guías: El registro inicial de una orden de despacho o envío requiere una autenticación válida que certifique que la subasta o transacción de origen ha sido pagada y cerrada con éxito.
* Integridad del Destino: Al realizar las peticiones de envío mediante JSON, asegúrate de proveer direcciones válidas e identificadores de usuario (idUsuario) reales para garantizar el correcto mapeo geográfico y de entrega en el flujo distribuido.
