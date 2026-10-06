package com.ryckxdev.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProdutoCarrinho {

    private int id;
    private String title;
    private double price;
    private int quantity;
    private double total;
    private double discountPercentage;

    public ProdutoCarrinho() {
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotal() {
        return total;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    @Override
    public String toString() {
        return "ProdutoCarrinho{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", total=" + total +
                ", discountPercentage=" + discountPercentage +
                '}';
    }
}
