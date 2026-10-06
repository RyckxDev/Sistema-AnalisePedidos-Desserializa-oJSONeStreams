package com.ryckxdev;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryckxdev.model.Carrinho;
import com.ryckxdev.model.ProdutoCarrinho;
import com.ryckxdev.service.AnaliseCarrinhos;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnaliseCarrinhosTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    void deveCalcularEconomiaDoCarrinho() throws Exception {
        Carrinho carrinho = OBJECT_MAPPER.readValue("""
                {
                  "id": 1,
                  "userId": 33,
                  "total": 1200.5,
                  "discountedTotal": 1050.0,
                  "totalProducts": 3,
                  "totalQuantity": 7,
                  "products": []
                }
                """, Carrinho.class);

        assertEquals(150.5, carrinho.getEconomia(), 0.0001);
    }

    @Test
    void deveAplicarOperacoesDeStream() throws Exception {
        List<Carrinho> carrinhos = List.of(
                carrinho(1, 10, 1200.0, 1000.0, 2, 4,
                        produto(100, "Notebook", 20.0),
                        produto(101, "Mouse", 5.0)),
                carrinho(2, 11, 500.0, 450.0, 1, 1,
                        produto(102, "Teclado", 16.0))
        );

        assertEquals(1, AnaliseCarrinhos.filtrarCarrinhosComTotalAcimaDe(carrinhos, 1000.0).size());
        assertEquals(List.of("Notebook", "Teclado"), AnaliseCarrinhos.titulosProdutosComDescontoAcimaDe(carrinhos, 15.0));
        assertEquals(1450.0, AnaliseCarrinhos.somaDiscountedTotal(carrinhos), 0.0001);

        Map<Integer, Long> agrupado = AnaliseCarrinhos.agruparQuantidadeCarrinhosPorNumeroProdutos(carrinhos);
        assertEquals(1L, agrupado.get(1));
        assertEquals(1L, agrupado.get(2));

        assertTrue(AnaliseCarrinhos.carrinhoMaiorValor(carrinhos).isPresent());
        assertEquals(1, AnaliseCarrinhos.carrinhoMaiorValor(carrinhos).orElseThrow().getId());
        assertEquals("Carrinho #1 | Usuário: 10 | Itens: 4 | Total: US$ 1200,00".replace(',', '.'),
                AnaliseCarrinhos.formatacaoPadrao().apply(carrinhos.get(0)).replace(',', '.'));
    }

    private static Carrinho carrinho(int id,
                                     int userId,
                                     double total,
                                     double discountedTotal,
                                     int totalProducts,
                                     int totalQuantity,
                                     ProdutoCarrinho... produtos) throws Exception {
        String json = """
                {
                  "id": %d,
                  "userId": %d,
                  "total": %.2f,
                  "discountedTotal": %.2f,
                  "totalProducts": %d,
                  "totalQuantity": %d,
                  "products": %s
                }
                """.formatted(id, userId, total, discountedTotal, totalProducts, totalQuantity, OBJECT_MAPPER.writeValueAsString(produtos));
        return OBJECT_MAPPER.readValue(json, Carrinho.class);
    }

    private static ProdutoCarrinho produto(int id, String title, double desconto) throws Exception {
        return OBJECT_MAPPER.readValue("""
                {
                  "id": %d,
                  "title": "%s",
                  "price": 100.0,
                  "quantity": 1,
                  "total": 100.0,
                  "discountPercentage": %.2f
                }
                """.formatted(id, title, desconto), ProdutoCarrinho.class);
    }
}
