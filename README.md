# Order Service

Servicio REST para la gestión de pedidos, desarrollado como parte de una prueba técnica para el rol de Líder Técnico.

La solución implementa un servicio de pedidos utilizando Spring Boot, Clean Architecture, principios SOLID, PostgreSQL, JPA/Hibernate, Flyway, pruebas automatizadas y un frontend sencillo basado en Bootstrap.
---

## 1. Descripción

El sistema permite:

- Crear pedidos.
- Consultar pedidos por ID.
- Confirmar pedidos.
- Cancelar pedidos.
- Calcular automáticamente el total del pedido.
- Validar la existencia de clientes y productos mediante puertos de aplicación.
- Persistir la información en PostgreSQL.
- Manejar errores de negocio, validación y recursos no encontrados.
- Ejecutar pruebas automatizadas.
- Ejecutar PostgreSQL mediante Docker Compose.

---

## 2. Arquitectura

La aplicación está estructurada utilizando principios de Clean Architecture, separando las responsabilidades del dominio, aplicación, infraestructura y exposición HTTP.

                         ┌──────────────────────┐
                         │      Frontend        │
                         │ Bootstrap + JS       │
                         └──────────┬───────────┘
                                    │ HTTP/REST
                                    ▼
                         ┌──────────────────────┐
                         │   Web / Controller  │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │ Application /        │
                         │ Use Cases             │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │       Domain         │
                         │ Order / OrderItem    │
                         │ Business Rules       │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │ Repository Port      │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │ Infrastructure       │
                         │ JPA / PostgreSQL     │
                         └──────────────────────┘

### Principios aplicados

- Separación de responsabilidades.
- Inversión de dependencias.
- Encapsulamiento de reglas de negocio.
- Dependency Inversion Principle.
- Ports and Adapters.
- Separación entre modelo de dominio y entidades de persistencia.
- Bajo acoplamiento entre dominio e infraestructura.
---

## 3. Tecnologías

### Backend
- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Hibernate
- PostgreSQL
- Flyway
- Maven
- JUnit
- Mockito

### Frontend
- HTML5
- CSS3
- JavaScript
- Bootstrap 5

### Infraestructura
- Docker
- Docker Compose
- PostgreSQL 16

### Control de versiones
- Git
---

## 4. Estructura del proyecto

order-service/
│
├── database/
│   ├── 01_schema.sql
│   ├── 02_indexes.sql
│   └── 03_procedures.sql
│
├── frontend/
│   ├── index.html
│   ├── css/
│   │   └── styles.css
│   └── js/
│       └── app.js
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/christiancriollo/orders/
│   │   │       ├── domain/
│   │   │       ├── application/
│   │   │       └── infrastructure/
│   │   │
│   │   └── resources/
│   │       ├── application.yaml
│   │       └── db/
│   │           └── migration/
│   │
│   └── test/
│       └── java/
│
├── docker-compose.yml
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
---

## 5. Dominio

El dominio principal está compuesto por `Order`, `OrderItem` y `OrderStatus`.

### Order

Representa un pedido y contiene:
- Identificador.
- Identificador del cliente.
- Items.
- Estado.
- Cálculo del total.
- Reglas para confirmar.
- Reglas para cancelar.

### OrderItem

Representa un producto dentro de un pedido:

- Identificador del producto.
- Nombre del producto.
- Precio unitario.
- Cantidad.
- Cálculo del subtotal.

### Estados
PENDING
CONFIRMED
CANCELLED
---

## 6. Reglas de negocio

### Creación

Un pedido debe:
- Tener un ID.
- Tener un cliente.
- Contener al menos un item.

Cada item debe:
- Tener un producto.
- Tener nombre.
- Tener precio válido.
- Tener cantidad mayor que cero.

### Confirmación

Una orden puede pasar de:
PENDING → CONFIRMED
Una orden `CONFIRMED` no puede confirmarse nuevamente.
Una orden `CANCELLED` no puede confirmarse.

### Cancelación
Una orden puede pasar de:
PENDING → CANCELLED
Una orden `CONFIRMED` no puede cancelarse.
Una orden `CANCELLED` no puede cancelarse nuevamente.
---

## 7. API REST

La API está disponible por defecto en:
http://localhost:8080/orders

### Crear pedido
POST /orders
Content-Type: application/json
Ejemplo:

