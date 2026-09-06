# Semana 4 - Backend for Frontend (BFF)

## Descripción

Proyecto desarrollado utilizando Spring Boot para implementar el patrón Backend for Frontend (BFF) en un sistema bancario.

La aplicación proporciona un backend específico para cada tipo de cliente del Banco XYZ, adaptando la información y las respuestas según las necesidades de cada canal:

- Web
- Aplicación móvil
- Cajero automático (ATM)

El proyecto mantiene los procesos de carga y procesamiento de datos desarrollados durante la Semana 3 mediante Spring Batch, utilizando Oracle Database como sistema de almacenamiento.

## Objetivo

Implementar una arquitectura BFF que permita:

Personalizar las respuestas según el frontend.
Reducir la cantidad de información enviada a clientes con restricciones de ancho de banda.
Centralizar la composición de información necesaria para cada canal.
Aplicar autenticación y autorización específicas para cada tipo de cliente.

## Arquitectura escogida

Se seleccionó la estrategia BFF basada en microservicios, implementando un BFF específico para cada canal.

Debido al alcance del proyecto, los tres BFF se encuentran actualmente modularizados dentro de una misma aplicación Spring Boot, manteniendo separación de responsabilidades y permitiendo una futura separación física en microservicios independientes.

## BFF implementados

### Web BFF

Endpoint:

`GET /api/web/cuentas/{cuentaId}/resumen`

Proporciona información completa para interfaces web, incluyendo:

- Información de la cuenta.
- Datos de intereses.
- Movimientos.
- Transacciones.

### Mobile BFF

Endpoint:

`GET /api/mobile/cuentas/{cuentaId}/resumen`

Entrega una respuesta más liviana, adaptada a dispositivos móviles:

- Información esencial de la cuenta.
- Interés aplicado.
- Período.
- Los 3 movimientos más recientes.

Se evita enviar información innecesaria para reducir el tamaño de la respuesta.

### ATM BFF

Endpoint:

`GET /api/atm/cuentas/{cuentaId}/saldo`

Entrega únicamente la información necesaria para una consulta de saldo:

- Identificador de cuenta.
- Saldo de referencia.

El saldo utilizado corresponde al saldo_inicial del período más reciente disponible en cuentas_intereses, debido a que el esquema actual no posee una tabla de cuentas con un saldo actual independiente.

## Seguridad

Se implementó autenticación y autorización mediante Spring Security y JWT.

Cada canal posee un rol específico:

| Canal | Rol |
| :---: | :---: |
| Web | ROL_WEB |
| Mobile | ROL_MOBILE |
| Atm | ROL_ATM |

El proceso de autenticación se realiza mediante:

`POST /auth/login`

El usuario obtiene un token JWT que posteriormente debe enviarse mediante:

`Authorization: Bearer <token>`

Los endpoints se encuentran protegidos según el rol correspondiente.

Por ejemplo, un usuario autenticado con `ROLE_MOBILE` puede acceder al Mobile BFF, pero no al Web BFF ni al ATM BFF.

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
│   │   │               │
│   │   │               └───services
│   │   │                       CuentaInteresService.java
│   │   │                       MovimientoAnualService.java
│   │   │                       TransaccionService.java
│   │   │
│   │   └───resources
│   │           application.properties
│   │           cuentas_anuales.csv
│   │           intereses.csv
│   │           logback-spring.xml
│   │           schema.sql
│   │           transacciones.csv

## Procesamiento de datos

Los datos utilizados por los BFF son generados mediante los procesos de Spring Batch implementados durante la Semana 3.

Se mantienen tres Jobs:

- `movimientoAnualJob`
- `cuentaInteresJob`
- `transaccionJob`

Estos procesos validan, transforman y almacenan la información proveniente de los archivos CSV en Oracle Database.

## Configuración de la base de datos

El proyecto utiliza Oracle Database mediante una conexión configurada
con wallet.

Por motivos de seguridad, las credenciales y ruta de la wallet no se 
encuentran almacenadas en el repositorio.

La configuración se realiza mediante las variables de entorno desde
application.properties:

- DB_URL
- DB_USERNAME
- DB_PASSWORD
- DB_TNS_ADMIN

## Ejecución

### 1. Requisitos previos

Para ejecutar el proyecto se requiere:

- Java 21
- Maven
- Acceso a una base de datos Oracle
- Una wallet de Oracle válida para la conexión

### 2. Configuración de las variables de entorno

Antes de ejecutar el proyecto se deben configurar las siguientes
variables de entorno:

- `DB_URL`: URL de conexión a Oracle
- `DB_USERNAME`: usuario de la base de datos
- `DB_PASSWORD`: contraseña del usuario
- `DB_TNS_ADMIN`: ruta local donde se encuentra la wallet de Oracle

La variable `DB_TNS_ADMIN` debe apuntar a la carpeta que contiene los
archivos de configuración de la wallet.

Por ejemplo:

`DB_TNS_ADMIN=C:\ruta\a\la\wallet`

Las variables deben adaptarse a la configuración de la base de datos
utilizada por cada usuario.

Luego se puede iniciar la aplicación mediante:

`mvn spring-boot:run`

La aplicación se ejecuta por defecto en:

`http://localhost:8080`

### 3. Base de datos

El proyecto requiere un usuario de Oracle con permisos suficientes
para conectarse a la base de datos y crear las tablas utilizadas por
la aplicación.

Al iniciar la aplicación, Spring ejecuta el archivo `schema.sql`,
que contiene la creación de las tablas utilizadas por los Jobs.

Además, Spring Batch inicializa las tablas necesarias para mantener
el historial de ejecución de los Jobs.

### 4. Ejecución de los Jobs

El proyecto contiene tres Jobs independientes. Para ejecutar un Job
específico, se debe indicar su nombre mediante la propiedad:

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

### 5. Autenticación

Antes de acceder a los BFF se debe obtener un token mediante:

`POST /auth/login`

Ejemplo:

{
    "username": "usuarioMobile",
    "password": "mobile123"
}

El token obtenido debe utilizarse en las solicitudes protegidas mediante el header:

Authorization: Bearer <token>

### 6. Usuarios de prueba

| Usuario | Contraseña | Rol |
| :---: | :---: | :---: |
| usuarioWeb | web123 | ROL_WEB |
| usuarioMobile | mobile123 | ROL_MOBILE |
| usuarioAtm | atm123 | ROL_ATM |

Estas credenciales corresponden únicamente a usuarios de prueba definidos en memoria para demostrar la autenticación y autorización por canal.
