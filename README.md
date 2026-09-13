# Semana 5 - Backend for Frontend (BFF)

## Descripción

Proyecto desarrollado utilizando Spring Boot para implementar el patrón Backend for Frontend (BFF) en un sistema bancario.

La aplicación proporciona un backend específico para cada tipo de cliente del Banco XYZ, adaptando la información y las respuestas según las necesidades de cada canal:

- Web
- Aplicación móvil
- Cajero automático (ATM)

El proyecto mantiene los procesos de carga y procesamiento de datos desarrollados durante la Semana 3 mediante Spring Batch, utilizando Oracle Database como sistema de almacenamiento.

## Objetivo

Implementar una arquitectura BFF que permita:

- Personalizar las respuestas según el frontend.
- Reducir la cantidad de información enviada a clientes con restricciones de ancho de banda.
- Centralizar la composición de información necesaria para cada canal.
- Aplicar autenticación y autorización específicas para cada tipo de cliente.
- Incorporar operaciones propias del canal ATM, como depósitos y retiros.

## Arquitectura escogida

Se seleccionó la estrategia BFF basada en microservicios, implementando un BFF específico para cada canal.

Debido al alcance del proyecto, los tres BFF se encuentran actualmente modularizados dentro de una misma aplicación Spring Boot, manteniendo separación de responsabilidades y permitiendo una futura separación física en microservicios independientes.

## BFF implementados

### Web BFF

Proporciona información completa para interfaces web, integrando información proveniente de diferentes servicios de negocio.

#### Endpoints disponibles

**Resumen de cuenta**

`GET /api/web/cuentas/{cuentaId}/resumen`

Entrega:

- Información de la cuenta.
- Datos de intereses.
- Movimientos.
- Transacciones.

**Movimientos de una cuenta**

`GET /api/web/cuentas/{cuentaId}/movimientos`

Entrega los movimientos registrados para la cuenta indicada.

**Intereses de una cuenta**

`GET /api/web/cuentas/{cuentaId}/intereses`

Entrega la información de intereses asociados a la cuenta.

**Transacciones**

`GET /api/web/transacciones`

Entrega las transacciones procesadas por el sistema.

### Mobile BFF

El Mobile BFF entrega respuestas más acotadas, adaptadas a dispositivos móviles y orientadas a reducir el tamaño de la información transmitida.

#### Endpoints disponibles

**Resumen de cuenta**

`GET /api/mobile/cuentas/{cuentaId}/resumen`

Entrega:

- Información esencial de la cuenta.
- Interés aplicado.
- Período.
- Los 3 movimientos más recientes.

**Movimientos recientes**

`GET /api/mobile/cuentas/{cuentaId}/movimientos`

Entrega únicamente los 3 movimientos más recientes de la cuenta.

De esta forma se evita enviar información innecesaria y se reduce el tamaño de las respuestas.

### ATM BFF

El ATM BFF proporciona las operaciones necesarias para un cajero automático.

#### Endpoints disponibles

**Consulta de saldo**

`GET /api/atm/cuentas/{cuentaId}/saldo`

Entrega:

- Identificador de cuenta.
- Saldo disponible.

El saldo inicial utilizado como referencia corresponde al período más reciente disponible en cuentas_intereses. Posteriormente se consideran las operaciones ATM registradas para la cuenta.

**Depósito**

`POST /api/atm/cuentas/{cuentaId}/depositos`

Request:

```json
{
    "monto": 5000
}
```

Registra un depósito y devuelve:

- Identificador de cuenta.
- Monto depositado.
- Saldo disponible posterior a la operación.
- Mensaje de confirmación.

**Retiro**

`POST /api/atm/cuentas/{cuentaId}/retiros`

Request:

```json
{
    "monto": 3000
}
```

Registra un retiro cuando existe saldo suficiente y devuelve:

- Identificador de cuenta.
- Monto retirado.
- Saldo disponible posterior a la operación.
- Mensaje de confirmación.

Los depósitos y retiros se almacenan en la tabla operaciones_atm.

## Validación y manejo de errores

Las solicitudes de depósito y retiro utilizan validaciones mediante Jakarta Validation.

Por ejemplo:

- El monto es obligatorio.
- El monto debe ser mayor que cero.
- No se permite realizar retiros superiores al saldo disponible.
- Las cuentas inexistentes generan una respuesta 404 Not Found.

