package com.nacho.creditcards.repositories;

import com.nacho.creditcards.entities.CreditCard;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.YearMonth;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreditCardRepositoryTest {

    @Autowired
    private CreditCardRepository repository;

    @BeforeAll
    public void setUp() {
        repository.saveAll(List.of(
            CreditCard.builder()
                .cardNumber("1234 5678 9012 3456")
                .holderName("John Doe")
                .expirationDate(YearMonth.of(2025, 12))
                .build(),
            CreditCard.builder()
                .cardNumber("2222 3333 4444 5555")
                .holderName("Jane Doe")
                .expirationDate(YearMonth.of(2023, 6))
                .build(),
            CreditCard.builder()
                .cardNumber("6666 7777 8888 9999")
                .holderName("Bob Smith")
                .expirationDate(YearMonth.of(2024, 9))
                .build()
        ));
    }

    @Test
    public void testFindByCardNumber() {
        String cardNumber = "1234 5678 9012 3456";
        CreditCard creditCard = repository.findByCardNumber(cardNumber);
        assertThat(creditCard).isNotNull();
        assertThat(creditCard.getCardNumber()).isEqualTo(cardNumber);
        assertThat(creditCard.getHolderName()).isEqualTo("John Doe");
        assertThat(creditCard.getExpirationDate()).isEqualTo(YearMonth.of(2025, 12));
    }

    @Test
    public void testFindAll() {
        List<CreditCard> creditCards = repository.findAll();
        assertThat(creditCards).hasSize(3);
    }
}
