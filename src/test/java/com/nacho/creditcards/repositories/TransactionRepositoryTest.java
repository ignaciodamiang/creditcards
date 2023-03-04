package com.nacho.creditcards.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.nacho.creditcards.entities.CardBrand;
import com.nacho.creditcards.entities.CreditCard;
import com.nacho.creditcards.entities.Transaction;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    private CreditCard creditCard;

    @BeforeAll
    public void setUp() {
        creditCard = CreditCard.builder()
                .cardNumber("1234567890123456")
                .holderName("John Doe")
                .expirationDate(YearMonth.of(2025, 12))
                .brand(CardBrand.VISA)
                .build();
        creditCardRepository.save(creditCard);
    }

    @Test
    @Order(1)
    public void testGetAllTransactions() {
        Transaction transaction1 = Transaction.builder()
                .amount(new BigDecimal("500.00"))
                .dateTime(LocalDateTime.now())
                .creditCard(creditCard)
                .build();
        Transaction transaction2 = Transaction.builder()
                .amount(new BigDecimal("100.00"))
                .dateTime(LocalDateTime.now())
                .creditCard(creditCard)
                .build();
        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);

        List<Transaction> retrievedTransactions = transactionRepository.findAll();

        assertThat(retrievedTransactions.size()).isEqualTo(2);
        assertThat(retrievedTransactions.get(0).getId()).isEqualTo(transaction1.getId());
        assertThat(retrievedTransactions.get(1).getId()).isEqualTo(transaction2.getId());
    }

    @Test
    @Order(2)
    public void testGetTransactionById() {
        Transaction transaction = Transaction.builder()
                .amount(new BigDecimal("500.00"))
                .dateTime(LocalDateTime.now())
                .creditCard(creditCard)
                .build();
        transactionRepository.save(transaction);

        Transaction retrievedTransaction = transactionRepository.findById(transaction.getId()).orElse(null);

        assertThat(retrievedTransaction).isNotNull();
        assertThat(retrievedTransaction.getId()).isEqualTo(transaction.getId());
        assertThat(retrievedTransaction.getAmount()).isEqualTo(transaction.getAmount());
        assertThat(retrievedTransaction.getDateTime()).isCloseTo(transaction.getDateTime(), within(1, ChronoUnit.HOURS));
    }

    @Test
    @Order(3)
    public void testSaveTransaction() {
        Transaction transaction = Transaction.builder()
                .amount(new BigDecimal("500.00"))
                .dateTime(LocalDateTime.now())
                .creditCard(creditCard)
                .build();

        transactionRepository.save(transaction);

        assertThat(transaction.getId()).isNotNull();
    }

    @Test
    @Order(4)
    public void testDeleteTransaction() {
        Transaction transaction = Transaction.builder()
                .amount(new BigDecimal("500.00"))
                .dateTime(LocalDateTime.now())
                .creditCard(creditCard)
                .build();
        transactionRepository.save(transaction);

        transactionRepository.deleteById(transaction.getId());

        assertThat(transactionRepository.findById(transaction.getId())).isEmpty();
    }
}
