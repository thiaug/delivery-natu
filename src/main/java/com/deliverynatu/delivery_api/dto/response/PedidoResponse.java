package com.deliverynatu.delivery_api.dto.response;

import com.deliverynatu.delivery_api.enums.StatusPedido;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
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

    @Getter
    @Setter
    public static class ClienteResponse {
        private Long id;
        private String nome;
    }

    @Getter
    @Setter
    public static class RestauranteResponse {
        private Long id;
        private String nome;
    }
}