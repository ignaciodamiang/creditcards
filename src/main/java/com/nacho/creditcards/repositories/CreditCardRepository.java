package com.nacho.creditcards.repositories;

import com.nacho.creditcards.entities.CardBrand;
import com.nacho.creditcards.entities.CreditCard;
import com.nacho.creditcards.repositories.interfaces.ICreditCardRepository;

import java.time.YearMonth;

import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardRepository extends ICreditCardRepository {

    CreditCard findByCardNumber(String cardNumber);

	CreditCard findByCardNumberAndHolderNameAndExpirationDateAndBrand(String cardNumber, String holderName,	YearMonth expirationDate, CardBrand brand);
}
