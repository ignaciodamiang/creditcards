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
	public boolean isExpirationDateValid(CreditCard creditCard) {
	    YearMonth expirationDate = creditCard.getExpirationDate();
	    YearMonth currentDate = YearMonth.now();
	    return expirationDate.isAfter(currentDate) || expirationDate.equals(currentDate);
	}

}
