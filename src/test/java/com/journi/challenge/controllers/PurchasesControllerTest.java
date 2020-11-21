package com.journi.challenge.controllers;

import com.journi.challenge.models.Purchase;
import com.journi.challenge.models.PurchaseRequest;
import com.journi.challenge.models.PurchaseStats;
import com.journi.challenge.repositories.PurchasesRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PurchasesControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PurchasesController purchasesController;
    @Autowired
    private PurchasesRepository purchasesRepository;

    private String getPurchaseJson(String invoiceNumber, String customerName, String dateTime, Double totalValue, String currencyCode, String... productIds) {
        String productIdList = "[\"" + String.join("\",\"", productIds) + "\"]";
        return String.format(Locale.US,"{\"invoiceNumber\":\"%s\",\"customerName\":\"%s\",\"dateTime\":\"%s\",\"productIds\":%s,\"amount\":%.2f,\"currencyCode\":\"%s\"}", invoiceNumber, customerName, dateTime, productIdList, totalValue, currencyCode);
    }

    @Test
    public void testPurchaseCurrencyCodeEUR() throws Exception {
        String body = getPurchaseJson("1", "customer 1", "2020-01-01T10:00:00+01:00", 25.34, "EUR", "product1");
        mockMvc.perform(post("/purchases")
                .contentType(MediaType.APPLICATION_JSON).content(body)
        ).andExpect(status().isOk());

        Purchase savedPurchase = purchasesRepository.list().get(purchasesRepository.list().size() - 1);
        assertEquals("customer 1", savedPurchase.getCustomerName());
        assertEquals("1", savedPurchase.getInvoiceNumber());
        assertEquals("2020-01-01T10:00:00", savedPurchase.getTimestamp().format(DateTimeFormatter.ISO_DATE_TIME));
        assertEquals(25.34, savedPurchase.getTotalValue());
    }


    @Test
    public void testPurchaseStatistics() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime firstDate = now.plusDays(20);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.of("UTC"));
        DateTimeFormatter outputFormatter = DateTimeFormatter.ISO_DATE.withZone(ZoneId.of("UTC"));

        // Outside window purchases
        purchasesController.save(new PurchaseRequest("1", "ABC", now.minusDays(35).format(formatter), Collections.emptyList(), 10d, "EUR"));
        purchasesController.save(new PurchaseRequest("1", "ABC", now.minusDays(34).format(formatter), Collections.emptyList(), 10d, "EUR"));
        purchasesController.save(new PurchaseRequest("1", "ABC", now.minusDays(33).format(formatter), Collections.emptyList(), 10d, "EUR"));
        purchasesController.save(new PurchaseRequest("1", "ABC", now.minusDays(32).format(formatter), Collections.emptyList(), 10d, "EUR"));
        purchasesController.save(new PurchaseRequest("1", "ABC", now.minusDays(32).format(formatter), Collections.emptyList(), 10d, "EUR"));
        purchasesController.save(new PurchaseRequest("1", "ABC", now.minusDays(31).format(formatter), Collections.emptyList(), 10d, "EUR"));

        // Inside window purchases
        purchasesController.save(new PurchaseRequest("1", "ABC", firstDate.format(formatter), Collections.emptyList(), 10d, "EUR"));
        purchasesController.save(new PurchaseRequest("1", "ABC", firstDate.plusDays(1).format(formatter), Collections.emptyList(), 10d, "EUR"));
        purchasesController.save(new PurchaseRequest("1", "ABC", firstDate.plusDays(2).format(formatter), Collections.emptyList(), 100d, "EUR"));
        purchasesController.save(new PurchaseRequest("1", "ABC", firstDate.plusDays(3).format(formatter), Collections.emptyList(), 20d, "EUR"));
        purchasesController.save(new PurchaseRequest("1", "ABC", firstDate.plusDays(4).format(formatter), Collections.emptyList(), 30d, "EUR"));
        purchasesController.save(new PurchaseRequest("1", "ABC", firstDate.plusDays(5).format(formatter), Collections.emptyList(), 50d, "EUR"));
        purchasesController.save(new PurchaseRequest("1", "ABC", firstDate.plusDays(6).format(formatter), Collections.emptyList(), 40d, "EUR"));
        purchasesController.save(new PurchaseRequest("1", "ABC", firstDate.plusDays(7).format(formatter), Collections.emptyList(), 70d, "EUR"));
        purchasesController.save(new PurchaseRequest("1", "ABC", firstDate.plusDays(8).format(formatter), Collections.emptyList(), 80d, "EUR"));
        purchasesController.save(new PurchaseRequest("1", "ABC", firstDate.plusDays(9).format(formatter), Collections.emptyList(), 90d, "EUR"));

        PurchaseStats purchaseStats = purchasesController.getStats();
        assertEquals(10, purchaseStats.getCountPurchases());
        assertEquals(500.0, purchaseStats.getTotalAmount());
        assertEquals(50.0, purchaseStats.getAvgAmount());
        assertEquals(10.0, purchaseStats.getMinAmount());
        assertEquals(100.0, purchaseStats.getMaxAmount());
        assertEquals(outputFormatter.format(firstDate), purchaseStats.getFrom());
        assertEquals(outputFormatter.format(firstDate.plusDays(9)), purchaseStats.getTo());
    }
}
