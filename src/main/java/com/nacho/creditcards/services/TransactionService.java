package com.nacho.creditcards.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
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
    
    public BigDecimal calculateFee(Transaction transaction) {
        BigDecimal fee = BigDecimal.ZERO;
        int year = transaction.getDateTime().getYear() % 100;
        int month = transaction.getDateTime().getMonthValue();
        if (transaction.getCreditCard().getBrand().equals(CardBrand.VISA)) {
            fee = BigDecimal.valueOf(year).divide(BigDecimal.valueOf(month), 2, RoundingMode.HALF_UP);
        } else if (transaction.getCreditCard().getBrand().equals(CardBrand.NARA)) {
            fee = BigDecimal.valueOf(transaction.getDateTime().getDayOfMonth()).multiply(BigDecimal.valueOf(0.5));
        } else if (transaction.getCreditCard().getBrand().equals(CardBrand.AMEX)) {
            fee = BigDecimal.valueOf(month).multiply(BigDecimal.valueOf(0.1));
        }
        return transaction.getAmount().multiply(fee).divide(BigDecimal.valueOf(100));
    }

    @Override
    public Transaction updateTransaction(Long id, Transaction transaction) throws TransactionNotFoundException {
        Transaction existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction with id: " + id + " not found"));
        
        existingTransaction.setCreditCard(transaction.getCreditCard());
        existingTransaction.setAmount(transaction.getAmount());
        existingTransaction.setDateTime(transaction.getDateTime());

        return transactionRepository.save(existingTransaction);
    }

    @Override
    public BigDecimal simulateTransactionAndGetFee(String brand, BigDecimal amount) {
        BigDecimal fee = BigDecimal.ZERO;
        if (brand != null && amount != null) {
            CardBrand cardBrand = CardBrand.valueOf(brand.toUpperCase());
            LocalDateTime now = LocalDateTime.now();
            CreditCard creditCard = CreditCard.builder()
                    .brand(cardBrand)
                    .expirationDate(YearMonth.from(now.plusYears(1)))
                    .holderName("John Doe")
                    .cardNumber("1234567890123456")
                    .build();
            Transaction transaction = Transaction.builder()
                    .creditCard(creditCard)
                    .dateTime(now)
                    .amount(amount)
                    .build();
            fee = calculateFee(transaction);
        }
        return fee;
    }
}
