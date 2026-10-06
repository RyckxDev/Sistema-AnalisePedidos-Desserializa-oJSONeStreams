package com.ryckxdev.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Carrinho {

    private int id;
    private int userId;
    private double total;
    private double discountedTotal;
    private int totalProducts;
    private int totalQuantity;
    private List<ProdutoCarrinho> products;

    public Carrinho() {
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public double getTotal() {
        return total;
    }

    public double getDiscountedTotal() {
        return discountedTotal;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public List<ProdutoCarrinho> getProducts() {
        return products;
    }

    public double getEconomia() {
        return total - discountedTotal;
    }

    @Override
    public String toString() {
        return "Carrinho{" +
                "id=" + id +
                ", userId=" + userId +
                ", total=" + total +
                ", discountedTotal=" + discountedTotal +
                ", totalProducts=" + totalProducts +
                ", totalQuantity=" + totalQuantity +
                ", products=" + products +
                '}';
    }
}
