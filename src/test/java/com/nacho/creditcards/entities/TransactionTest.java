package com.nacho.creditcards.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;

public class TransactionTest {

    @Test
    public void testCreateTransaction() {
        CreditCard creditCard = CreditCard.builder()
                .cardNumber("1234 5678 9012 3456")
                .holderName("John Doe")
                .expirationDate(YearMonth.of(2025, 12))
                .brand(CardBrand.VISA)
                .build();

        BigDecimal amount = new BigDecimal("500.50");

        Transaction transaction = Transaction.builder()
                .creditCard(creditCard)
                .amount(amount)
                .dateTime(LocalDateTime.now())
                .build();

        Assertions.assertEquals(creditCard, transaction.getCreditCard());
        Assertions.assertEquals(amount, transaction.getAmount());
        Assertions.assertNotNull(transaction.getDateTime());
    }

}
