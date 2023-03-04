package com.nacho.creditcards.utilities;

import org.junit.jupiter.api.Test;

import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class YearMonthAttributeConverterTest {

    private final YearMonthAttributeConverter converter = new YearMonthAttributeConverter();

    @Test
    void testConvertToDatabaseColumn() {
        YearMonth yearMonth = YearMonth.of(2022, 3);
        String result = converter.convertToDatabaseColumn(yearMonth);
        assertEquals("2022-03", result);
    }

    @Test
    void testConvertToDatabaseColumnWithNull() {
        String result = converter.convertToDatabaseColumn(null);
        assertNull(result);
    }

    @Test
    void testConvertToEntityAttribute() {
        String value = "2022-03";
        YearMonth result = converter.convertToEntityAttribute(value);
        assertEquals(YearMonth.of(2022, 3), result);
    }

    @Test
    void testConvertToEntityAttributeWithNull() {
        YearMonth result = converter.convertToEntityAttribute(null);
        assertNull(result);
    }
}
