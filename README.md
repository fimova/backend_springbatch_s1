# Semana 3 - Spring Batch

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
- SLF4J / Logback

## Arquitectura escogida

Se implementó procesamiento multithread mediante `ThreadPoolTaskExecutor` en los tres Steps.

La decisión se tomó considerando el tamaño de los datasets utilizados, la naturaleza del procesamiento
y la complejidad adicional que implica una estrategia de particionamiento.

Se realizaron pruebas de rendimiento modificando el tamaño del chunk y los parámetros del pool 
de ejecución, observando su impacto sobre el tiempo de ejecución, memoria utilizada 
y estabilidad del procesamiento.

Para el escenario actual se determinó que el procesamiento multithread permite incorporar 
concurrencia de forma adecuada sin necesidad de implementar partitioning. 
Además, se incorporó `CallerRunsPolicy` para aplicar backpressure cuando el executor 
alcanza su capacidad, evitando el rechazo de nuevas tareas.

## Configuración del rendimiento

Los parámetros de concurrencia se encuentran externalizados en application.properties, 
permitiendo modificar la configuración sin alterar el código fuente.

Los principales parámetros configurables son:

- Tamaño del chunk
- Core Pool Size
- Max Pool Size
- Queue Capacity

Las configuraciones utilizadas pueden variar según el Job y el entorno de ejecución. 
Los valores empleados para las pruebas de esta entrega fueron seleccionados a partir de benchmarking 
y observación del comportamiento de cada proceso.

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

## Procesamiento y tolerancia a fallos

Los Jobs utilizan procesamiento por chunks y ejecución concurrente mediante `ThreadPoolTaskExecutor`.

El tamaño del chunk y los parámetros del executor se encuentran definidos mediante 
propiedades externas en `application.properties`, permitiendo ajustar la configuración 
de acuerdo con las características del proceso y los recursos disponibles.

El executor utiliza `CallerRunsPolicy` como estrategia ante saturación, 
permitiendo que el hilo que intenta enviar una tarea la ejecute directamente 
cuando el pool y la cola se encuentran ocupados.

### Tolerancia a fallos

Los Jobs utilizan políticas de tolerancia a fallos mediante `faultTolerant()`.

Las excepciones propias de las reglas de negocio son controladas mediante skip, 
permitiendo omitir registros inválidos sin detener completamente el procesamiento.

Además, se implementaron políticas de reintento para errores transitorios
relacionados con el acceso a la base de datos:

- `CannotAcquireLockException`
- `TransientDataAccessException`

Cada una de estas excepciones puede ser reintentada hasta 3 veces mediante `retryLimit(3)`.

También se controla `DuplicateKeyException` en los procesos correspondientes, 
permitiendo manejar registros que ya fueron almacenados previamente.

### Idempotencia

Los procesos cuentan con mecanismos de idempotencia mediante restricciones de 
unicidad en las tablas de destino y el manejo de `DuplicateKeyException`.

Esto permite realizar reejecuciones de los Jobs sin generar duplicaciones de 
información previamente almacenada. Los registros que ya cumplen las condiciones de 
unicidad son detectados y omitidos durante la escritura.

### Monitoreo y logging

Para observar el comportamiento de los procesos se implementaron `StepExecutionListener` 
y `JobExecutionListener`.

Los listeners registran información relevante de cada ejecución, incluyendo:

- Job ejecutado
- Step ejecutado
- Estado de la ejecución
- Cantidad de registros leídos
- Cantidad de registros escritos
- Cantidad de registros omitidos
- Cantidad de errores
- Tiempo de ejecución
- Memoria utilizada
- Memoria máxima disponible

El registro se realiza mediante SLF4J/Logback, utilizando distintos niveles según el tipo de evento:

- INFO: inicio y finalización de Jobs y Steps, además de métricas generales.
- WARN: registros omitidos y situaciones esperadas que requieren seguimiento.
- ERROR: errores inesperados o fallos durante la ejecución.

Los mensajes incluyen información del Job y Step correspondiente, facilitando la trazabilidad de las ejecuciones.

### Cierre de recursos 

Para evitar que los hilos del procesamiento paralelo mantengan la aplicación en ejecución
una vez finalizado el Job, se implementó un componente `ExecutorShutdown` que cierra
los ThreadPoolTaskExecutor mediante `@PreDestroy`.

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

### 4. Configuración de los parámetros de ejecución

Los parámetros de concurrencia se encuentran en application.properties 
y pueden modificarse antes de ejecutar la aplicación.

Entre ellos se encuentran:

app.movimiento.chunk-size=20
app.movimiento.core-pool-size=4
app.movimiento.max-pool-size=8
app.movimiento.queue-capacity=50

Los nombres y valores pueden variar según el Job. Se recomienda revisar 
las propiedades correspondientes antes de ejecutar cada proceso.

### 5. Ejecución de los Jobs

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

### 6. Archivos de entrada

Los archivos CSV utilizados por los Jobs se encuentran en:

`src/main/resources/`

- `cuentas_anuales.csv`
- `intereses.csv`
- `transacciones.csv`

No es necesario modificar sus rutas para ejecutar el proyecto.