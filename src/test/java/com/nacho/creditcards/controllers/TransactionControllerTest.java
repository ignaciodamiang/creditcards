package com.nacho.creditcards.controllers;

import com.nacho.creditcards.entities.CardBrand;
import com.nacho.creditcards.entities.CreditCard;
import com.nacho.creditcards.entities.Transaction;
import com.nacho.creditcards.services.interfaces.ITransactionService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {
    
    @Mock
    private ITransactionService transactionService;
    
    @InjectMocks
    private TransactionController controller;
    
    @Test
    public void testGetTransactionById() throws Exception {
        // Arrange
        Long id = 1L;
        Transaction transaction = new Transaction();
        transaction.setId(id);
        when(transactionService.getTransactionById(id)).thenReturn(transaction);

        // Act
        ResponseEntity<Transaction> response = controller.getTransactionById(id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(transaction);
        verify(transactionService, times(1)).getTransactionById(id);
    }
    
    @Test
    public void testGetAllTransactions() {
        // Arrange
        List<Transaction> transactions = Arrays.asList(
                Transaction.builder()
                        .id(1L)
                        .amount(BigDecimal.valueOf(100.00))
                        .creditCard(CreditCard.builder().id(1L).build())
                        .build(),
                Transaction.builder()
                        .id(2L)
                        .amount(BigDecimal.valueOf(50.00))
                        .creditCard(CreditCard.builder().id(2L).build())
                        .build()
        );
        when(transactionService.getAllTransactions()).thenReturn(transactions);

        // Act
        ResponseEntity<List<Transaction>> response = controller.getAllTransactions();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(transactions);
    }
    
    @Test
    public void testCreateTransaction() throws Exception {
        // Arrange
        BigDecimal amount = BigDecimal.valueOf(100.00);
        CreditCard creditCard = CreditCard.builder()
                .id(1L)
                .cardNumber("1234 5678 9012 3456")
                .holderName("John Doe")
                .expirationDate(YearMonth.of(2025, 12))
                .brand(CardBrand.VISA)
                .build();
        Transaction transaction = Transaction.builder()
                .id(1L)
                .amount(amount)
                .creditCard(creditCard)
                .build();
        Transaction createdTransaction = Transaction.builder()
                .id(1L)
                .amount(amount)
                .creditCard(creditCard)
                .build();
        
        when(transactionService.createTransaction(creditCard, amount)).thenReturn(createdTransaction);

        // Act
        ResponseEntity<Transaction> response = controller.createTransaction(transaction);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(createdTransaction);
        verify(transactionService, times(1)).createTransaction(creditCard, amount);
    }

    @Test
    public void testUpdateTransaction() throws Exception {
        // Arrange
        Long id = 1L;
        Transaction transactionToUpdate = Transaction.builder()
                .id(id)
                .amount(BigDecimal.valueOf(100.00))
                .creditCard(CreditCard.builder().id(1L).build())
                .build();
        Transaction updatedTransaction = Transaction.builder()
                .id(id)
                .amount(BigDecimal.valueOf(150.00))
                .creditCard(CreditCard.builder().id(1L).build())
                .build();

        when(transactionService.updateTransaction(id, transactionToUpdate)).thenReturn(updatedTransaction);

        // Act
        ResponseEntity<Transaction> response = controller.updateTransaction(id, transactionToUpdate);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(updatedTransaction);
        verify(transactionService, times(1)).updateTransaction(id, transactionToUpdate);
    }

    @Test
    public void testDeleteTransaction() throws Exception {
        // Arrange
        Long id = 1L;
        doNothing().when(transactionService).deleteTransaction(id);

        // Act
        ResponseEntity<Void> response = controller.deleteTransaction(id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(transactionService, times(1)).deleteTransaction(id);
    }
    
    @Test
    public void testValidateTransaction() {
        // Arrange
        CreditCard creditCard = CreditCard.builder()
                .id(1L)
                .cardNumber("1234 5678 9012 3456")
                .holderName("John Doe")
                .expirationDate(YearMonth.of(2025, 12))
                .brand(CardBrand.VISA)
                .build();
        
        BigDecimal amount = BigDecimal.valueOf(100.00);
        doNothing().when(transactionService).validateTransaction(creditCard, amount);

        // Act
        ResponseEntity<String> response = controller.validateTransaction(Transaction.builder()
                .amount(amount)
                .creditCard(creditCard)
                .build());

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Transaction is valid");
        verify(transactionService, times(1)).validateTransaction(creditCard, amount);
    }

}
