package com.deliverynatu.delivery_api.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProdutoResponse {

    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private String categoria;
    private boolean disponivel;
    private RestauranteInProdutoResponse restaurante;

    @Data
    public static class RestauranteInProdutoResponse {
        private Long id;
        private String nome;
    }
}