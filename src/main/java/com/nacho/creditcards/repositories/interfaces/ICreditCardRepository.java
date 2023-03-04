package com.nacho.creditcards.repositories.interfaces;

import com.nacho.creditcards.entities.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICreditCardRepository extends JpaRepository<CreditCard, Long> {
}
