package com.deliverynatu.delivery_api.service;

import com.deliverynatu.delivery_api.entity.Pedido;
import com.deliverynatu.delivery_api.enums.StatusPedido;

import java.util.List;
import java.util.Optional;

public interface PedidoService {

    Pedido criarPedido(Long clienteId, Long restauranteId);

    Pedido adicionarItem(Long pedidoId, Long produtoId, Integer quantidade);

    Pedido confirmarPedido(Long pedidoId);

    Optional<Pedido> buscarPorId(Long id);

    List<Pedido> listarPorCliente(Long clienteId);

    Optional<Pedido> buscarPorNumero(String numeroPedido);

    Pedido cancelarPedido(Long pedidoId, String motivoCancelamento);

    Pedido atualizarStatus(Long pedidoId, StatusPedido status);
}