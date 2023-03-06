# Breve explicación del projecto creditcards (Backend)

## Endpoints tarjeta de crédito

GET /credit-cards: devuelve una lista de todas las tarjetas de crédito en la base de datos.

GET /credit-cards/{id}: devuelve una tarjeta de crédito basada en su id.

POST /credit-cards: crea una nueva tarjeta de crédito. El cuerpo de la solicitud debe contener los detalles de la tarjeta de crédito que se está creando.
Ej:

```json
{
    "cardNumber": "1111222233334444",
    "holderName": "Bob Smith",
    "expirationDate": "2023-09",
    "brand": "AMEX"
}
```

PUT /credit-cards/{id}: actualiza una tarjeta de crédito existente basada en su id. El cuerpo de la solicitud debe contener los detalles actualizados de la tarjeta de crédito.

DELETE /credit-cards/{id}: elimina una tarjeta de crédito existente basada en su id.

GET /creditcards/{id}/valid: verifica si una tarjeta de crédito existente en la base de datos es válida. Retorna una respuesta con un mensaje que indica si la tarjeta de crédito es válida para operaciones o no.
Ej respuesta:

```
Credit card is valid for operations
```

POST /creditcards/is-valid: verifica si una tarjeta de crédito es válida para operaciones. Toma los detalles de la tarjeta de crédito en el cuerpo de la solicitud. Devuelve una respuesta indicando si es valida o no. 
Ej resrespuesta:

```
Credit card is not valid for operations.
```

POST /creditcards/is-distinct: Este endpoint permite verificar si los detalles de una tarjeta de crédito son distintos de los detalles de todas las demás tarjetas de crédito almacenadas en la base de datos. Toma los detalles de la tarjeta de crédito en el cuerpo de la solicitud como un objeto JSON.
Ej respuesta:

```json
isDistinct: true
```

## Endpoints transacciones

GET /transactions/{id}: Obtiene una transacción por su id y devuelve un ResponseEntity con el objeto Transaction si existe, de lo contrario devuelve una respuesta con estado 404 (Not Found).

GET /transactions: Obtiene todas las transacciones y devuelve un ResponseEntity con una lista de objetos Transaction si existen, de lo contrario devuelve una respuesta con estado 204 (No Content).

POST /transactions: Crea una nueva transacción y devuelve un ResponseEntity con el objeto Transaction creado y un estado 201 (Created).
Ej:
```json
{
    "id": 1,
    "creditCard": {
        "id": 1,
        "cardNumber": "1111222233334444",
        "holderName": "Bob Smith",
        "expirationDate": "2023-09",
        "brand": "AMEX"
    },
    "dateTime": "2023-03-06T09:10:48.147309287",
    "amount": 100.0
}
```

PUT /transactions/{id}: Actualiza una transacción existente y devuelve un ResponseEntity con el objeto Transaction actualizado si existe, de lo contrario devuelve una respuesta con estado 404 (Not Found).

DELETE /transactions/{id}: Elimina una transacción existente y devuelve un ResponseEntity con estado 204 (No Content).

POST /transactions/validate: Valida una transacción y devuelve un ResponseEntity con un mensaje de respuesta y un estado 200 (OK) si es válida, o una respuesta con estado 400 (Bad Request) y un mensaje de error si no lo es.

GET /transactions/fee?brand={brand}&amount={amount}: Obtiene la comisión de una transacción y devuelve un ResponseEntity con un objeto BigDecimal que representa la comisión y un estado 200 (OK). El endpoint recibe dos parámetros, 'brand' que indica la marca de la tarjeta de crédito y 'amount' que indica el monto de la transacción.
Ej:

```
GET /transactions/fee?brand=visa&amount=100.00
```

## Arquitectura de aplicación

La aplicación está compuesta por capas de servicios, repositorios, controladores y entidades. 
Se conecta a una base de datos mysql para realizar las operaciones.
Hay tests para todas las capas y entidades.
Se puede levantar el projecto con docker composer con el comando `docker compose up` despues de haber compilado la aplicación.
