package com.ryckxdev.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RespostaCarrinhos {

    private List<Carrinho> carts;
    private int total;
    private int skip;
    private int limit;

    public RespostaCarrinhos() {
    }

    public List<Carrinho> getCarts() {
        return carts;
    }

    public int getTotal() {
        return total;
    }

    public int getSkip() {
        return skip;
    }

    public int getLimit() {
        return limit;
    }

    @Override
    public String toString() {
        return "RespostaCarrinhos{" +
                "carts=" + carts +
                ", total=" + total +
                ", skip=" + skip +
                ", limit=" + limit +
                '}';
    }
}