Los errores se manejan mediante un GlobalExceptionHandler, utilizando una estructura común:

```json
{
    "timestamp": "2026-09-13T04:16:38",
    "status": 400,
    "error": "Validation Error",
    "message": "El monto debe ser mayor que 0"
}
```

Las respuestas de colecciones sin información utilizan listas vacías ([]) en lugar de valores null.

## Seguridad

Se implementó autenticación y autorización mediante Spring Security y JWT.

Cada canal posee un rol específico:

| Canal | Rol |
| :---: | :---: |
| Web | ROL_WEB |
| Mobile | ROL_MOBILE |
| Atm | ROL_ATM |

### Autenticación

El proceso de autenticación se realiza mediante:

`POST /auth/login`

Ejemplo:

```json
{
    "username": "usuarioMobile",
    "password": "mobile123"
}
```

La respuesta contiene un access token y un refresh token:

```json
{
    "accessToken": "...",
    "refreshToken": "..."
}
```

El access token debe enviarse posteriormente mediante:

`Authorization: Bearer <accessToken>`

### Refresh token

Para renovar el access token se utiliza:

`POST /auth/refresh`

Request:

```json
{
    "refreshToken": "<refreshToken>"
}
```

Los tiempos de expiración configurados son:

- Access token: 1 hora.
- Refresh token: 7 días.

Los refresh tokens no son aceptados como tokens de acceso para los endpoints protegidos de los BFF.

### Autorización por canal

Los endpoints se encuentran protegidos según el rol correspondiente.

Por ejemplo:

