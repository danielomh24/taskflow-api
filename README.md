# 🚀 Taskflow API

<p align="center">
  <a href="https://taskflow-api-production-52a1.up.railway.app">
    <img src="https://img.shields.io/badge/Live_Demo-Railway-0B0D0E?style=for-the-badge&logo=railway&logoColor=white" alt="Live Demo" />
  </a>
  <a href="https://github.com/danielomh24/taskflow-api">
    <img src="https://img.shields.io/badge/Source_Code-GitHub-181717?style=for-the-badge&logo=github&logoColor=white" alt="GitHub" />
  </a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/PostgreSQL-Database-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL" />
  <img src="https://img.shields.io/badge/Docker-Multi--stage-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker" />
  <img src="https://img.shields.io/badge/CI%2FCD-GitHub_Actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white" alt="GitHub Actions" />
</p>

---

## 📌 Descripción

**Taskflow API** es una solución Backend RESTful moderna, robusta y escalable diseñada para la gestión integral de tareas y proyectos. El proyecto está construido siguiendo buenas prácticas de arquitectura en capas, seguridad basada en **JSON Web Tokens (JWT)** y una sólida suite de pruebas unitarias automatizadas e integradas en un pipeline continuo de **CI/CD**.

---

## 🛠️ Stack Tecnológico

* **Lenguaje:** Java 21 (LTS)
* **Framework Backend:** Spring Boot 3 (Spring Web, Spring Security, Spring Data JPA)
* **Seguridad & Autenticación:** JSON Web Tokens (JWT) / BCrypt
* **Base de Datos:** PostgreSQL
* **Pruebas Unitarias:** JUnit 5, Mockito
* **Contenedorización:** Docker (Multi-stage build)
* **CI/CD Pipeline:** GitHub Actions
* **Despliegue Cloud:** Railway

---

## 🌐 Live API & Despliegue

La aplicación se encuentra desplegada y en producción a través de **Railway**:

* 🔗 **Base URL:** `https://taskflow-api-production-52a1.up.railway.app`

---

## 🧪 Pruebas Rápidas & Testing de la API

Puedes interactuar y probar la API mediante cualquiera de las siguientes opciones:

### Opción 1: Archivo de Peticiones Interactivo (Recomendado)
El proyecto incluye un archivo preparado con solicitudes HTTP en [`src/main/resources/taskflow-api.http`](./src/main/resources/taskflow-api.http).

Si utilizas **IntelliJ IDEA** o **VS Code** (con la extensión *REST Client*), puedes abrir dicho archivo y ejecutar peticiones directamente contra el servidor de producción con un solo clic.

### Opción 2: Autenticación mediante cURL

**Solicitud de Login:**
curl -X POST https://taskflow-api-production-52a1.up.railway.app/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "password123"
  }'

**Respuesta Esperada:**
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer"
}

---

## 🏗️ Arquitectura del Proyecto

El código fuente sigue una estructura limpia y desacoplada organizada por capas:

src/main/java/com/taskflow_api/
├── config/       # Seguridad, Filtros JWT y Beans Globales
├── controllers/  # Controladores REST (Endpoints)
├── dto/          # Data Transfer Objects (Request/Response)
├── exceptions/   # Manejo Global de Excepciones (GlobalExceptionHandler)
├── models/       # Entidades JPA (Mapeo O/R)
├── repositories/ # Interfaces Spring Data JPA
└── services/     # Lógica de Negocio (Probada de forma aislada con Mockito)

---

## 🧪 Pruebas Unitarias & Integración Continua (CI/CD)

La capa de servicio cuenta con una suite automatizada de **14 pruebas unitarias aisladas** utilizando **JUnit 5** y **Mockito**.

### Pipeline en GitHub Actions
Cada `push` o `pull_request` a la rama `main` ejecuta un workflow automatizado que:
1. Instala un entorno aislado con **JDK 21 Temurin**.
2. Despliega un servicio temporal de **PostgreSQL 15**.
3. Compila el proyecto y ejecuta el conjunto de pruebas (`./mvnw test`).

### Ejecución Local de Pruebas

./mvnw clean test

---

## ⚙️ Configuración del Entorno (.env)

Crea un archivo `.env` en la raíz del proyecto para definir las credenciales necesarias. **No incluyas tu archivo `.env` real dentro del control de versiones.**

SPRING_DATASOURCE_URL=jdbc:postgresql://<HOST>:<PUERTO>/<NOMBRE_BD>
SPRING_DATASOURCE_USERNAME=<USUARIO_BD>
SPRING_DATASOURCE_PASSWORD=<PASSWORD_BD>
JWT_SECRET=<TU_CLAVE_SECRETA_BASE64>
JWT_EXPIRATION=86400000

---

## 🚀 Despliegue Local con Docker

El proyecto utiliza un `Dockerfile` optimizado mediante **Multi-stage builds** sobre imágenes Eclipse Temurin Alpine para reducir el peso final del contenedor.

### 1. Construir la Imagen

docker build -t taskflow-api .

### 2. Desplegar el Contenedor

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
| `GET` | `/api/v1/tasks` | Listar todas las tareas del usuario | ✅ Bearer JWT |
| `POST` | `/api/v1/tasks` | Crear una nueva tarea | ✅ Bearer JWT |
| `GET` | `/api/v1/tasks/{id}` | Obtener detalle de una tarea | ✅ Bearer JWT |
| `PUT` | `/api/v1/tasks/{id}` | Actualizar datos o estado de la tarea | ✅ Bearer JWT |
| `DELETE` | `/api/v1/tasks/{id}` | Eliminar una tarea | ✅ Bearer JWT |

---

## ✒️ Autor

* **Daniel Monroy Hernández** - Backend & Software Developer - [GitHub](https://github.com/danielomh24)
