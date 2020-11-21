package com.journi.challenge.util.currency;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CountryCurrencyUtilTest {

    private final CountryCurrencyUtil countryCurrencyUtil = new CountryCurrencyUtil();

    @Test
    void findCurrencyCodeForSupportedCountry() {
        assertEquals("EUR", countryCurrencyUtil.getCurrencyForCountryCode("AT"));
        assertEquals("EUR", countryCurrencyUtil.getCurrencyForCountryCode("DE"));
        assertEquals("EUR", countryCurrencyUtil.getCurrencyForCountryCode("FR"));
        assertEquals("BRL", countryCurrencyUtil.getCurrencyForCountryCode("BR"));
        assertEquals("GBP", countryCurrencyUtil.getCurrencyForCountryCode("GB"));
    }

    @Test
    void findCurrencyCodeForNonSupportedCountry() {
        assertEquals("EUR", countryCurrencyUtil.getCurrencyForCountryCode("CH"));
        assertEquals("EUR", countryCurrencyUtil.getCurrencyForCountryCode("CL"));
        assertEquals("EUR", countryCurrencyUtil.getCurrencyForCountryCode("AR"));
        assertEquals("EUR", countryCurrencyUtil.getCurrencyForCountryCode("FI"));
    }
}