- ROLE_WEB: acceso a /api/web/**
- ROLE_MOBILE: acceso a /api/mobile/**
- ROLE_ATM: acceso a /api/atm/**

Un usuario autenticado con ROLE_MOBILE puede acceder al Mobile BFF, pero no al Web BFF ni al ATM BFF.

## HTTPS

La aplicación utiliza HTTPS mediante un certificado local en formato PKCS12.

Configuración:

https://localhost:8443

El certificado utilizado durante el desarrollo es autofirmado, por lo que navegadores y herramientas como Postman pueden mostrar una advertencia de seguridad.

Este certificado está destinado únicamente al entorno local de desarrollo y demostración.

## Configuración del JWT

La clave utilizada para firmar los JWT no se encuentra almacenada directamente en el código fuente.

Se configura mediante una variable de entorno:

JWT_SECRET

En `application.properties` se utiliza:

- jwt.secret=${JWT_SECRET}
- jwt.access-token-expiration=3600000
- jwt.refresh-token-expiration=604800000

Por buenas prácticas, ésta no se incluye en el respositorio. La clave a utilizar debe ser suficientemente larga.

## Tecnología utilizada

- Java 21
- Spring Boot
- Spring Security
- JWT
- Spring Batch
- Maven
- Oracle Database
- JDBC
- JUnit
- Mockito
- SLF4J / Logback

## Estructura del proyecto

```texto
├───src
│   ├───main
│   │   ├───java
│   │   │   └───com
│   │   │       └───duoc
│   │   │           └───semana1
│   │   │               │   Semana1Application.java
│   │   │               │
│   │   │               ├───bff
│   │   │               │   ├───atm
│   │   │               │   │   │   AtmBffController.java
│   │   │               │   │   │   AtmBffService.java
│   │   │               │   │   │
│   │   │               │   │   └───dto
│   │   │               │   │           AtmDepositoRequest.java
│   │   │               │   │           AtmDepositoResponse.java
│   │   │               │   │           AtmRetiroRequest.java
│   │   │               │   │           AtmRetiroResponse.java
│   │   │               │   │           AtmSaldoResponse.java
│   │   │               │   │
│   │   │               │   ├───mobile
│   │   │               │   │   │   MobileBffController.java
│   │   │               │   │   │   MobileBffService.java
│   │   │               │   │   │
│   │   │               │   │   └───dto
│   │   │               │   │           MobileCuentaResponse.java
│   │   │               │   │           MobileMovimientoResponse.java
│   │   │               │   │           MobileResumenResponse.java
│   │   │               │   │
│   │   │               │   └───web
│   │   │               │       │   WebBffController.java
│   │   │               │       │   WebBffService.java
│   │   │               │       │
│   │   │               │       └───dto
│   │   │               │               WebCuentaResponse.java
│   │   │               │               WebInteresResponse.java
│   │   │               │               WebMovimientoResponse.java
│   │   │               │               WebResumenResponse.java
│   │   │               │               WebTransaccionResponse.java
│   │   │               │
│   │   │               ├───config
│   │   │               │       BatchConfig.java
│   │   │               │       CuentaInteresBatchConfig.java
│   │   │               │       CuentaInteresItemReaderConfig.java
│   │   │               │       CuentaInteresItemWriterConfig.java
│   │   │               │       DataSourceConfig.java
│   │   │               │       ExecutorShutdown.java
│   │   │               │       MovimientoAnualItemReaderConfig.java
│   │   │               │       MovimientoAnualItemWriterConfig.java
│   │   │               │       TransaccionBatchConfig.java
│   │   │               │       TransaccionItemReaderConfig.java
│   │   │               │       TransaccionItemWriterConfig.java
│   │   │               │
│   │   │               ├───exception
│   │   │               │       CuentaInteresException.java
│   │   │               │       ErrorResponse.java
│   │   │               │       GlobalExceptionHandler.java
│   │   │               │       InsufficientBalanceException.java
│   │   │               │       MovimientoAnualException.java
│   │   │               │       ResourceNotFoundException.java
│   │   │               │       TransaccionException.java
│   │   │               │
│   │   │               ├───listener
│   │   │               │       CuentaInteresStepExecutionListener.java
│   │   │               │       JobCompletionListener.java
│   │   │               │       MovimientoAnualStepExecutionListener.java
│   │   │               │       TransaccionStepExecutionListener.java
│   │   │               │
│   │   │               ├───model
│   │   │               │       CuentaInteres.java
│   │   │               │       MovimientoAnual.java
│   │   │               │       OperacionAtm.java
│   │   │               │       Transaccion.java
│   │   │               │
│   │   │               ├───processor
│   │   │               │       CuentaInteresItemProcessor.java
│   │   │               │       MovimientoAnualItemProcessor.java
│   │   │               │       TransaccionItemProcessor.java
│   │   │               │
│   │   │               ├───repository
│   │   │               │       CuentaInteresRepository.java
│   │   │               │       MovimientoAnualRepository.java
│   │   │               │       OperacionAtmRepository.java
│   │   │               │       TransaccionRepository.java
│   │   │               │
│   │   │               ├───security
│   │   │               │   │   AuthController.java
│   │   │               │   │   JwtAuthenticationFilter.java
│   │   │               │   │   JwtService.java
│   │   │               │   │   SecurityConfig.java
│   │   │               │   │
│   │   │               │   └───dto
│   │   │               │           LoginRequest.java
│   │   │               │           LoginResponse.java
│   │   │               │           RefreshTokenRequest.java
│   │   │               │
│   │   │               └───services
│   │   │                       CuentaInteresService.java
│   │   │                       MovimientoAnualService.java
│   │   │                       TransaccionService.java
│   │   │
│   │   └───resources
│   │           application.properties
│   │           cuentas_anuales.csv
│   │           https.p12
│   │           intereses.csv
│   │           logback-spring.xml
│   │           schema.sql
│   │           transacciones.csv
```

## Procesamiento de datos

Los datos utilizados por los BFF son generados mediante los procesos de Spring Batch implementados durante la Semana 3.

Se mantienen tres Jobs:

- `movimientoAnualJob`
- `cuentaInteresJob`
- `transaccionJob`

Estos procesos validan, transforman y almacenan la información proveniente de los archivos CSV en Oracle Database.

Las operaciones realizadas desde el ATM se almacenan independientemente en la tabla:

`operaciones_atm`

Esta tabla permite registrar depósitos y retiros sin modificar los procesos existentes de Spring Batch.

## Configuración de la base de datos

El proyecto utiliza Oracle Database mediante una conexión configurada
con wallet.

Por motivos de seguridad, las credenciales y ruta de la wallet no se 
encuentran almacenadas en el repositorio.

La configuración se realiza mediante las variables de entorno desde
application.properties:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `DB_TNS_ADMIN`

La variable `DB_TNS_ADMIN` debe apuntar a la carpeta que contiene los
archivos de configuración de la wallet.

Por ejemplo:

`DB_TNS_ADMIN=C:\ruta\a\la\wallet`

Las variables deben adaptarse a la configuración de la base de datos
utilizada por cada usuario.

## Ejecución

### 1. Requisitos previos

Para ejecutar el proyecto se requiere:

- Java 21
- Maven
- Acceso a una base de datos Oracle
- Una wallet de Oracle válida para la conexión
- Configuración de las variables de entorno requeridas

### 2. Configuración de las variables de entorno

Antes de ejecutar el proyecto se deben configurar las siguientes
variables de entorno:

- `DB_URL`: URL de conexión a Oracle
- `DB_USERNAME`: usuario de la base de datos
- `DB_PASSWORD`: contraseña del usuario
- `DB_TNS_ADMIN`: ruta local donde se encuentra la wallet de Oracle
- `JWT_SECRET`: clave utilizada para firmar los tokens JWT.

### 3. Ejecución de la aplicación

La aplicación se inicia mediante:

`mvn spring-boot:run`

La aplicación se ejecuta mediante HTTPS en:

`https://localhost:8443`

### 4. Base de datos

El proyecto requiere un usuario de Oracle con permisos suficientes
para conectarse a la base de datos y crear las tablas utilizadas por
la aplicación.

Las tablas de Spring Batch se inicializan según la configuración de:

`spring.batch.jdbc.initialize-schema`

La inicialización automática del esquema SQL de la aplicación se encuentra desactivada:

`spring.sql.init.mode=never`

Por lo tanto, las tablas de negocio deben encontrarse previamente creadas en la base de datos.

Si se ejecuta la aplicación por primera vez, se recomienda cambiar esta
configuración desde `application.properties`, indicando `always`.

### 5. Ejecución de los Jobs

El proyecto contiene tres Jobs independientes, los cuales se recomiendan ejecutar previo a
realizar llamados a los BFF.

Para ejecutar un Job específico, se debe indicar su nombre mediante la propiedad:

`spring.batch.job.name`

Por ejemplo:

`spring.batch.job.name=movimientoAnualJob`

Los Jobs disponibles son:

- `movimientoAnualJob`
- `cuentaInteresJob`
- `transaccionJob`

La propiedad **debe** agregarse temporalmente en `application.properties`
**antes** de ejecutar la aplicación, seleccionando el Job que se desea
ejecutar.

Después de seleccionar el Job, se puede ejecutar la aplicación
Spring Boot normalmente.

### 6. Autenticación y usuarios de prueba

Antes de acceder a los BFF se debe obtener un token mediante:

`POST https://localhost:8443/auth/login`

Ejemplo:

```json
{
    "username": "usuarioMobile",
    "password": "mobile123"
}
```

**Usuarios disponibles:**

| Usuario | Contraseña | Rol |
| :---: | :---: | :---: |
| usuarioWeb | web123 | ROL_WEB |
| usuarioMobile | mobile123 | ROL_MOBILE |
| usuarioAtm | atm123 | ROL_ATM |

Estas credenciales corresponden únicamente a usuarios de prueba definidos en memoria para demostrar la autenticación y autorización por canal.

Después de obtener el access token, este debe enviarse en las solicitudes protegidas mediante:

`Authorization: Bearer <accessToken>`

## Resumen de endpoints

| Método | Endpoint | Canal | Descripción |
| :---: | --- | :---: | --- |
| `POST` | `/auth/login` | Seguridad | Autenticación y obtención de tokens |
| `POST` | `/auth/refresh` | Seguridad | Renovación del access token |
| `GET` | `/api/web/cuentas/{cuentaId}/resumen` | Web | Resumen completo de la cuenta |
| `GET` | `/api/web/cuentas/{cuentaId}/movimientos` | Web | Movimientos de la cuenta |
| `GET` | `/api/web/cuentas/{cuentaId}/intereses` | Web | Información de intereses |
| `GET` | `/api/web/transacciones` | Web | Transacciones procesadas |
| `GET` | `/api/mobile/cuentas/{cuentaId}/resumen` | Mobile | Resumen reducido de la cuenta |
| `GET` | `/api/mobile/cuentas/{cuentaId}/movimientos` | Mobile | 3 movimientos más recientes |
| `GET` | `/api/atm/cuentas/{cuentaId}/saldo` | ATM | Consulta de saldo |
| `POST` | `/api/atm/cuentas/{cuentaId}/depositos` | ATM | Registrar depósito |
| `POST` | `/api/atm/cuentas/{cuentaId}/retiros` | ATM | Registrar retiro |
