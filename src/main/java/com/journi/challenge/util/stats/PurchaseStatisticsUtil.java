package com.journi.challenge.util.stats;

import com.journi.challenge.models.Purchase;
import com.journi.challenge.models.PurchaseStats;

import java.time.Duration;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Deque;
import java.util.LinkedList;

public class PurchaseStatisticsUtil {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE.withZone(ZoneId.of("UTC"));

    private static Deque<Purchase> minPurchases = new LinkedList<>();
    private static Deque<Purchase> maxPurchases = new LinkedList<>();
    private static Deque<Purchase> allPurchases = new LinkedList<>();
    private static double runningSum = 0.0d;

    public static void addPurchase(Purchase purchase) {
        if(null == purchase || null == purchase.getTimestamp() || null == purchase.getTotalValue()) {
            return;
        }
        updateAllPurchases(purchase);
        updateMinimum(purchase);
        updateMaximum(purchase);
    }

    public static PurchaseStats getLast30DaysStats() {
        String from = "";
        String to = "";
        long countPurchase = 0L;
        double totalAmount = runningSum;
        double averageAmount = 0.0d;
        double min = 0.0d;
        double max = 0.0d;
        if(!allPurchases.isEmpty()) {
            from = formatter.format(allPurchases.peekFirst().getTimestamp());
            to = formatter.format(allPurchases.peekLast().getTimestamp());
            countPurchase = allPurchases.size();
            averageAmount = totalAmount/countPurchase;
            min = minPurchases.peekFirst().getTotalValue();
            max = maxPurchases.peekFirst().getTotalValue();
        }
        return new PurchaseStats(from, to, countPurchase, totalAmount, averageAmount, min, max) ;
    }

    private static void updateAllPurchases(Purchase purchase) {
        while(!allPurchases.isEmpty()) {
            Duration duration = Duration.between(allPurchases.getFirst().getTimestamp(), purchase.getTimestamp());
            if(duration.toDays() <= 30) {
                break;
            }else {
                Purchase firstPurchase = allPurchases.removeFirst();
                runningSum -= firstPurchase.getTotalValue();
            }
        }
        allPurchases.add(purchase);
        runningSum += purchase.getTotalValue();
    }

    private static void updateMinimum(Purchase purchase) {
        while(!minPurchases.isEmpty() && minPurchases.peekLast().getTotalValue() > purchase.getTotalValue()) {
            minPurchases.removeLast();
        }
        minPurchases.addLast(purchase);
    }

    private static void updateMaximum(Purchase purchase) {
        while(!maxPurchases.isEmpty() && maxPurchases.peekLast().getTotalValue() < purchase.getTotalValue()) {
            maxPurchases.removeLast();
        }
        maxPurchases.addLast(purchase);
    }
}
