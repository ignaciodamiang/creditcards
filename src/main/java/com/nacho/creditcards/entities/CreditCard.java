package com.nacho.creditcards.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.YearMonth;

import com.nacho.creditcards.utilities.CardBrandAttributeConverter;
import com.nacho.creditcards.utilities.YearMonthAttributeConverter;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cardNumber;

    @Column(nullable = false)
    private String holderName;

    @Column(nullable = false)
    @Convert(converter = YearMonthAttributeConverter.class)
    private YearMonth expirationDate;

    @Column(nullable = false)
    @Convert(converter = CardBrandAttributeConverter.class)
    private CardBrand brand;
    
    public CreditCard(@NonNull String cardNumber, @NonNull String holderName, @NonNull YearMonth expirationDate, @NonNull CardBrand brand) {
        this.cardNumber = cardNumber;
        this.holderName = holderName;
        this.expirationDate = expirationDate;
        this.brand = brand;
    }
}
