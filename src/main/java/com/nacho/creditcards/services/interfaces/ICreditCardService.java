package com.nacho.creditcards.services.interfaces;

import com.nacho.creditcards.entities.CreditCard;

import java.util.List;

public interface ICreditCardService {
    CreditCard createCreditCard(CreditCard creditCard);
    CreditCard getCreditCardById(Long id);
    List<CreditCard> getAllCreditCards();
    CreditCard updateCreditCard(Long id, CreditCard creditCard);
    void deleteCreditCard(Long id);
}
