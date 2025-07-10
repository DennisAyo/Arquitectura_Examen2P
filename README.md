# Microservicio de Ventanilla Bancaria

## Descripción
Microservicio para el manejo del efectivo en las ventanillas de las agencias del Banco Banquito. Permite a los cajeros realizar tres operaciones principales: abrir turno, procesar transacciones y cerrar turno, con control detallado de denominaciones de billetes.

## Funcionalidades Principales

### 1. Abrir Turno
- El cajero registra el dinero recibido de la bóveda del banco
- Se especifica la cantidad de billetes de cada denominación
- Se genera un código único de turno: `{CODIGO_CAJA}-{CODIGO_CAJERO}-{YYYYMMDD}`
- Se registra automáticamente una transacción de tipo "INICIO"

### 2. Procesar Transacciones
- Registro de transacciones de retiro (AHORRO) o depósito (DEPOSITO)
- Control detallado de billetes recibidos (+) o entregados (-)
- Validación de que el turno esté abierto antes de procesar

### 3. Cerrar Turno
- El cajero ingresa la cantidad final de billetes por denominación
- El sistema compara el monto calculado vs el monto declarado
- **Alerta automática** si hay diferencias en el balance
- Se registra automáticamente una transacción de tipo "CIERRE"

## Tecnologías Utilizadas

- **Java 21**
- **Spring Boot 3.5.3**
- **Spring Data JPA** - Para persistencia de datos
- **PostgreSQL** - Base de datos relacional
- **MapStruct** - Para mapeo automático entre DTOs y entidades
- **OpenAPI/Swagger** - Documentación de API
- **Lombok** - Para reducir código boilerplate
- **SLF4J** - Para logging

## Estructura del Proyecto

```
src/main/java/com/banquito/analisis/
├── config/                 # Configuraciones (CORS, OpenAPI)
├── controller/            # Controladores REST
│   ├── dto/              # Data Transfer Objects
│   └── mapper/           # Mappers con MapStruct
├── enums/                # Enumeraciones
├── exception/            # Excepciones personalizadas
├── model/                # Entidades JPA
├── repository/           # Repositorios JPA
└── service/              # Lógica de negocio
```

## Modelos de Datos

### TurnosCaja
- **codigoTurno**: Código único del turno (PK)
- **codigoCaja**: Código de la caja
- **codigoCajero**: Código del cajero
- **inicioTurno**: Fecha y hora de inicio
- **montoInicial**: Monto inicial del turno
- **finTurno**: Fecha y hora de fin
- **montoFinal**: Monto final del turno
- **estado**: ABIERTO o CERRADO

### TransaccionesTurno
- **id**: Identificador único (PK)
- **codigoCaja**: Código de la caja
- **codigoCajero**: Código del cajero
- **codigoTurno**: Código del turno
- **tipoTransaccion**: INICIO, AHORRO, DEPOSITO, CIERRE
- **montoTotal**: Monto total de la transacción
- **fechaTransaccion**: Fecha y hora de la transacción
- **denominaciones**: Lista de denominaciones de billetes

### DenominacionBilletes
- **billete**: Tipo de billete (1, 5, 10, 20, 50, 100 dólares)
- **cantidadBilletes**: Cantidad de billetes
- **monto**: Monto calculado (valor × cantidad)

## Endpoints de la API

### Gestión de Turnos
- `POST /v1/ventanilla/turnos/abrir` - Abrir turno
- `POST /v1/ventanilla/turnos/cerrar` - Cerrar turno
- `GET /v1/ventanilla/turnos/{codigoTurno}` - Obtener turno por código
- `GET /v1/ventanilla/cajeros/{codigoCajero}/turnos` - Obtener turnos por cajero

### Gestión de Transacciones
- `POST /v1/ventanilla/transacciones/procesar` - Procesar transacción
- `GET /v1/ventanilla/turnos/{codigoTurno}/transacciones` - Obtener transacciones por turno

## Denominaciones Soportadas

El sistema maneja las siguientes denominaciones de dólares:
- $1.00
- $5.00
- $10.00
- $20.00
- $50.00
- $100.00

## Validaciones y Excepciones

### Excepciones Personalizadas
- `TurnoNotFoundException`: Turno no encontrado
- `TurnoYaAbiertoException`: Ya existe un turno abierto para la caja/cajero
- `TurnoYaCerradoException`: El turno ya está cerrado
- `DatosInvalidosException`: Datos inválidos en la solicitud

### Validaciones de Negocio
- Solo puede haber un turno abierto por caja/cajero
- Las transacciones solo se procesan en turnos abiertos
- Las cantidades de billetes no pueden ser negativas
- Se requiere al menos una denominación en cada operación

## Sistema de Alertas

Al finalizar un turno, el sistema:
1. Calcula el monto esperado basado en las transacciones
2. Compara con el monto declarado por el cajero
3. Genera una alerta si hay diferencias
4. Retorna información detallada sobre la diferencia

## Configuración

### Base de Datos
```properties
spring.datasource.url=jdbc:postgresql://HOST:PORT/DATABASE
spring.datasource.username=USERNAME
spring.datasource.password=PASSWORD
spring.jpa.properties.hibernate.default_schema=ventanilla_efectivo
```

### Servicios Principales
- **GestionCajaService**: Contiene la lógica de negocio para abrir turno, procesar transacciones y cerrar turno

### Swagger UI
La documentación de la API está disponible en:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/api-docs`

## Instalación y Ejecución

1. **Clonar el repositorio**
```bash
git clone <repository-url>
cd analisis
```

2. **Configurar la base de datos**
   - Crear esquema `ventanilla_efectivo` en PostgreSQL
   - Actualizar credenciales en `application.properties`

3. **Ejecutar la aplicación**
```bash
mvn clean install
mvn spring-boot:run
```

4. **Acceder a la documentación**
   - Swagger UI: `http://localhost:8080/swagger-ui.html`

## Ejemplo de Uso

### 1. Abrir Turno
```json
POST /v1/ventanilla/turnos/abrir
{
  "codigoCaja": "CAJ01",
  "codigoCajero": "USU01",
  "denominaciones": [
    {"billete": "CIEN", "cantidadBilletes": 10},
    {"billete": "CINCUENTA", "cantidadBilletes": 20},
    {"billete": "VEINTE", "cantidadBilletes": 50}
  ]
}
```

### 2. Procesar Transacción
```json
POST /v1/ventanilla/transacciones/procesar
{
  "codigoTurno": "CAJ01-USU01-20250109",
  "tipoTransaccion": "DEPOSITO",
  "denominaciones": [
    {"billete": "CIEN", "cantidadBilletes": 2},
    {"billete": "CINCUENTA", "cantidadBilletes": 4}
  ]
}
```

### 3. Cerrar Turno
```json
POST /v1/ventanilla/turnos/cerrar
{
  "codigoTurno": "CAJ01-USU01-20250109",
  "denominacionesFinales": [
    {"billete": "CIEN", "cantidadBilletes": 12},
    {"billete": "CINCUENTA", "cantidadBilletes": 24},
    {"billete": "VEINTE", "cantidadBilletes": 50}
  ]
}
```

## Contribución

1. Fork del repositorio
2. Crear rama para nueva funcionalidad
3. Hacer commit de los cambios
4. Push a la rama
5. Crear Pull Request

## Licencia

Este proyecto está bajo la Licencia MIT. 