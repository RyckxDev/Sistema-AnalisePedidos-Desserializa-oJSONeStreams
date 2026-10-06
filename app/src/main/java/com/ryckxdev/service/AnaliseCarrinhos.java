package com.ryckxdev.service;

import com.ryckxdev.model.Carrinho;
import com.ryckxdev.model.ProdutoCarrinho;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class AnaliseCarrinhos {

    private AnaliseCarrinhos() {
    }

    public static List<Carrinho> filtrarCarrinhosComTotalAcimaDe(List<Carrinho> carrinhos, double valorMinimo) {
        return carrinhos.stream()
                .filter(carrinho -> carrinho.getTotal() > valorMinimo)
                .toList();
    }

    public static List<String> titulosProdutosComDescontoAcimaDe(List<Carrinho> carrinhos, double descontoMinimo) {
        return carrinhos.stream()
                .flatMap(carrinho -> Optional.ofNullable(carrinho.getProducts()).orElse(List.of()).stream())
                .filter(produto -> produto.getDiscountPercentage() > descontoMinimo)
                .map(ProdutoCarrinho::getTitle)
                .toList();
    }

    public static List<Carrinho> ordenarCarrinhosPorEconomiaDecrescente(List<Carrinho> carrinhos) {
        return carrinhos.stream()
                .sorted(Comparator.comparingDouble(Carrinho::getEconomia).reversed())
                .toList();
    }

    public static double somaDiscountedTotal(List<Carrinho> carrinhos) {
        return carrinhos.stream()
                .map(Carrinho::getDiscountedTotal)
                .reduce(0.0, Double::sum);
    }

    public static Map<Integer, Long> agruparQuantidadeCarrinhosPorNumeroProdutos(List<Carrinho> carrinhos) {
        return carrinhos.stream()
                .collect(Collectors.groupingBy(Carrinho::getTotalProducts, Collectors.counting()));
    }

    public static Optional<Carrinho> carrinhoMaiorValor(List<Carrinho> carrinhos) {
        return carrinhos.stream().max(Comparator.comparingDouble(Carrinho::getTotal));
    }

    public static Function<Carrinho, String> formatacaoPadrao() {
        return carrinho -> String.format(
                "Carrinho #%d | Usuário: %d | Itens: %d | Total: US$ %.2f",
                carrinho.getId(),
                carrinho.getUserId(),
                carrinho.getTotalQuantity(),
                carrinho.getTotal());
    }
}