{
  "customerId": "11111111-1111-1111-1111-111111111111",
  "items": [
    {
      "productId": "22222222-2222-2222-2222-222222222222",
      "productName": "Producto de prueba",
      "unitPrice": 35000,
      "quantity": 1
    }
  ]
}

Respuesta:

{
  "id": "73adcb4b-c3b1-4c0a-b7f0-4a284824c89b",
  "customerId": "11111111-1111-1111-1111-111111111111",
  "total": 35000.00,
  "status": "PENDING"
}
---

### Consultar pedido

GET /orders/{id}

Ejemplo:
GET /orders/73adcb4b-c3b1-4c0a-b7f0-4a284824c89b
---

### Confirmar pedido
POST /orders/{id}/confirm

Respuesta:
{
  "id": "73adcb4b-c3b1-4c0a-b7f0-4a284824c89b",
  "customerId": "11111111-1111-1111-1111-111111111111",
  "total": 35000.0000,
  "status": "CONFIRMED"
}
---

### Cancelar pedido
POST /orders/{id}/cancel
Respuesta:

{
  "id": "3900515e-07f1-4299-880a-d09bd7f89bf8",
  "customerId": "11111111-1111-1111-1111-111111111111",
  "total": 25000.0000,
  "status": "CANCELLED"
}
---

## 8. Manejo de excepciones

La aplicación centraliza el manejo de excepciones mediante `@RestControllerAdvice`.

### Errores de negocio

HTTP:
400 Bad Request

Ejemplo:

{
  "error": "Una orden confirmada no puede ser cancelada."
}

### Recursos no encontrados

HTTP:
404 Not Found

Ejemplo:

{
  "error": "Pedido no encontrado: aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"
}

### Errores de validación

HTTP:
400 Bad Request

Ejemplo:

{
  "details": {
    "items": "La orden debe contener al menos un item"
  },
  "error": "Error de validación"
}

### Errores inesperados

HTTP:
500 Internal Server Error
La respuesta no expone detalles internos de la aplicación.
---

## 9. Persistencia

La aplicación utiliza:

- PostgreSQL.
- Spring Data JPA.
- Hibernate.
- Flyway.

El modelo de dominio está separado de las entidades JPA mediante entidades específicas de infraestructura y un mapper de persistencia.
Esto evita acoplar directamente el dominio con JPA/Hibernate.

### Componentes principales

Domain Model
      │
      ▼
OrderRepository
      │
      ▼
JpaOrderRepositoryAdapter
      │
      ▼
JpaOrderRepository
      │
      ▼
PostgreSQL
---

## 10. Migraciones de base de datos

Las migraciones son administradas por Flyway se encuentran en:

src/main/resources/db/migration/
Actualmente se dispone de:
V1__create_orders_schema.sql

Adicionalmente, el proyecto contiene scripts SQL en:

database/
├── 01_schema.sql
├── 02_indexes.sql
└── 03_procedures.sql

Estos scripts permiten documentar:

- Esquema de base de datos.
- Índices.
- Procedimientos almacenados.

### Estrategia

Flyway es el mecanismo utilizado por la aplicación para administrar las migraciones durante el arranque.
Los scripts ubicados en `database/` funcionan como scripts SQL complementarios y de referencia para la base de datos.
---

## 11. Docker Compose

PostgreSQL puede ejecutarse mediante:
docker compose up -d

Verificar el estado:
docker compose ps

El contenedor PostgreSQL utiliza:
Database: orders_db
User: orders_user
Port: 5432

El servicio cuenta con un healthcheck utilizando `pg_isready`.
Para detener los servicios:
docker compose down

Para detenerlos y eliminar el volumen:
docker compose down -v

 `docker compose down -v` elimina los datos persistidos de PostgreSQL.
---

## 12. Ejecución del backend

Desde la raíz del proyecto:
order-service/

### Windows
Ejecutar las pruebas:
.\mvnw.cmd clean test

Para ejecutar la aplicación:
.\mvnw.cmd spring-boot:run

El backend quedará disponible en:
http://localhost:8080

### Nota
El proyecto utiliza Maven Wrapper, por lo que no es necesario tener Maven instalado globalmente.
---

## 13. Ejecución del frontend

El frontend se encuentra en:
frontend/

Está compuesto por:

frontend/
├── index.html
├── css/
│   └── styles.css
└── js/
    └── app.js

