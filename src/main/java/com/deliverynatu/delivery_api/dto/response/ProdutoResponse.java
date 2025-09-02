package com.deliverynatu.delivery_api.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProdutoResponse {

    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private String categoria;
    private boolean disponivel;
    private RestauranteInProdutoResponse restaurante;

    @Getter
    @Setter
    public static class RestauranteInProdutoResponse {
        private Long id;
        private String nome;
    }
}