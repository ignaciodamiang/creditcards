package com.nacho.creditcards.services;

import com.nacho.creditcards.entities.CardBrand;
import com.nacho.creditcards.entities.CreditCard;
import com.nacho.creditcards.entities.Transaction;
import com.nacho.creditcards.exceptions.TransactionAmountInvalidException;
import com.nacho.creditcards.repositories.TransactionRepository;
import com.nacho.creditcards.services.interfaces.ITransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

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

}
