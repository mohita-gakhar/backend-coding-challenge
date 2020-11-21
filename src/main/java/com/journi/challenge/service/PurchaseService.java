package com.journi.challenge.service;

import com.journi.challenge.models.PurchaseStats;
import com.journi.challenge.util.currency.CurrencyConverterUtil;
import com.journi.challenge.models.Purchase;
import com.journi.challenge.models.PurchaseRequest;
import com.journi.challenge.repositories.PurchasesRepository;
import com.journi.challenge.util.stats.PurchaseStatisticsUtil;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Named
@Service
public class PurchaseService {

    @Inject
    private PurchasesRepository purchasesRepository;

    @Inject
    private CurrencyConverterUtil currencyConverterUtil;

    public Purchase savePurchase(PurchaseRequest purchaseRequest) {
        Double totalConvertValue = currencyConverterUtil.convertCurrencyToEuro(purchaseRequest.getCurrencyCode(),
                purchaseRequest.getAmount());
        LocalDateTime dateTime = LocalDateTime.parse(purchaseRequest.getDateTime(), DateTimeFormatter.ISO_DATE_TIME);

        Purchase newPurchase = new Purchase(
                purchaseRequest.getInvoiceNumber(),
                dateTime,
                purchaseRequest.getProductIds(),
                purchaseRequest.getCustomerName(),
                totalConvertValue
        );
        purchasesRepository.save(newPurchase);
        PurchaseStatisticsUtil.addPurchase(newPurchase);
        return newPurchase;
    }

    public PurchaseStats getLast30DaysStats() {
        return PurchaseStatisticsUtil.getLast30DaysStats();
    }

}
