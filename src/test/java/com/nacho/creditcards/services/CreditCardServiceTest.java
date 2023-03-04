package com.nacho.creditcards.services;

import com.nacho.creditcards.entities.CreditCard;
import com.nacho.creditcards.repositories.CreditCardRepository;
import com.nacho.creditcards.services.interfaces.ICreditCardService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class CreditCardServiceTest {

    @Mock
    private CreditCardRepository creditCardRepository;

    @InjectMocks
    private ICreditCardService creditCardService = new CreditCardService();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCreditCard() {
        CreditCard creditCard = CreditCard.builder()
                .cardNumber("1234567890123456")
                .holderName("John Doe")
                .expirationDate(YearMonth.of(2023, 12))
                .build();
        when(creditCardRepository.save(creditCard)).thenReturn(creditCard);
        CreditCard savedCreditCard = creditCardService.createCreditCard(creditCard);
        verify(creditCardRepository, times(1)).save(creditCard);
        Assertions.assertEquals(creditCard, savedCreditCard);
    }

    @Test
    public void testGetCreditCardById() {
        Long id = 1L;
        CreditCard creditCard = CreditCard.builder()
                .id(id)
                .cardNumber("1234567890123456")
                .holderName("John Doe")
                .expirationDate(YearMonth.of(2023, 12))
                .build();
        when(creditCardRepository.findById(id)).thenReturn(Optional.of(creditCard));
        CreditCard retrievedCreditCard = creditCardService.getCreditCardById(id);
        verify(creditCardRepository, times(1)).findById(id);
        Assertions.assertEquals(creditCard, retrievedCreditCard);
    }

    @Test
    public void testGetCreditCardByIdNotFound() {
        Long id = 1L;
        when(creditCardRepository.findById(id)).thenReturn(Optional.empty());
        CreditCard retrievedCreditCard = creditCardService.getCreditCardById(id);
        verify(creditCardRepository, times(1)).findById(id);
        Assertions.assertNull(retrievedCreditCard);
    }

    @Test
    public void testGetAllCreditCards() {
        CreditCard creditCard1 = CreditCard.builder()
                .id(1L)
                .cardNumber("1234567890123456")
                .holderName("John Doe")
                .expirationDate(YearMonth.of(2023, 12))
                .build();
        CreditCard creditCard2 = CreditCard.builder()
                .id(2L)
                .cardNumber("2345678901234567")
                .holderName("Jane Doe")
                .expirationDate(YearMonth.of(2024, 3))
                .build();
        List<CreditCard> creditCards = new ArrayList<>();
        creditCards.add(creditCard1);
        creditCards.add(creditCard2);
        when(creditCardRepository.findAll()).thenReturn(creditCards);
        List<CreditCard> retrievedCreditCards = creditCardService.getAllCreditCards();
        verify(creditCardRepository, times(1)).findAll();
        Assertions.assertEquals(2, retrievedCreditCards.size());
        Assertions.assertEquals(creditCard1, retrievedCreditCards.get(0));
        Assertions.assertEquals(creditCard2, retrievedCreditCards.get(1));
    }

    @Test
    public void testUpdateCreditCard() {
        Long id = 1L;
        CreditCard existingCreditCard = CreditCard.builder()
                .id(id)
                .cardNumber("1234567890123456")
                .holderName("John Doe")
                .expirationDate(YearMonth.of(2023, 12))
                .build();
        CreditCard newCreditCard = CreditCard.builder()
                .cardNumber("2345678901234567")
                .holderName("Jane Doe")
                .expirationDate(YearMonth.of(2024, 3))
                .build();
        when(creditCardRepository.findById(id)).thenReturn(Optional.of(existingCreditCard));
        when(creditCardRepository.save(existingCreditCard)).thenReturn(existingCreditCard);
        CreditCard updatedCreditCard = creditCardService.updateCreditCard(id, newCreditCard);
        verify(creditCardRepository, times(1)).findById(id);
        verify(creditCardRepository, times(1)).save(existingCreditCard);
        Assertions.assertEquals("2345678901234567", existingCreditCard.getCardNumber());
        Assertions.assertEquals("Jane Doe", existingCreditCard.getHolderName());
        Assertions.assertEquals(YearMonth.of(2024, 3), existingCreditCard.getExpirationDate());
        Assertions.assertEquals(existingCreditCard, updatedCreditCard);
    }

    @Test
    public void testUpdateCreditCardNotFound() {
        Long id = 1L;
        CreditCard newCreditCard = CreditCard.builder()
                .cardNumber("2345678901234567")
                .holderName("Jane Doe")
                .expirationDate(YearMonth.of(2024, 3))
                .build();
        when(creditCardRepository.findById(id)).thenReturn(Optional.empty());
        CreditCard updatedCreditCard = creditCardService.updateCreditCard(id, newCreditCard);
        verify(creditCardRepository, times(1)).findById(id);
        verify(creditCardRepository, never()).save(any());
        Assertions.assertNull(updatedCreditCard);
    }

    @Test
    public void testDeleteCreditCard() {
        Long id = 1L;
        doNothing().when(creditCardRepository).deleteById(id);
        creditCardService.deleteCreditCard(id);
        verify(creditCardRepository, times(1)).deleteById(id);
    }
}