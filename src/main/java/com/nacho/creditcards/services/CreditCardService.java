package com.nacho.creditcards.services;

import com.nacho.creditcards.entities.CardBrand;
import com.nacho.creditcards.entities.CreditCard;
import com.nacho.creditcards.exceptions.CreditCardNotValidException;
import com.nacho.creditcards.repositories.CreditCardRepository;
import com.nacho.creditcards.services.interfaces.ICreditCardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
public class CreditCardService implements ICreditCardService {

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Override
    public CreditCard createCreditCard(CreditCard creditCard) throws CreditCardNotValidException {
        YearMonth now = YearMonth.now();
        YearMonth cardExpiration = creditCard.getExpirationDate();
        if (cardExpiration.isBefore(now)) {
            throw new CreditCardNotValidException("Credit card expiration date is not valid");
        }
        return creditCardRepository.save(creditCard);
    }

    @Override
    public CreditCard getCreditCardById(Long id) {
        Optional<CreditCard> optionalCreditCard = creditCardRepository.findById(id);
        return optionalCreditCard.orElse(null);
    }

    @Override
    public List<CreditCard> getAllCreditCards() {
        return creditCardRepository.findAll();
    }

    @Override
    public CreditCard updateCreditCard(Long id, CreditCard creditCard) {
        Optional<CreditCard> optionalCreditCard = creditCardRepository.findById(id);
        if (optionalCreditCard.isPresent()) {
            CreditCard existingCreditCard = optionalCreditCard.get();
            existingCreditCard.setCardNumber(creditCard.getCardNumber());
            existingCreditCard.setHolderName(creditCard.getHolderName());
            existingCreditCard.setExpirationDate(creditCard.getExpirationDate());
            existingCreditCard.setBrand(creditCard.getBrand());
            return creditCardRepository.save(existingCreditCard);
        } else {
            return null;
        }
    }

    @Override
    public void deleteCreditCard(Long id) {
        creditCardRepository.deleteById(id);
    }
    
	@Override
	public CreditCard findByCardNumberAndHolderNameAndExpirationDateAndBrand(String cardNumber, String holderName, YearMonth expirationDate, CardBrand brand) {
		return creditCardRepository.findByCardNumberAndHolderNameAndExpirationDateAndBrand(cardNumber, holderName, expirationDate, brand);
	}
	
	@Override
	public boolean isValidCreditCard(CreditCard creditCard) {
		boolean isValid = true;
		String errorMessage = "";
	
		// Check card number validity
		String cardNumber = creditCard.getCardNumber();
		if (!isValidCardNumber(cardNumber)) {
			isValid = false;
			errorMessage += "Invalid card number: " + cardNumber + "\n";
		}
	
		// Check expiration date validity
		if (!isExpirationDateValid(creditCard)) {
			isValid = false;
			errorMessage += "Invalid expiration date: " + creditCard.getExpirationDate() + "\n";
		}
	
		// Check cardholder name validity
		String cardholderName = creditCard.getHolderName();
		if (!isCardHolderNameValid(cardholderName)) {
			isValid = false;
			errorMessage += "Invalid cardholder name: " + cardholderName + "\n";
		}
	
		if (!isValid) {
			throw new CreditCardNotValidException("Credit card is not valid: \n" + errorMessage);
		}
	
		return isValid;
	}	

	@Override
	public boolean isExpirationDateValid(CreditCard creditCard) {
	    YearMonth expirationDate = creditCard.getExpirationDate();
	    YearMonth currentDate = YearMonth.now();
	    return expirationDate.isAfter(currentDate) || expirationDate.equals(currentDate);
	}

	private boolean isValidCardNumber(String cardNumber) {
	    return cardNumber.matches("\\d{16}");
	}
	
	public boolean isCardHolderNameValid(String cardholderName) {
	    if (cardholderName == null || cardholderName.isBlank()) {
	        return false;
	    }
	    String[] nameParts = cardholderName.split("\\s+");
	    if (nameParts.length < 2) {
	        return false;
	    }
	    // Validate each name part
	    for (String namePart : nameParts) {
	        if (!namePart.matches("[A-Za-z]+")) {
	            return false;
	        }
	    }
	    return true;
	}

	@Override
	public boolean isCreditCardDistinct(CreditCard creditCard) {
	    // check if a credit card with the same number already exists
	    CreditCard existingCard = creditCardRepository.findByCardNumberAndHolderNameAndExpirationDateAndBrand(
	            creditCard.getCardNumber(),
	            creditCard.getHolderName(),
	            creditCard.getExpirationDate(),
	            creditCard.getBrand()
	    );
	    if (existingCard == null) {
	        // no credit card with the same number exists, it is distinct
	        return true;
	    } else {
	        // a credit card with the same number already exists, it is not distinct
	        return false;
	    }
	}
}
