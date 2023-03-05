package com.nacho.creditcards.services;

import com.nacho.creditcards.entities.CardBrand;
import com.nacho.creditcards.entities.CreditCard;
import com.nacho.creditcards.entities.Transaction;
import com.nacho.creditcards.exceptions.CreditCardNotFoundException;
import com.nacho.creditcards.exceptions.TransactionAmountInvalidException;
import com.nacho.creditcards.repositories.TransactionRepository;
import com.nacho.creditcards.services.interfaces.ITransactionService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    
    @Mock
    private CreditCardService creditCardService;

    @InjectMocks
    private ITransactionService transactionService = new TransactionService(transactionRepository);
    
    private CreditCard validCreditCard;
    private BigDecimal validAmount;
    private BigDecimal invalidAmount;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        validCreditCard = CreditCard.builder()
                .cardNumber("1234567890123456")
                .holderName("John Doe")
                .expirationDate(YearMonth.of(2025, 12))
                .brand(CardBrand.VISA)
                .build();

        validAmount = BigDecimal.valueOf(500.0);
        invalidAmount = BigDecimal.valueOf(1001.0);
    }

    @Test
    public void testCreateTransactionWithValidData() throws Exception {
        Transaction transaction = Transaction.builder()
                .creditCard(validCreditCard)
                .amount(validAmount)
                .dateTime(LocalDateTime.now())
                .build();

        when(transactionRepository.save(any())).thenReturn(transaction);

        Transaction createdTransaction = transactionService.createTransaction(validCreditCard, validAmount);

        assertThat(createdTransaction).isEqualTo(transaction);
        verify(transactionRepository, times(1)).save(any());
    }
    
    @Test
    public void testCreateTransactionWithInvalidAmount() {
        assertThrows(TransactionAmountInvalidException.class,
                () -> transactionService.createTransaction(validCreditCard, invalidAmount));
    }
    
    @Test
    public void testCalculateFeeForVisaTransaction() {
        LocalDateTime dateTime = LocalDateTime.of(2022, 12, 10, 14, 30);
        BigDecimal amount = BigDecimal.valueOf(100.0);
        CreditCard creditCard = CreditCard.builder()
                .id(1L)
                .brand(CardBrand.VISA)
                .cardNumber("1234567890123456")
                .holderName("John Doe")
                .expirationDate(YearMonth.of(2023, 12))
                .build();
        BigDecimal fee = transactionService.calculateFee(
                Transaction.builder()
                        .creditCard(creditCard)
                        .dateTime(dateTime)
                        .amount(amount)
                        .build()
        );
        BigDecimal expectedFee = BigDecimal.valueOf(22).divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP).multiply(amount);
        Assertions.assertEquals(expectedFee, fee);
    }
    
    @Test
    public void testCalculateFeeForNaraTransaction() {
        LocalDateTime dateTime = LocalDateTime.of(2022, 12, 10, 14, 30);
        BigDecimal amount = BigDecimal.valueOf(100.0);
        CreditCard creditCard = CreditCard.builder()
                .id(1L)
                .brand(CardBrand.NARA)
                .cardNumber("1234567890123456")
                .holderName("John Doe")
                .expirationDate(YearMonth.of(2023, 12))
                .build();
        double fee = transactionService.calculateFee(
                Transaction.builder()
                        .creditCard(creditCard)
                        .dateTime(dateTime)
                        .amount(amount)
                        .build()
        ).doubleValue();
        double expectedFee = 10.0 * 0.5 * 100.0;
        Assertions.assertEquals(expectedFee, fee, 0.01);
    }

    
    @Test
    public void testCalculateFeeForAmexTransaction() {
        LocalDateTime dateTime = LocalDateTime.of(2022, 12, 10, 14, 30);
        BigDecimal amount = BigDecimal.valueOf(100.0);
        CreditCard creditCard = CreditCard.builder()
                .id(1L)
                .brand(CardBrand.AMEX)
                .cardNumber("1234567890123456")
                .holderName("John Doe")
                .expirationDate(YearMonth.of(2023, 12))
                .build();
        Transaction transaction = Transaction.builder()
                .creditCard(creditCard)
                .dateTime(dateTime)
                .amount(amount)
                .build();
        BigDecimal fee = transactionService.calculateFee(transaction);
        BigDecimal expectedFee = BigDecimal.valueOf(12).multiply(BigDecimal.valueOf(0.1)).multiply(amount);
        Assertions.assertEquals(expectedFee, fee);
    }
    
    @Test
    public void testValidateTransaction_withValidData() throws Exception {
        CreditCard creditCard = CreditCard.builder()
                .id(1L)
                .cardNumber("1234 5678 9012 3456")
                .holderName("John Doe")
                .build();
        BigDecimal amount = BigDecimal.valueOf(500);
        when(creditCardService.getCreditCardById(creditCard.getId())).thenReturn(creditCard);

        // No exception should be thrown when credit card and amount are valid
        transactionService.validateTransaction(creditCard, amount);
    }
    
    @Test
    public void testValidateTransaction_withInvalidCreditCard() {
        CreditCard creditCard = null;
        BigDecimal amount = BigDecimal.valueOf(500);
        when(creditCardService.getCreditCardById(1L)).thenReturn(null);

        assertThrows(CreditCardNotFoundException.class, () -> {
            transactionService.validateTransaction(creditCard, amount);
        });
    }
    
    @Test
    public void testValidateTransaction_withInvalidAmount() {
        CreditCard creditCard = CreditCard.builder()
                .id(1L)
                .cardNumber("1234 5678 9012 3456")
                .holderName("John Doe")
                .build();
        BigDecimal amount = BigDecimal.valueOf(1001);
        when(creditCardService.getCreditCardById(creditCard.getId())).thenReturn(creditCard);

        assertThrows(TransactionAmountInvalidException.class, () -> {
            transactionService.validateTransaction(creditCard, amount);
        });
    }

    @Test
    public void testSimulateTransactionAndGetFee() {
        // Arrange
        int year = LocalDateTime.now().getYear() % 100;
        int month = LocalDateTime.now().getMonthValue();
        String brand = "visa";
        BigDecimal amount = BigDecimal.valueOf(100.0);
        BigDecimal expectedFee = BigDecimal.valueOf(year).divide(BigDecimal.valueOf(month), 2, RoundingMode.HALF_UP).multiply(amount);
        // Act
        BigDecimal fee = transactionService.simulateTransactionAndGetFee(brand, amount);
    
        // Assert
        assertNotNull(fee);
        assertTrue(fee instanceof BigDecimal);
        assertEquals(fee, expectedFee);
    }
}