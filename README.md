Простое веб-приложение на Spring Boot (для дизайна используется Thymeleaf). Пользователь вводит сумму и нажимает
кнопку "Оплатить". После этого происходит вызов метода createPayment для
создания депозита:

POST https://engine-sandbox.pay.tech/api/v1/payments

Authorization: Bearer cAmmvalAikARkB81fgxgMtnMbEdNbuWa

Content-Type: application/json

{

    "paymentType": "DEPOSIT",
    "amount": {введенная пользователем сумма},
    "currency": "EUR",
    "customer": {"referenceId": "abc"}
}

В случае успеха пользователь редиректится на полученный в ответе redirectUrl. В случае
ошибки - показывается страница с ошибкой.

Запускается на http://localhost:8080/add-payment, в случае ошибки http://localhost:8080/error-payment