# Guía de Pruebas en Postman - Análisis Crediticio

## Configuración Inicial

**Base URL**: `http://localhost:8080`

## 📋 Colección de APIs

### 1. **Evaluación Automática de Crédito**

**Endpoint**: `POST /v1/risk/auto-evaluation`

**Headers**:
```
Content-Type: application/json
```

**Body (JSON)**:
```json
{
  "idSolicitud": 12345,
  "ingresos": 3000.00,
  "egresos": 2000.00,
  "cuotaMensual": 250.00
}
```

**Casos de Prueba**:

#### ✅ Caso 1: Aprobación Automática
```json
{
  "idSolicitud": 1001,
  "ingresos": 5000.00,
  "egresos": 2000.00,
  "cuotaMensual": 300.00
}
```
**Resultado Esperado**: `APROBADO` (capacidad de pago suficiente + score alto simulado)

#### ❌ Caso 2: Rechazo por Capacidad de Pago
```json
{
  "idSolicitud": 1002,
  "ingresos": 2000.00,
  "egresos": 1800.00,
  "cuotaMensual": 250.00
}
```
**Resultado Esperado**: `RECHAZADO` (capacidad de pago: (2000-1800)*0.3 = 60 < 250)

#### ⚠️ Caso 3: Revisión Manual
```json
{
  "idSolicitud": 1003,
  "ingresos": 4000.00,
  "egresos": 2500.00,
  "cuotaMensual": 400.00
}
```
**Resultado Esperado**: `REVISION_MANUAL` (score intermedio simulado)

---

### 2. **Revisión del Analista**

**Endpoint**: `POST /v1/risk/analyst-review`

**Headers**:
```
Content-Type: application/json
```

**Body (JSON)**:
```json
{
  "idEvaluacion": 1,
  "decisionFinal": "APROBADO",
  "justificacion": "Cliente con excelente historial crediticio. Ingresos comprobables y estables.",
  "idUsuario": 101
}
```

**Casos de Prueba**:

#### ✅ Caso 1: Analista Aprueba
```json
{
  "idEvaluacion": 1,
  "decisionFinal": "APROBADO",
  "justificacion": "Cliente presenta garantías adicionales que compensan el riesgo inicial.",
  "idUsuario": 101
}
```

#### ❌ Caso 2: Analista Rechaza
```json
{
  "idEvaluacion": 2,
  "decisionFinal": "RECHAZADO",
  "justificacion": "Historial crediticio inconsistente. Múltiples atrasos en últimos 6 meses.",
  "idUsuario": 102
}
```

---

### 3. **Consultar Evaluación por Solicitud**

**Endpoint**: `GET /v1/risk/evaluations/{idSolicitud}`

**Ejemplos**:
- `GET /v1/risk/evaluations/1001`
- `GET /v1/risk/evaluations/1002`
- `GET /v1/risk/evaluations/1003`

---

## 🔧 Lógica de Negocio Implementada

### **Módulo 1: Consulta Buró**
- Consulta solo si la solicitud está pendiente
- Simula respuesta del buró externo
- Implementa reintentos automáticos (máximo 3 intentos)
- Estados: `PENDIENTE`, `COMPLETADA`, `FALLIDA`, `REINTENTANDO`

### **Módulo 2: Informe del Buró**
- Procesa la respuesta JSON del buró
- Extrae información relevante (score, deudas, montos)
- Valida formato y completitud de datos

### **Módulo 3: Evaluación Interna**
#### Cálculo de Capacidad de Pago:
```
capacidadPago = (ingresos - egresos) * 0.30
```

#### Reglas del Motor de Score:
1. **Score > 750 + Sin deudas morosas** → `APROBADO`
2. **Score 600-750** → `REVISION_MANUAL`
3. **Score < 600 o Con deudas** → `RECHAZADO`
4. **Capacidad de pago < cuota mensual** → `RECHAZADO`

### **Módulo 4: Revisión del Analista**
- Permite override de decisión automática
- Requiere justificación obligatoria
- Registra auditoría (usuario, fecha, decisión)

---

## 📊 Respuestas Esperadas

### Evaluación Exitosa:
```json
{
  "idEvaluacion": 1,
  "idSolicitud": 1001,
  "resultadoAutomatico": "APROBADO",
  "decisionFinalAnalista": null,
  "scoreInternoCalculado": 800,
  "observacionesMotorReglas": "Score excelente y sin deudas morosas. Aprobación automática.",
  "justificacionAnalista": null,
  "fechaEvaluacion": "2024-01-15T10:30:00"
}
```

### Error de Validación (400):
```json
{
  "timestamp": "2024-01-15T10:30:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Los ingresos son obligatorios"
}
```

### Recurso No Encontrado (404):
```json
{
  "timestamp": "2024-01-15T10:30:00.000+00:00",
  "status": 404,
  "error": "Not Found"
}
```

---

## 🔄 Flujo de Pruebas Completo

1. **Ejecutar Evaluación Automática** (`POST /v1/risk/auto-evaluation`)
2. **Obtener ID de Evaluación** de la respuesta
3. **Si resultado es REVISION_MANUAL**, ejecutar **Revisión del Analista** (`POST /v1/risk/analyst-review`)
4. **Consultar estado final** (`GET /v1/risk/evaluations/{idSolicitud}`)

---

## 🚀 Comandos para Iniciar el Microservicio

```bash
# Compilar
mvn clean compile

# Ejecutar tests
mvn test

# Iniciar aplicación
mvn spring-boot:run
```

**Swagger UI**: http://localhost:8080/swagger-ui.html
**API Docs**: http://localhost:8080/api-docs