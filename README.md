# 🚀 Taskflow API

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=springboot)
![Docker](https://img.shields.io/badge/Docker-Multi--stage-2496ED?style=for-the-badge&logo=docker)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-4169E1?style=for-the-badge&logo=postgresql)
![CI/CD](https://img.shields.io/badge/CI-GitHub_Actions-2088FF?style=for-the-badge&logo=githubactions)

**Taskflow API** es una solución Backend RESTful moderna, robusta y escalable diseñada para la gestión integral de tareas y proyectos. El proyecto está construido bajo buenas prácticas de arquitectura en capas, seguridad mediante JSON Web Tokens (JWT) y una sólida suite de pruebas unitarias integradas en un flujo de **Integración Continua (CI/CD)**.

---

## 🛠️ Stack Tecnológico

* **Lenguaje:** Java 21 (LTS)
* **Framework Backend:** Spring Boot 3 (Spring Web, Spring Security, Spring Data JPA)
* **Seguridad & Autenticación:** JSON Web Tokens (JWT) / BCrypt
* **Base de Datos:** PostgreSQL
* **Pruebas Unitarias:** JUnit 5, Mockito
* **Contenedorización:** Docker (Multi-stage build)
* **CI/CD Pipeline:** GitHub Actions

---

## 🏗️ Arquitectura del Proyecto

El código fuente sigue un diseño limpio y desacoplado, estructurado en capas para garantizar mantenibilidad y modularidad:

src/main/java/com/taskflow_api/
├── config/       # Configuraciones de Seguridad, JWT Filter y Beans
├── controllers/  # Controladores REST (Endpoints)
├── dto/          # Data Transfer Objects (Peticiones y Respuestas)
├── exceptions/   # Manejo Global de Excepciones y Handlers
├── models/       # Entidades JPA (Mapeo de la Base de Datos)
├── repositories/ # Interfaces Spring Data JPA
└── services/     # Lógica de Negocio (Capa aislada probada con Mockito)

---

## 🧪 Suite de Pruebas Unitarias & CI/CD

La lógica de negocio central cuenta con un conjunto automatizado de 14 pruebas unitarias aisladas escritas con **JUnit 5** y **Mockito**.

### Integración Continua (GitHub Actions)
Cada `push` o `pull_request` a la rama `main` dispara automáticamente un workflow de **GitHub Actions** que:
1. Despliega un contenedor de servicios **PostgreSQL 15**.
2. Configura un entorno aislado con **JDK 21 Temurin**.
3. Compila el proyecto y ejecuta `./mvnw test`.

### Ejecutar Pruebas Localmente

./mvnw clean test

---

## ⚙️ Configuración de Variables de Entorno

Para ejecutar la aplicación localmente o en un entorno de producción (Railway / Docker), crea un archivo `.env` en la raíz del proyecto. **Nunca subas tu archivo `.env` real al repositorio de control de versiones.**

Estructura requerida:

| Variable | Descripción | Formato Requerido |
| :--- | :--- | :--- |
| `SPRING_DATASOURCE_URL` | URL de conexión JDBC a PostgreSQL | `jdbc:postgresql://<HOST>:<PUERTO>/<NOMBRE_BD>` |
| `SPRING_DATASOURCE_USERNAME` | Usuario de la base de datos | Nombre de usuario configurado en PostgreSQL |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña del usuario | Contraseña configurada en PostgreSQL |
| `JWT_SECRET` | Clave secreta para la firma de tokens | Cadena segura codificada en HEX o Base64 |
| `JWT_EXPIRATION` | Tiempo de validez del token JWT en ms | Valor numérico (ej. 86400000 para 24 horas) |

---

## 🚀 Despliegue y Ejecución con Docker

La aplicación implementa un `Dockerfile` optimizado con **Multi-stage builds** usando imágenes Alpine de Eclipse Temurin para maximizar la velocidad de compilación y reducir el tamaño final de la imagen.

### 1. Construir la Imagen de Docker

docker build -t taskflow-api .

### 2. Ejecutar el Contenedor con Variables de Entorno

docker run -d -p 8080:8080 --env-file .env --name taskflow-app taskflow-api

---

## 🔌 Endpoints de la API

### Autenticación (`/api/v1/auth`)

| Método | Endpoint | Descripción | Requiere Auth |
| :---: | :--- | :--- | :---: |
| `POST` | `/api/v1/auth/register` | Registro de nuevos usuarios | ❌ |
| `POST` | `/api/v1/auth/login` | Autenticación y obtención de JWT | ❌ |

### Gestión de Tareas (`/api/v1/tasks`)

| Método | Endpoint | Descripción | Requiere Auth |
| :---: | :--- | :--- | :---: |
| `GET` | `/api/v1/tasks` | Listar todas las tareas del usuario autenticado | ✅ Bearer JWT |
| `POST` | `/api/v1/tasks` | Crear una nueva tarea | ✅ Bearer JWT |
| `GET` | `/api/v1/tasks/{id}` | Obtener detalle de una tarea por ID | ✅ Bearer JWT |
| `PUT` | `/api/v1/tasks/{id}` | Actualizar título, descripción o estado | ✅ Bearer JWT |
| `DELETE` | `/api/v1/tasks/{id}` | Eliminar una tarea | ✅ Bearer JWT |

---

## ✒️ Autor

* **Daniel Monroy Hernández** - Backend & Software Developer - [GitHub](https://github.com/danielomh24)
