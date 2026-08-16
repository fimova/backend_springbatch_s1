# Semana 1 - Spring Batch

## Descripción

Proyecto desarrollado utilizando Spring Boot y Spring Batch para
procesar archivos CSV mediante Jobs de procesamiento por lotes,
validando, transformando y almacenando la información en una base
de datos Oracle.

## Objetivo

El proyecto implementa tres procesos independientes:

- Generación de estados de cuenta anuales
- Cálculo de intereses mensuales
- Reporte de transacciones diarias

Cada proceso utiliza la estructura de Spring Batch basada en
ItemReader, ItemProcessor y ItemWriter.

## Tecnologías utilizadas

- Java 21
- Spring Boot
- Spring Batch
- Maven
- Oracle Database
- JDBC
- JUnit
- Mockito

## Estructura del proyecto

src/
├── main/
│   ├── java/com/duoc/semana1/
│   │   ├── config/
│   │   ├── exception/
│   │   ├── listener/
│   │   ├── model/
│   │   └── processor/
│   └── resources/
│       ├── *.csv
│       ├── application.properties
│       └── schema.sql
└── test/
    └── java/com/duoc/semana1/
        ├── processor/
        ├── reader/
        └── writer/

## Jobs implementados

1. Movimiento Anual

Procesa el archivo cuentas_anuales.csv, valida los registros y 
almacena los movimientos válidos en la base de datos.

Los registros que no cumplen con las reglas de negocio son omitidos
mediante el mecanismo de  tolerancia a fallo (faultTolerant) y
skip de Spring Batch.

2. Calculo de Intereses Mensuales

Procesa intereses.csv, calcula el interés correspondiente según
el tipo de cuenta y genera un saldo final.

Para este ejercicio se definieron las siguientes tasas de interés:

- Ahorro: 2%
- Préstamo: 5%
- Hipoteca: 4%

El interés se calcula sobre el saldo inicial de cada cuenta y se
almacena junto con el saldo final.

3. Reporte de Transacciones diarias

Procesa transacciones.csv y detecta transacciones anómalas según 
las reglas de validación definidas, registrando la anomalía, sin 
omitir el procesamiento de ese dato y almacenándolo en la base de
datos.

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

### 5. Archivos de entrada

Los archivos CSV utilizados por los Jobs se encuentran en:

`src/main/resources/`

- `cuentas_anuales.csv`
- `intereses.csv`
- `transacciones.csv`

No es necesario modificar sus rutas para ejecutar el proyecto.