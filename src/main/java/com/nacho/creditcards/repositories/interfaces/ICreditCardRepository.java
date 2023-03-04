package com.nacho.creditcards.repositories.interfaces;

import com.nacho.creditcards.entities.CardBrand;
import com.nacho.creditcards.entities.CreditCard;

import java.time.YearMonth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICreditCardRepository extends JpaRepository<CreditCard, Long> {
    CreditCard findByCardNumberAndHolderNameAndExpirationDateAndBrand(String cardNumber, String holderName, YearMonth expirationDate, CardBrand brand);
}
