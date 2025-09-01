package com.deliverynatu.delivery_api.dto.response;

import com.deliverynatu.delivery_api.enums.StatusPedido;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoResponse {

    private Long id;
    private String numeroPedido;
    private LocalDateTime dataPedido;
    private StatusPedido status;
    private BigDecimal subtotal;
    private BigDecimal taxaEntrega;
    private BigDecimal valorTotal;
    private String observacoes;

    private ClienteResponse cliente;
    private RestauranteResponse restaurante;
    private List<ItemPedidoResponse> itens;

    @Data
    public static class ClienteResponse {
        private Long id;
        private String nome;
    }

    @Data
    public static class RestauranteResponse {
        private Long id;
        private String nome;
    }
}