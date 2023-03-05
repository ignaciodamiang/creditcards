package com.nacho.creditcards.controllers;

import com.nacho.creditcards.entities.CardBrand;
import com.nacho.creditcards.entities.CreditCard;
import com.nacho.creditcards.exceptions.CreditCardNotValidException;
import com.nacho.creditcards.services.interfaces.ICreditCardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreditCardControllerTest {

    @Mock
    private ICreditCardService creditCardService;

    @InjectMocks
    CreditCardController controller;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testGetCreditCardById() {
        // Arrange
        CreditCard creditCard = CreditCard.builder()
                .id(1L)
                .cardNumber("1234 5678 9012 3456")
                .holderName("John Doe")
                .expirationDate(YearMonth.of(2025, 12))
                .brand(CardBrand.VISA)
                .build();
        when(creditCardService.getCreditCardById(creditCard.getId())).thenReturn(creditCard);

        controller.creditCardService = creditCardService;

        // Act
        ResponseEntity<CreditCard> response = controller.getCreditCardById(creditCard.getId());

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(creditCard);
    }
    
    @Test
    public void testGetAllCreditCards() {
        // Arrange
        List<CreditCard> creditCards = Arrays.asList(
            CreditCard.builder()
                .id(1L)
                .cardNumber("1234 5678 9012 3456")
                .holderName("John Doe")
                .expirationDate(YearMonth.of(2025, 12))
                .brand(CardBrand.VISA)
                .build(),
            CreditCard.builder()
                .id(2L)
                .cardNumber("5678 1234 9012 3456")
                .holderName("Jane Doe")
                .expirationDate(YearMonth.of(2023, 9))
                .brand(CardBrand.VISA)
                .build()
        );
        when(creditCardService.getAllCreditCards()).thenReturn(creditCards);

        controller.creditCardService = creditCardService;

        // Act
        ResponseEntity<List<CreditCard>> response = controller.getAllCreditCards();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(creditCards);
    }

    @Test
    public void testCreateCreditCard() throws CreditCardNotValidException {
        // Arrange
        CreditCard creditCard = CreditCard.builder()
                .id(1L)
                .cardNumber("1234 5678 9012 3456")
                .holderName("John Doe")
                .expirationDate(YearMonth.of(2025, 12))
                .brand(CardBrand.VISA)
                .build();
        
        CreditCard createdCreditCard = CreditCard.builder()
                .id(1L)
                .cardNumber("1234 5678 9012 3456")
                .holderName("John Doe")
                .expirationDate(YearMonth.of(2025, 12))
                .brand(CardBrand.VISA)
                .build();
        
        when(creditCardService.createCreditCard(creditCard)).thenReturn(createdCreditCard);
        
        controller.creditCardService = creditCardService;

        // Act
        ResponseEntity<CreditCard> response = controller.createCreditCard(creditCard);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(createdCreditCard);
    }

    @Test
    public void testUpdateCreditCard() {
        // Arrange
        Long id = 1L;
        CreditCard creditCardToUpdate = CreditCard.builder()
                .id(id)
                .cardNumber("1234 5678 9012 3456")
                .holderName("John Doe")
                .expirationDate(YearMonth.of(2025, 12))
                .brand(CardBrand.VISA)
                .build();
        CreditCard updatedCreditCard = CreditCard.builder()
                .id(id)
                .cardNumber("1234 5678 9012 3456")
                .holderName("Jane Doe")
                .expirationDate(YearMonth.of(2026, 1))
                .brand(CardBrand.AMEX)
                .build();

        when(creditCardService.updateCreditCard(id, creditCardToUpdate)).thenReturn(updatedCreditCard);

        controller.creditCardService = creditCardService;

        // Act
        ResponseEntity<CreditCard> response = controller.updateCreditCard(id, creditCardToUpdate);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(updatedCreditCard);
    }
    
    @Test
    public void testDeleteCreditCard() throws Exception {
        // Arrange
        Long id = 1L;
        doNothing().when(creditCardService).deleteCreditCard(id);

        controller.creditCardService = creditCardService;

        // Act
        ResponseEntity<Void> response = controller.deleteCreditCard(id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(creditCardService, times(1)).deleteCreditCard(id);
    }
    
    @Test
    public void testIsCreditCardValid() throws Exception {
        // Arrange
        Long id = 1L;
        CreditCard creditCard = CreditCard.builder()
                .id(id)
                .cardNumber("1234 5678 9012 3456")
                .holderName("John Doe")
                .expirationDate(YearMonth.of(2025, 12))
                .brand(CardBrand.VISA)
                .build();
        when(creditCardService.getCreditCardById(id)).thenReturn(creditCard);
        when(creditCardService.isValidCreditCard(creditCard)).thenReturn(true);

        controller.creditCardService = creditCardService;
        
        // Act
        ResponseEntity<String> response = controller.isCreditCardValid(id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Credit card is valid for operations");
        verify(creditCardService, times(1)).getCreditCardById(id);
        verify(creditCardService, times(1)).isValidCreditCard(creditCard);
    }
    
    @Test
    public void testIsCreditCardDistinct() {
        // Arrange
        Long id = 1L;
        CreditCard creditCard = new CreditCard();
        creditCard.setId(id);
        when(creditCardService.getCreditCardById(id)).thenReturn(creditCard);
        when(creditCardService.isCreditCardDistinct(creditCard)).thenReturn(true);

        controller.creditCardService = creditCardService;
        
        // Act
        ResponseEntity<Boolean> response = controller.isCreditCardDistinct(id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(true);
        verify(creditCardService, times(1)).getCreditCardById(id);
        verify(creditCardService, times(1)).isCreditCardDistinct(creditCard);
    }
}
