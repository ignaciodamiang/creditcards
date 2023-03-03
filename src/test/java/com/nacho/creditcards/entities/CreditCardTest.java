package com.nacho.creditcards.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.YearMonth;

import org.junit.jupiter.api.Test;

public class CreditCardTest {
    @Test
    public void testCreditCard() {
        CreditCard creditCard = CreditCard.builder()
                .cardNumber("1234 5678 9012 3456")
                .holderName("John Doe")
                .expirationDate(YearMonth.of(2025, 12))
                .build();
        assertEquals("1234 5678 9012 3456", creditCard.getCardNumber());
        assertEquals("John Doe", creditCard.getHolderName());
        assertEquals(YearMonth.of(2025, 12), creditCard.getExpirationDate());
    }
    
}
