package com.nacho.creditcards.repositories;

import com.nacho.creditcards.entities.CreditCard;
import com.nacho.creditcards.repositories.interfaces.ICreditCardRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardRepository extends ICreditCardRepository {

    CreditCard findByCardNumber(String cardNumber);
}
