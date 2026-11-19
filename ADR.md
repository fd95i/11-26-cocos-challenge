# Decisiones de diseño

## Arquitectura

- Se implementa el API con **Arquitectura Hexagonal**.

## Cálculo de rendimiento

- Se usa el **precio promedio de adquisición** para calcular el rendimiento contra el valor de cierre (`close`).
- El rendimiento se calcula en el **backend**, para no ensuciar la query.

## Estrategia de consultas

- Se opta por queries más complejas para evitar procesamiento en el backend (salvo en el caso del rendimiento).

## Listado y búsqueda de activos

- El resultado se **ordena alfabéticamente**; no se usan campos de `sort`.
- Se considera válida la búsqueda de activos **sin especificar `name` ni `ticker`**.

## Creación de órdenes

- Para respetar el alcance del ejercicio, se retorna **`200 OK`** con la orden en el body de la respuesta, en lugar de **`201 Created`** y una URL del tipo `/orders/{id}`.
- Para simplificar el proceso, se asume que:
  - el `instrumentId` de los pesos es siempre **66**;
  - es el **único** instrumento de tipo `MONEDA`;
  - por eso no se consulta al crear la orden y queda **hardcodeado**.