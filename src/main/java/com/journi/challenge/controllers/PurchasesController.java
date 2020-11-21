package com.journi.challenge.controllers;

import com.journi.challenge.models.Purchase;
import com.journi.challenge.models.PurchaseRequest;
import com.journi.challenge.models.PurchaseStats;
import com.journi.challenge.service.PurchaseService;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
public class PurchasesController {

    @Inject
    private PurchaseService purchaseService;

    @GetMapping("/purchases/statistics")
    public PurchaseStats getStats() {
        return purchaseService.getLast30DaysStats();
    }

    @PostMapping("/purchases")
    public Purchase save(@RequestBody PurchaseRequest purchaseRequest) {
       return purchaseService.savePurchase(purchaseRequest);
    }
}
