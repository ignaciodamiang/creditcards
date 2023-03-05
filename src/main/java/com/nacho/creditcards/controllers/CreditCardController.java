package com.nacho.creditcards.controllers;

import com.nacho.creditcards.entities.CreditCard;
import com.nacho.creditcards.services.interfaces.ICreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/credit-cards")
public class CreditCardController {

    @Autowired ICreditCardService creditCardService;

    @GetMapping("/{id}")
    public ResponseEntity<CreditCard> getCreditCardById(@PathVariable Long id) {
        CreditCard creditCard = creditCardService.getCreditCardById(id);
        if (creditCard == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(creditCard);
    }

    @GetMapping
    public ResponseEntity<List<CreditCard>> getAllCreditCards() {
        List<CreditCard> creditCards = creditCardService.getAllCreditCards();
        if (creditCards.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(creditCards);
    }

    @PostMapping
    public ResponseEntity<CreditCard> createCreditCard(@RequestBody CreditCard creditCard) {
        CreditCard createdCreditCard = creditCardService.createCreditCard(creditCard);
        return new ResponseEntity<>(createdCreditCard, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreditCard> updateCreditCard(@PathVariable Long id, @RequestBody CreditCard creditCard) {
        CreditCard updatedCreditCard = creditCardService.updateCreditCard(id, creditCard);
        if (updatedCreditCard == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedCreditCard);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCreditCard(@PathVariable Long id) {
        creditCardService.deleteCreditCard(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}/valid")
    public ResponseEntity<String> isCreditCardValid(@PathVariable Long id) {
        CreditCard creditCard = creditCardService.getCreditCardById(id);
        if (creditCard == null) {
            return ResponseEntity.notFound().build();
        }
        boolean isValid = creditCardService.isValidCreditCard(creditCard);
        if (isValid) {
            return ResponseEntity.ok("Credit card is valid for operations");
        } else {
            return ResponseEntity.ok("Credit card is not valid for operations");
        }
    }

    @GetMapping("/is-distinct")
    public ResponseEntity<Boolean> isCreditCardDistinct(@RequestBody CreditCard creditCard) {
        boolean isDistinct = creditCardService.isCreditCardDistinct(creditCard);
        return ResponseEntity.ok(isDistinct);
    }    
}
