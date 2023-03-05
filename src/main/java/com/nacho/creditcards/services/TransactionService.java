package com.nacho.creditcards.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nacho.creditcards.entities.CardBrand;
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

        validateTransaction(creditCard, amount);

        Transaction transaction = Transaction.builder()
                .creditCard(creditCard)
                .amount(amount)
                .dateTime(LocalDateTime.now())
                .build();

        return transactionRepository.save(transaction);
    }
    
    public void validateTransaction(CreditCard creditCard, BigDecimal amount)
            throws CreditCardNotFoundException, CreditCardNotValidException, TransactionAmountInvalidException {

        if (creditCard == null) {
            throw new CreditCardNotFoundException("Credit card not found");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0 || amount.compareTo(BigDecimal.valueOf(1000)) > 0) {
            throw new TransactionAmountInvalidException("Transaction amount is invalid");
        }
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
    
    public double calculateFee(Transaction transaction) {
        double fee = 0.0;
        int year = transaction.getDateTime().getYear() % 100;
        int month = transaction.getDateTime().getMonthValue();
        if (transaction.getCreditCard().getBrand().equals(CardBrand.VISA)) {
            fee = year / (double) month;
        } else if (transaction.getCreditCard().getBrand().equals(CardBrand.NARA)) {
            fee = transaction.getDateTime().getDayOfMonth() * 0.5;
        } else if (transaction.getCreditCard().getBrand().equals(CardBrand.AMEX)) {
            fee = month * 0.1;
        }
        return transaction.getAmount().doubleValue() * fee;
    }
}
