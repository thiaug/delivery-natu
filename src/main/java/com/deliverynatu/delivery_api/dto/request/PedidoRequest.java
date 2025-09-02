package com.deliverynatu.delivery_api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PedidoRequest {

    @NotNull(message = "O ID do cliente é obrigatório.")
    private Long clienteId;

    @NotNull(message = "O ID do restaurante é obrigatório.")
    private Long restauranteId;

    private String observacoes;
}