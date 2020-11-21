package com.journi.challenge.vo;

import com.journi.challenge.models.Product;

public class ProductVO {

    private final String id;
    private final String description;
    private final Double price;
    private final String currencyCode;

    public ProductVO(Product product, String currencyCode, Double price) {
        this.id = product.getId();
        this.description = product.getDescription();
        this.currencyCode = currencyCode;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }
}
