package com.deliverynatu.delivery_api.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ItemPedidoResponse {

    private Long id;
    private int quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;
    private String produtoNome;
    private Long produtoId;

}