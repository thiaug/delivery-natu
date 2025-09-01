package com.deliverynatu.delivery_api.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProdutoRequest {

    @NotBlank(message = "O nome do produto é obrigatório.")
    private String nome;

    private String descricao;

    @NotNull(message = "O preço do produto é obrigatório.")
    @DecimalMin(value = "0.01", message = "O preço do produto deve ser maior que zero.")
    private BigDecimal preco;

    private String categoria;
}