package com.nacho.creditcards.utilities;

import com.nacho.creditcards.entities.CardBrand;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CardBrandAttributeConverter implements AttributeConverter<CardBrand, String> {

    @Override
    public String convertToDatabaseColumn(CardBrand cardBrand) {
        if (cardBrand == null) {
            return null;
        }
        return cardBrand.name();
    }

    @Override
    public CardBrand convertToEntityAttribute(String columnValue) {
        if (columnValue == null) {
            return null;
        }
        return CardBrand.valueOf(columnValue);
    }
}
