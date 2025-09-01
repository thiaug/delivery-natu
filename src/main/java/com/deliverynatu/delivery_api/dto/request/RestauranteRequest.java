package com.deliverynatu.delivery_api.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RestauranteRequest {

    @NotBlank(message = "Nome do restaurante é obrigatório")
    private String nome;

    @NotBlank(message = "Categoria do restaurante é obrigatória")
    private String categoria;

    private String endereco;

    private String telefone;

    @DecimalMin(value = "0.00", message = "Taxa de entrega deve ser maior ou igual a zero")
    private BigDecimal taxaEntrega;

}