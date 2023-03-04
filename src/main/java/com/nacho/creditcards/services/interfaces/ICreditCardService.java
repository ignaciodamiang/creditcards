package com.nacho.creditcards.services.interfaces;

import com.nacho.creditcards.entities.CardBrand;
import com.nacho.creditcards.entities.CreditCard;

import java.time.YearMonth;
import java.util.List;

public interface ICreditCardService {
    CreditCard createCreditCard(CreditCard creditCard);
    CreditCard getCreditCardById(Long id);
    List<CreditCard> getAllCreditCards();
    CreditCard updateCreditCard(Long id, CreditCard creditCard);
    void deleteCreditCard(Long id);
	CreditCard findByCardNumberAndHolderNameAndExpirationDateAndBrand(String cardNumber, String holderName, YearMonth expirationDate, CardBrand brand);
	boolean isExpirationDateValid(CreditCard creditCard);
}
