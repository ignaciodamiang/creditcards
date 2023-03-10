package com.nacho.creditcards.repositories;

import com.nacho.creditcards.entities.CreditCard;
import com.nacho.creditcards.entities.CardBrand;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.YearMonth;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
                .brand(CardBrand.VISA)
                .build(),
            CreditCard.builder()
                .cardNumber("2222 3333 4444 5555")
                .holderName("Jane Doe")
                .expirationDate(YearMonth.of(2023, 6))
                .brand(CardBrand.NARA)
                .build(),
            CreditCard.builder()
                .cardNumber("6666 7777 8888 9999")
                .holderName("Bob Smith")
                .expirationDate(YearMonth.of(2024, 9))
                .brand(CardBrand.AMEX)
                .build()
        ));
    }

    @Test
    @Order(1)
    public void testFindByCardNumber() {
        String cardNumber = "1234 5678 9012 3456";
        CreditCard creditCard = repository.findByCardNumber(cardNumber);
        assertThat(creditCard).isNotNull();
        assertThat(creditCard.getCardNumber()).isEqualTo(cardNumber);
        assertThat(creditCard.getHolderName()).isEqualTo("John Doe");
        assertThat(creditCard.getExpirationDate()).isEqualTo(YearMonth.of(2025, 12));
    }

    @Test
    @Order(2)
    public void testFindAll() {
        List<CreditCard> creditCards = repository.findAll();
        // assert that credit cards size is greater than 2
        assertThat(creditCards).hasSizeGreaterThan(2);
    }

@Test
@Order(2)
public void testSaveNewCreditCard() {
    CreditCard creditCard = CreditCard.builder()
            .cardNumber("7777 8888 9999 0000")
            .holderName("Alice Smith")
            .expirationDate(YearMonth.of(2026, 3))
            .brand(CardBrand.VISA)
            .build();
    repository.save(creditCard);
    CreditCard retrievedCreditCard = repository.findByCardNumber("7777 8888 9999 0000");
    assertThat(retrievedCreditCard).isNotNull();
    assertThat(retrievedCreditCard).isEqualTo(creditCard);
}


    @Test
    @Order(3)
    public void testUpdateCreditCard() {
        CreditCard creditCard = repository.findByCardNumber("1234 5678 9012 3456");
        creditCard.setHolderName("John Smith");
        repository.save(creditCard);
        CreditCard retrievedCreditCard = repository.findByCardNumber("1234 5678 9012 3456");
        assertThat(retrievedCreditCard).isNotNull();
        assertThat(retrievedCreditCard.getHolderName()).isEqualTo("John Smith");
    }

    @Test
    @Order(4)
    public void testDeleteCreditCard() {
        CreditCard creditCard = repository.findByCardNumber("2222 3333 4444 5555");
        repository.delete(creditCard);
        CreditCard retrievedCreditCard = repository.findByCardNumber("2222 3333 4444 5555");
        assertThat(retrievedCreditCard).isNull();
    }

    @Test
    @Order(5)
    void testFindByCardNumberAndHolderNameAndExpirationDateAndBrand() {
        CreditCard creditCard = CreditCard.builder()
                .cardNumber("1111 8888 9999 0000")
                .holderName("Alice Smith")
                .expirationDate(YearMonth.of(2026, 3))
                .brand(CardBrand.VISA)
                .build();
        repository.save(creditCard);

        CreditCard retrievedCreditCard = repository.findByCardNumberAndHolderNameAndExpirationDateAndBrand(
                "1111 8888 9999 0000", "Alice Smith", YearMonth.of(2026, 3), CardBrand.VISA);

        assertThat(retrievedCreditCard).isNotNull();
        assertThat(retrievedCreditCard).isEqualTo(creditCard);
    }
}