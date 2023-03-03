package com.nacho.creditcards.utilities;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Converter(autoApply = true)
public class YearMonthAttributeConverter implements AttributeConverter<YearMonth, String> {
    private static final String PATTERN = "yyyy-MM";

    @Override
    public String convertToDatabaseColumn(YearMonth yearMonth) {
        if (yearMonth == null) {
            return null;
        }
        return yearMonth.format(DateTimeFormatter.ofPattern(PATTERN));
    }

    @Override
    public YearMonth convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }
        return YearMonth.parse(value, DateTimeFormatter.ofPattern(PATTERN));
    }
}
