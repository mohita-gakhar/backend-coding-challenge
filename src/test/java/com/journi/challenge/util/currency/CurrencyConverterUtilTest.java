package com.journi.challenge.util.currency;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CurrencyConverterUtilTest {

    private final CurrencyConverterUtil currencyConverterUtil = new CurrencyConverterUtil();

    @Test
    void convertEurValueToSupportedCurrency() {
        assertEquals(25.0, currencyConverterUtil.convertEurToCurrency("EUR", 25.0));
        assertEquals(25.0 * 5.1480, currencyConverterUtil.convertEurToCurrency("BRL", 25.0));
    }
}
