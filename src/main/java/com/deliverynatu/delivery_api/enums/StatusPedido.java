package com.deliverynatu.delivery_api.enums;

public enum StatusPedido {
    PENDENTE("Pendente"),
    CONFIRMADO("Confirmado"),
    PREPARANDO("Preparando"),
    EM_TRANSITO("Em trânsito"),
    SAIU_PARA_ENTREGA("Saiu para entrega"),
    ENTREGUE("Entregue"),
    CANCELADO("Cancelado");

    private final String descricao;

    StatusPedido(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;

    }
}