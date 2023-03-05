package com.nacho.creditcards.services.interfaces;

import java.math.BigDecimal;
import java.util.List;

import com.nacho.creditcards.entities.CreditCard;
import com.nacho.creditcards.entities.Transaction;
import com.nacho.creditcards.exceptions.CreditCardNotFoundException;
import com.nacho.creditcards.exceptions.CreditCardNotValidException;
import com.nacho.creditcards.exceptions.TransactionAmountInvalidException;
import com.nacho.creditcards.exceptions.TransactionNotFoundException;

public interface ITransactionService {

    Transaction createTransaction(CreditCard creditCard, BigDecimal amount)
            throws CreditCardNotFoundException, CreditCardNotValidException, TransactionAmountInvalidException;

    Transaction getTransactionById(Long id) throws TransactionNotFoundException;

    List<Transaction> getAllTransactions();

    void deleteTransaction(Long id) throws TransactionNotFoundException;

	BigDecimal calculateFee(Transaction transaction);

	void validateTransaction(CreditCard creditCard, BigDecimal amount);

	Transaction updateTransaction(Long id, Transaction transaction);

	BigDecimal simulateTransactionAndGetFee(String brand, BigDecimal amount);
}