Puede ejecutarse utilizando cualquier servidor HTTP estático.
Por ejemplo, con Node.js:
cd frontend
npx serve .
Por defecto estará disponible en:
http://localhost:3000

El frontend consume la API REST del backend:
http://localhost:8080/orders
---

## 14. Pruebas automatizadas

La aplicación cuenta con pruebas unitarias para el dominio y casos de uso.

Ejecutar:
.\mvnw.cmd clean test

Resultado validado:
Tests run: 15
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS

Las pruebas cubren principalmente:
- Creación de pedidos.
- Validaciones.
- Cálculo de totales.
- Confirmación.
- Cancelación.
- Reglas de transición de estados.
- Comportamiento de casos de uso.
---

## 15. Validación funcional

La aplicación fue validada desde el frontend realizando operaciones de:
- Creación de pedidos.
- Consulta mediante `GET`.
- Confirmación.
- Cancelación.
- Validación de estados.
- Validación de errores de negocio.
- Validación de pedidos inexistentes.

También se verificó la persistencia de los pedidos en PostgreSQL.

### Ejemplos validados

#### Pedido pendiente
{
  "id": "2acd213b-0bf4-4379-a78d-0604a4706251",
  "customerId": "11111111-1111-1111-1111-111111111111",
  "total": 35000.00,
  "status": "PENDING"
}

#### Pedido confirmado

{
  "id": "2acd213b-0bf4-4379-a78d-0604a4706251",
  "customerId": "11111111-1111-1111-1111-111111111111",
  "total": 35000.0000,
  "status": "CONFIRMED"
}

#### Pedido cancelado

{
  "id": "3900515e-07f1-4299-880a-d09bd7f89bf8",
  "customerId": "11111111-1111-1111-1111-111111111111",
  "total": 25000.0000,
  "status": "CANCELLED"
}

#### Regla de negocio validada

Una orden confirmada no puede ser cancelada:

{
  "error": "Una orden confirmada no puede ser cancelada."
}

Una orden cancelada no puede ser confirmada:

{
  "error": "Una orden cancelada no puede ser confirmada."
}
---

## 16. Decisiones técnicas

### Clean Architecture

Se separó el dominio de los mecanismos externos como:
- Spring.
- JPA.
- PostgreSQL.
- HTTP.

Esto permite evolucionar la infraestructura sin modificar las reglas principales del dominio.

### Ports and Adapters

La interacción con servicios externos se realiza mediante interfaces.
Por ejemplo:
CustomerServicePort
ProductServicePort
Esto permite reemplazar posteriormente los adapters actuales por integraciones reales con otros servicios.

### Persistencia
Se utiliza JPA/Hibernate para la persistencia del servicio, manteniendo las entidades de persistencia separadas del modelo de dominio.

### Validaciones
Las reglas críticas se encuentran dentro del dominio y no dependen exclusivamente del controlador.

### Repositorios
La aplicación mantiene una abstracción de repositorio en el dominio y utiliza un adapter de infraestructura para implementar la persistencia mediante Spring Data JPA.
---

## 17. Posibles evoluciones

En un escenario productivo se podrían incorporar:
- Autenticación y autorización mediante OAuth2/JWT.
- API Gateway.
- Integración real con servicios de clientes y productos.
- Resiliencia con circuit breaker, retry y timeout.
- Observabilidad con métricas, logs y tracing distribuido.
- CI/CD mediante Azure DevOps.
- Contenerización completa del backend.
- Gestión segura de secretos.
- OpenAPI/Swagger.
- Tests de integración y pruebas end-to-end automatizadas.
- Versionamiento de API.
- Idempotencia para operaciones críticas.
- Mensajería asíncrona cuando el dominio lo requiera.
---

## 18. Objetivo de la solución

La solución busca demostrar la capacidad para diseñar y desarrollar un servicio mantenible, aplicando:

- Principios de arquitectura limpia.
- Principios SOLID.
- Separación de responsabilidades.
- Ports and Adapters.
- Persistencia desacoplada del dominio.
- Buenas prácticas de desarrollo.
- Manejo centralizado de errores.
- Pruebas automatizadas.
- Docker.
- APIs REST.
- Desarrollo de un frontend funcional.

El diseño permite evolucionar posteriormente la solución hacia un ecosistema de microservicios manteniendo aisladas las reglas de negocio y reduciendo el acoplamiento con la infraestructura.
---

## 19. Autor

**Christian Criollo**
Prueba técnica — Líder Técnico
