# Prueba Técnica - Java Backend Developer Ssr

Este repositorio contiene la prueba técnica realizada para el puesto de **Java Backend Developer Ssr**. A continuación se detallan los pasos para ejecutar el proyecto, los requerimientos técnicos y una explicación de las decisiones técnicas tomadas durante el desarrollo.

## 1. Pasos para ejecutar el proyecto

### Requisitos previos
Asegúrate de tener instalados los siguientes programas:
- [Docker Desktop](https://www.docker.com/get-started)
- [Git](https://git-scm.com/)

#### 1. Clonar el repositorio

```bash
git clone https://github.com/MerelesLautaro/Technical-test-SSR.git
```

#### 2. Abrir una terminal en la misma raiz que el archivo docker-compose.yml
```bash
cd technical-test
```

#### 3. Ejecutar  el contenedor de Docker
```bash
docker-compose up
```
Este comando descargará las imágenes necesarias y pondrá en marcha el contenedor. Una vez que el contenedor esté corriendo, puedes acceder al servicio en http://localhost:8082. También se puede acceder a la documentación en http://localhost:8082/swagger-ui/index.html (agregando /v3/api-docs en la barra de busqueda de Swagger) accediendo desde un navegador (o desde Postman si asi se desea).

## 2. Requerimientos técnicos

Como parte de nuestro proceso de selección, queremos evaluar tus habilidades en el desarrollo de APIs RESTful con Spring Boot, aplicando arquitecturas escalables, buenas prácticas y patrones de diseño.

A continuación, te detallamos la prueba técnica que debes completar en un máximo de 72 horas.

### Objetivo
Desarrollar una API REST para la gestión de usuarios con **Spring Boot**, aplicando buenas prácticas de desarrollo, seguridad y optimización del código.

### Requerimientos

#### 1. Diseño y Desarrollo de la API REST
- **GET /users**: Listar todos los usuarios con paginación y filtros.
- **GET /users/{id}**: Obtener usuario por ID.
- **POST /users**: Crear un usuario.
- **PUT /users/{id}**: Actualizar un usuario.
- **DELETE /users/{id}**: Eliminar un usuario (Soft Delete).

#### 2. Implementación de Arquitectura y Buenas Prácticas
- Uso del patrón **DTO** para la transferencia de datos.
- Implementación del patrón **Service Layer** para separar la lógica de negocio.
- Implementación del **Manejo Global de Excepciones** con `@RestControllerAdvice`.
- Uso de **Spring Security** con **JWT** para autenticación y autorización.

#### 3. Persistencia y Optimización
- Base de datos **MySQL** o **PostgreSQL** (dockerizada preferentemente).
- Uso de **Spring Data JPA** con consultas personalizadas en el `UserRepository`.
- Paginación y filtros en la consulta de usuarios (**GET /users**).
- Implementación de **caché** en consultas frecuentes utilizando **Spring Cache** (opcional).

#### 4. Documentación y Testing
- **Documentación** con **Swagger/OpenAPI**.
- **Pruebas unitarias** con **JUnit** y **Mockito** (mínimo 3 pruebas).
- **Pruebas de integración** con **Testcontainers** (opcional, pero valorado).

#### 5. Despliegue y Entorno de Desarrollo
- Creación de un **Dockerfile** para contenedorización.
- Uso de **docker-compose** para levantar la base de datos y la API.
- Uso de **GitHub Actions** o **GitLab CI/CD** para integración continua (opcional).

### Criterios de evaluación
1. Código limpio y modular (**principios SOLID**, separación de responsabilidades).
2. Uso eficiente de **JPA** y **DTOs**.
3. Correcta implementación de **seguridad con JWT**.
4. Optimización de consultas SQL y manejo de **caché**.
5. Testing unitario y de integración.
6. Correcta documentación y despliegue del proyecto.

### Entrega del Proyecto
Sube el código a un repositorio público en **GitHub/GitLab** e incluye un `README.md` con:

- Instrucciones para ejecutar la API (Docker o manualmente).
- Descripción de las decisiones técnicas tomadas.
- Explicación de la arquitectura utilizada.

## 3. Decisiones técnicas


### Arquitectura Utilizada

El proyecto sigue una arquitectura **modular y limpia** basada en la separación de responsabilidades. Esto facilita la escalabilidad, el mantenimiento y las pruebas. La estructura de directorios es la siguiente:

```bash
technical-test/
├── docker-compose.yml
├── Dockerfile
├── mvnw
├── mvnw.cmd
├── pom.xml
├── README.md
└── src
    └── main
       ├── java
       │   └── com.lautadev.technical_test
       │               ├── TechnicalTestApplication.java
       │               ├── Config
       │               ├── Controller
       │               ├── DTO
       │               ├── Entities
       │               ├── Exception
       │               ├── Repository
       │               ├── Security
       │               ├── Services
       │               └── Util
       └── resources
           └── application.properties
```

### Convenciones de Nombres para las URLs

Para organizar mejor la estructura de las URLs y asegurar la claridad en la API, decidí añadir un prefijo de versión y un prefijo relacionado con la autenticación al inicio de las URLs requeridas en las especificaciones técnicas.

- **`/api/v1/authentication`**: Este prefijo se utiliza para el endpoint `POST /users`, que permite registrar un usuario. Al agregar el prefijo `/authentication`, se hace más evidente que este endpoint está relacionado con el proceso de autenticación y registro de usuarios.

- **`/api/v1/users`**: El resto de los endpoints relacionados con la gestión de usuarios, como `GET /users`, `PUT /users/{id}`, `DELETE /users/{id}`, y otros, tienen el prefijo `/users`, lo que deja claro que estos endpoints están orientados a la manipulación de los datos de los usuarios.

El uso de `/api/v1/` como prefijo también facilita la gestión de versiones de la API, permitiendo en el futuro una evolución de la misma sin afectar a los usuarios actuales.

### Medidas de Seguridad

Para garantizar la seguridad de los usuarios y proteger las rutas de la API, se utilizó **Spring Security** para implementar un sistema de autenticación y autorización basado en **JSON Web Tokens (JWT)**.

#### Proceso de Autenticación

Cuando un usuario se registra correctamente, recibe dos tokens: un **access token** y un **refresh token**. El **access token** es utilizado para autenticar las solicitudes en las rutas protegidas de la API, mientras que el **refresh token** se utiliza para obtener un nuevo **access token** cuando este ha expirado, sin necesidad de que el usuario vuelva a iniciar sesión.

- **Access Token**: Es un token corto con un tiempo de expiración limitado (en este caso, 1 día). Este token es necesario para acceder a las rutas protegidas de la API.
  
  Ejemplo de respuesta al registrarse:
  
  ```json
  {
    "accessToken": "......",
    "refreshToken: "......."
  }
    ```

### Validaciones Básicas de Contraseña

Aunque el proyecto no especifica requerimientos particulares para las contraseñas, decidí agregar una validación básica para garantizar la seguridad de las mismas. La validación personalizada asegura que las contraseñas cumplan con ciertos criterios esenciales, como la longitud mínima y máxima, y la restricción de caracteres en blanco.

La validación se implementa mediante una anotación personalizada `@Password` que se asocia con el campo de contraseña y utiliza un validador específico para verificar las reglas.

```bash
└── Util
    └──Password
    └──PasswordValidator
```

### ¿Por qué no dejar que la contraseña sea editable?

En el endpoint `PUT /users/{id}`, se implementó un DTO para pasar los campos a editar. Sin embargo, la contraseña no está incluida en este DTO, ya que considero que permitir la edición directa de la contraseña a través de este endpoint sería una práctica poco segura. 

Cambiar la contraseña de un usuario directamente en este tipo de solicitudes podría abrir la puerta a vulnerabilidades, como ataques de tipo **man-in-the-middle** o exposición accidental de datos sensibles.

En su lugar, prefiero implementar prácticas más seguras para el cambio de contraseñas. Sugiero el uso de un **OTP (One-Time Password)** para verificar la identidad del usuario antes de permitirle cambiar su contraseña. Esta es una medida adicional de seguridad que garantiza que el usuario está autenticado de manera adecuada antes de realizar un cambio tan crítico. 

Además, para aumentar la seguridad, se invalida el **access token** una vez que el cambio de contraseña es exitoso, y el token se agrega a la **blacklist** para evitar que sea utilizado de forma maliciosa.

Este enfoque asegura que el cambio de contraseña se realice de manera controlada y segura, protegiendo la integridad de la cuenta del usuario.

### Uso de Spring Cache y Caffeine

Para mejorar el rendimiento de la API y reducir la carga en el servidor, decidí implementar un mecanismo de **caching** en el endpoint `GET /users`, utilizando **Spring Cache** junto con **Caffeine** como proveedor de caché. Este enfoque es especialmente útil para casos en los que los datos no cambian con frecuencia, como la lista de usuarios, y puede mejorar significativamente los tiempos de respuesta al evitar consultas repetidas a la base de datos.

#### ¿Por qué cachear el endpoint `GET /users`?

El endpoint `GET /users` devuelve una lista de usuarios con soporte de paginación y filtros. Decidí cachear este endpoint debido a que la información que devuelve es estática en comparación con otros endpoints que modifican datos (por ejemplo, `POST /users`, `PUT /users`, `DELETE /users`). Los cambios realizados en los usuarios, como actualizaciones o eliminaciones, se gestionan en otros endpoints, lo que permite mantener el cache de `GET /users` coherente sin impactar su rendimiento.

#### Implementación de Caché

La implementación del caching se realiza en el servicio correspondiente utilizando la anotación `@Cacheable`. La clave de cache se genera en función de los filtros y la paginación utilizados, lo que asegura que las consultas con diferentes combinaciones de parámetros sean almacenadas de manera independiente:

```java
@Override
@Cacheable(value = "users", key = "#filters.toString() + #pageable.pageNumber + #pageable.pageSize")
public Page<UserDetailsResponse> getUsers(Map<String, String> filters, Pageable pageable) {
    
}
```

#### Configuración de Caché con Caffeine

Utilicé Caffeine, una biblioteca de caché de alto rendimiento, para gestionar el cache. La configuración establece un Time-to-Live (TTL) de 5 minutos para cada entrada de caché, lo que asegura que los datos no permanezcan en caché indefinidamente. Además, se establece un tamaño máximo de 1000 entradas en el caché para evitar que crezca de manera indefinida.
```bash
└── Config
    └──CacheConfig
```

#### ¿Por qué no usar CacheEvict con una clave allUsers?

Opté por no usar `@CacheEvict` con una clave global como `allUsers` para invalidar todo el caché, ya que esto podría tener un impacto negativo en el rendimiento si el número de usuarios crece considerablemente. Al invalidar todo el caché, estaríamos obligando a reconstruir la lista completa de usuarios en cada solicitud, lo cual podría generar una sobrecarga en el servidor, especialmente cuando la cantidad de usuarios es grande.

En su lugar, al cachear las respuestas de `GET /users` de forma granular (usando los filtros y la paginación como claves), la memoria del servidor se usa de manera más eficiente y se evita un impacto en el rendimiento innecesario.

### Uso de Criteria API

Para implementar una forma flexible y eficiente de filtrar los usuarios en el endpoint `GET /users`, utilicé **Criteria API** de JPA. Esta API permite construir consultas dinámicas de manera programática, lo que es útil cuando se requieren filtros personalizados que pueden variar según los parámetros de la solicitud.

#### Ventajas de Criteria API
 - Flexibilidad: Criteria API permite crear consultas dinámicas que pueden adaptarse a diferentes filtros.
- Mantenibilidad: Al usar las especificaciones y Criteria API, el código se mantiene limpio y modular, separando la lógica de la construcción de la consulta.
- Optimización: Las consultas se construyen y ejecutan de manera eficiente, aprovechando el poder de JPA y Criteria API para manejar grandes volúmenes de datos.

#### Implementación de la Criteria API

La lógica de los filtros se implementa a través de la **Specification** de JPA, que permite construir consultas basadas en condiciones dinámicas. En este caso, se pasa un **Map** con los filtros deseados a través del endpoint `GET /users`, como por ejemplo: `/users?name=Lautaro`.

A continuación se explica cómo se implementa la filtración de usuarios usando la Criteria API:

1. **UserRepository**: La interfaz del repositorio extiende `JpaSpecificationExecutor<User>`, lo que permite ejecutar las especificaciones dinámicas con facilidad:

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    ...
}
```

2. **UserSpecification**: La especificación construye la consulta utilizando los filtros pasados en el Map. La clase `UserSpecification` implementa la interfaz Specification<User> y genera una Predicate que será utilizada por el repositorio.
```bash
└── Repository
    └──specifications
    |    └──UserSpecification.java
    └──UserRepository.java
```

3. **FilterValidator**: La clase `FilterValidator` se encarga de recorrer los filtros y construir los Predicates correspondientes para cada filtro aplicado. En este caso, los filtros pueden ser name, email, y isDeleted, que son verificados en la consulta.
```bash
└── Util
    └──FilterValidator.java
```

#### ¿Cómo funciona?
Cuando el cliente realiza una solicitud a GET /users con filtros, como por ejemplo:
```bash
GET /users?name=Lautaro&email=example@example.com
```




