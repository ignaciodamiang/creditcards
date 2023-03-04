package com.nacho.creditcards.repositories.interfaces;

import com.nacho.creditcards.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction, Long> {
	
}
