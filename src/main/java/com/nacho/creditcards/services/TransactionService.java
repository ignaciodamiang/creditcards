package com.nacho.creditcards.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nacho.creditcards.entities.CreditCard;
import com.nacho.creditcards.entities.Transaction;
import com.nacho.creditcards.exceptions.CreditCardNotFoundException;
import com.nacho.creditcards.exceptions.CreditCardNotValidException;
import com.nacho.creditcards.exceptions.TransactionAmountInvalidException;
import com.nacho.creditcards.exceptions.TransactionNotFoundException;
import com.nacho.creditcards.repositories.TransactionRepository;
import com.nacho.creditcards.services.interfaces.ITransactionService;

@Service
public class TransactionService implements ITransactionService {

    private TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction createTransaction(CreditCard creditCard, BigDecimal amount)
            throws CreditCardNotFoundException, CreditCardNotValidException, TransactionAmountInvalidException {

        if (creditCard == null) {
            throw new CreditCardNotFoundException("Credit card not found");
        }
        if (!isValidCreditCard(creditCard)) {
            throw new CreditCardNotValidException("Credit card is not valid");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0 || amount.compareTo(BigDecimal.valueOf(1000)) > 0) {
            throw new TransactionAmountInvalidException("Transaction amount is invalid");
        }

        Transaction transaction = Transaction.builder()
                .creditCard(creditCard)
                .amount(amount)
                .dateTime(LocalDateTime.now())
                .build();

        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction getTransactionById(Long id) throws TransactionNotFoundException {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public void deleteTransaction(Long id) throws TransactionNotFoundException {
        if (!transactionRepository.existsById(id)) {
            throw new TransactionNotFoundException("Transaction not found");
        }
        transactionRepository.deleteById(id);
    }
    
    @Override
    public boolean isValidCreditCard(CreditCard creditCard) {
        // Perform validation checks on the credit card
        // ...
        return true; // Return true if the credit card is valid, false otherwise
    }
}
