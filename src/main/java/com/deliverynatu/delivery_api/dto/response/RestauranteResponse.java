package com.deliverynatu.delivery_api.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RestauranteResponse {

    private Long id;
    private String nome;
    private String categoria;
    private String endereco;
    private String telefone;
    private BigDecimal taxaEntrega;
    private BigDecimal avaliacao;

}