package com.journi.challenge.util.currency;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Named;
import javax.inject.Singleton;
import java.net.URL;
import java.util.*;

@Named
@Singleton
public class CurrencyConverterUtil {

    private final Map<String, Double> currencyEurRate;

    public CurrencyConverterUtil() {

        currencyEurRate = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            URL rates = getClass().getResource("/eur_rate.json");
            JsonNode ratesTree = mapper.readTree(rates);
            Iterator<JsonNode> currenciesIterator = ratesTree.findPath("currencies").elements();
            currenciesIterator.forEachRemaining(currency -> currencyEurRate.put(currency.get("currency").asText(), currency.get("rate").asDouble()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Double convertEurToCurrency(String currencyCode, Double eurValue) {
        return eurValue * currencyEurRate.getOrDefault(currencyCode, 1.0);
    }

    public Double convertCurrencyToEuro(String currencyCode, Double value) {
        double conversionRate = currencyEurRate.getOrDefault(currencyCode, 1.0);
        return value * 1.0d/conversionRate;
    }
}
