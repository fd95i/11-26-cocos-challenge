# Cocos Assets API

API REST para consultar el portfolio de un usuario, buscar activos y enviar órdenes.

**Base URL:** `http://localhost:8080/cocos-assets-api`

## Índice

- [Endpoints](#endpoints)
- [Portfolio](#1-portfolio)
- [Buscar activos](#2-buscar-activos)
- [Crear orden](#3-crear-orden)

---

## Endpoints

| Método | Ruta              | Descripción                              |
|--------|-------------------|------------------------------------------|
| `GET`  | `/portfolio/{id}` | Cartera de un usuario                    |
| `GET`  | `/assets`         | Búsqueda de activos por ticker y/o nombre |
| `POST` | `/orders`         | Alta de una orden                        |

---

## 1. Portfolio

Devuelve el valor total de la cuenta, los pesos disponibles y el detalle de cada posición.

```
GET /portfolio/{id}
```

### Parámetros de ruta

| Nombre | Tipo    | Descripción      |
|--------|---------|------------------|
| `id`   | integer | ID del usuario   |

### Ejemplo

```bash
curl http://localhost:8080/cocos-assets-api/portfolio/2
```

### Respuesta `200 OK`

```json
{
  "userId": 2,
  "totalValue": 4800000.00,
  "available": 4795276.00,
  "assets": [
    {
      "instrumentId": 27,
      "type": "ACCIONES",
      "ticker": "AUSO",
      "name": "Auto Soft",
      "size": 1,
      "totalValue": 624.00,
      "totalReturnRate": 0.00,
      "dailyReturnRate": 0.16
    },
    {
      "instrumentId": 61,
      "type": "ACCIONES",
      "ticker": "ALUA",
      "name": "Aluar",
      "size": 10,
      "totalValue": 4100.00,
      "totalReturnRate": 0.00,
      "dailyReturnRate": -0.49
    }
  ]
}
```

### Campos

| Campo                      | Descripción                                                              |
|----------------------------|--------------------------------------------------------------------------|
| `totalValue`               | Pesos disponibles + valor de mercado de todas las posiciones             |
| `available`                | Saldo en ARS disponible para operar                                      |
| `assets[].size`            | Cantidad de acciones en cartera                                          |
| `assets[].totalValue`      | `size × close` más reciente                                              |
| `assets[].totalReturnRate` | Rendimiento total (%) contra el precio promedio de adquisición           |
| `assets[].dailyReturnRate` | Variación diaria (%), `(close − previousClose) / previousClose × 100`    |

---

## 2. Buscar activos

Busca instrumentos por ticker, por nombre o por ambos. Todos los filtros son opcionales.

```
GET /assets
GET /assets?ticker={ticker}
GET /assets?name={name}
GET /assets?name={name}&ticker={ticker}
```

### Query params

| Nombre   | Tipo   | Requerido | Descripción                                     |
|----------|--------|-----------|-------------------------------------------------|
| `ticker` | string | No        | Ticker completo o parcial (ej.: `bbar`)         |
| `name`   | string | No        | Nombre completo o parcial (ej.: `ban`)          |

### Ejemplos

```bash
# Todos los activos
curl "http://localhost:8080/cocos-assets-api/assets"

# Por ticker
curl "http://localhost:8080/cocos-assets-api/assets?ticker=bbar"

# Por nombre y ticker
curl "http://localhost:8080/cocos-assets-api/assets?name=ban&ticker=bbar"
```

### Respuesta `200 OK`

```json
{
  "limit": 10,
  "offset": 0,
  "count": 1,
  "totalCount": 1,
  "data": [
    {
      "id": 22,
      "ticker": "BBAR",
      "name": "Banco Frances",
      "type": "ACCIONES"
    }
  ]
}
```

### Campos de paginación

| Campo        | Descripción                                   |
|--------------|-----------------------------------------------|
| `limit`      | Máximo de resultados por página               |
| `offset`     | Cantidad de resultados salteados              |
| `count`      | Resultados devueltos en esta respuesta        |
| `totalCount` | Total de resultados que cumplen el filtro     |

> Los resultados se devuelven ordenados alfabéticamente.

---

## 3. Crear orden

Registra una orden de compra, venta o transferencia de pesos.

```
POST /orders
Content-Type: application/json
```

### Body

| Campo          | Tipo    | Requerido        | Descripción                                                   |
|----------------|---------|------------------|---------------------------------------------------------------|
| `instrumentId` | integer | Sí               | Activo a operar. Para transferencias de pesos, usar `66`      |
| `userId`       | integer | Sí               | Usuario dueño de la orden                                     |
| `size`         | integer | Sí               | Cantidad de acciones (o de pesos en `CASH_IN`/`CASH_OUT`)     |
| `side`         | string  | Sí               | `BUY`, `SELL`, `CASH_IN` o `CASH_OUT`                         |
| `type`         | string  | Sí               | `MARKET` o `LIMIT`                                            |
| `price`        | number  | Solo en `LIMIT`  | Precio límite. En `MARKET` se ignora y se usa el `close`      |

### Ejemplos de request

<details>
<summary><b>Compra a mercado</b></summary>

```json
{
  "instrumentId": 31,
  "userId": 2,
  "size": 10,
  "side": "BUY",
  "type": "MARKET"
}
```
</details>

<details>
<summary><b>Venta a mercado</b></summary>

```json
{
  "instrumentId": 31,
  "userId": 2,
  "size": 10,
  "side": "SELL",
  "type": "MARKET"
}
```
</details>

<details>
<summary><b>Compra con precio límite</b></summary>

```json
{
  "instrumentId": 31,
  "userId": 2,
  "price": 15.5,
  "size": 10,
  "side": "BUY",
  "type": "LIMIT"
}
```
</details>

<details>
<summary><b>Ingreso de pesos</b></summary>

```json
{
  "instrumentId": 66,
  "userId": 2,
  "size": 100000,
  "side": "CASH_IN",
  "type": "MARKET"
}
```
</details>

<details>
<summary><b>Retiro de pesos</b></summary>

```json
{
  "instrumentId": 66,
  "userId": 2,
  "size": 100000,
  "side": "CASH_OUT",
  "type": "MARKET"
}
```
</details>

### Ejemplo con cURL

```bash
curl -X POST http://localhost:8080/cocos-assets-api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "instrumentId": 31,
    "userId": 2,
    "size": 10,
    "side": "BUY",
    "type": "MARKET"
  }'
```

### Respuesta `200 OK`

```json
{
  "id": 29,
  "instrumentId": 66,
  "userId": 2,
  "size": 100000,
  "price": 1.00,
  "side": "CASH_OUT",
  "status": "FILLED",
  "type": "MARKET",
  "dateTime": "2026-11-19T03:49:03.701Z"
}
```