package com.ryckxdev;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryckxdev.model.Carrinho;
import com.ryckxdev.model.RespostaCarrinhos;
import com.ryckxdev.service.AnaliseCarrinhos;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

public class App {

    private static final String ENDPOINT = "https://dummyjson.com/carts?limit=0";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void main(String[] args) {
        buscarRespostaCarrinhos().ifPresent(App::exibirRelatorio);
    }

    static Optional<RespostaCarrinhos> buscarRespostaCarrinhos() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT))
                .GET()
                .build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.err.println("Erro ao consumir API: status HTTP " + response.statusCode());
                return Optional.empty();
            }
            return Optional.of(OBJECT_MAPPER.readValue(response.body(), RespostaCarrinhos.class));
        } catch (JsonProcessingException e) {
            System.err.println("Erro ao desserializar JSON da API: " + e.getOriginalMessage());
        } catch (IOException e) {
            System.err.println("Erro de conexão ao acessar a API: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Requisição interrompida: " + e.getMessage());
        }
        return Optional.empty();
    }

    static void exibirRelatorio(RespostaCarrinhos respostaCarrinhos) {
        List<Carrinho> carrinhos = Optional.ofNullable(respostaCarrinhos.getCarts()).orElse(List.of());

        System.out.println("Carrinhos com total acima de US$ 1.000:");
        AnaliseCarrinhos.filtrarCarrinhosComTotalAcimaDe(carrinhos, 1000.0)
                .forEach(carrinho -> System.out.println(AnaliseCarrinhos.formatacaoPadrao().apply(carrinho)));

        System.out.println("\nProdutos com desconto acima de 15%:");
        AnaliseCarrinhos.titulosProdutosComDescontoAcimaDe(carrinhos, 15.0)
                .forEach(titulo -> System.out.println("- " + titulo));

        System.out.println("\nCarrinhos ordenados por economia (maior para menor):");
        AnaliseCarrinhos.ordenarCarrinhosPorEconomiaDecrescente(carrinhos)
                .forEach(carrinho -> System.out.printf("Carrinho #%d | Economia: US$ %.2f%n", carrinho.getId(), carrinho.getEconomia()));

        System.out.println("\nSoma de discountedTotal de todos os carrinhos:");
        List.of(AnaliseCarrinhos.somaDiscountedTotal(carrinhos))
                .forEach(soma -> System.out.printf("US$ %.2f%n", soma));

        System.out.println("\nQuantidade de carrinhos por número de produtos:");
        AnaliseCarrinhos.agruparQuantidadeCarrinhosPorNumeroProdutos(carrinhos)
                .forEach((totalProdutos, quantidadeCarrinhos) ->
                        System.out.printf("%d produto(s): %d carrinho(s)%n", totalProdutos, quantidadeCarrinhos));

        System.out.println("\nCarrinho de maior valor:");
        AnaliseCarrinhos.carrinhoMaiorValor(carrinhos)
                .map(AnaliseCarrinhos.formatacaoPadrao())
                .ifPresentOrElse(System.out::println, () -> System.out.println("Nenhum carrinho encontrado."));
    }
}
