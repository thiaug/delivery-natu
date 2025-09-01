package com.deliverynatu.delivery_api.service.impl;

import com.deliverynatu.delivery_api.entity.*;
import com.deliverynatu.delivery_api.enums.StatusPedido;
import com.deliverynatu.delivery_api.repository.*;
import com.deliverynatu.delivery_api.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Override
    public Pedido criarPedido(Long clienteId, Long restauranteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado com ID: " + clienteId));

        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado com ID: " + restauranteId));

        if (!cliente.isAtivo()) {
            throw new IllegalArgumentException("Cliente inativo. Não é possível criar pedido.");
        }
        if (!restaurante.isAtivo()) {
            throw new IllegalArgumentException("Restaurante inativo. Não é possível criar pedido.");
        }

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setStatus(StatusPedido.PENDENTE);

        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido adicionarItem(Long pedidoId, Long produtoId, Integer quantidade) {
        Pedido pedido = buscarPorId(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado com ID: " + pedidoId));

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado com ID: " + produtoId));

        if (!produto.isDisponivel()) {
            throw new IllegalArgumentException("Produto indisponível:" + produto.getNome() + " Não é possível adicionar ao pedido");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        if (!produto.getRestaurante().getId().equals(pedido.getRestaurante().getId())) {
            throw new IllegalArgumentException("O produto " + produto.getNome() + " não pertence ao restaurante do pedido.");
        }
        ItemPedido item = new ItemPedido();
        item.setProduto(produto);
        item.setPedido(pedido);
        item.setQuantidade(quantidade);
        item.setPrecoUnitario(produto.getPreco());
        item.setSubtotal(produto.getPreco().multiply(BigDecimal.valueOf(quantidade)));

        pedido.adicionarItem(item);
        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido confirmarPedido(Long pedidoId) {
        Pedido pedido = buscarPorId(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado com ID: " + pedidoId));

        if (pedido.getStatus() != StatusPedido.PENDENTE) {
            throw new IllegalArgumentException("Apenas pedidos pendentes podem ser confirmados.");
        }

        if (pedido.getItens().isEmpty()) {
            throw new IllegalArgumentException("Não é possível confirmar um pedido sem itens.");
        }
        pedido.confirmar();

        return pedidoRepository.save(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> listarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteIdOrderByDataPedidoDesc(clienteId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pedido> buscarPorNumero(String numeroPedido) {
        return Optional.ofNullable(pedidoRepository.findByNumeroPedido(numeroPedido));
    }

    @Override
    public Pedido cancelarPedido(Long pedidoId, String motivoCancelamento) {
        Pedido pedido = buscarPorId(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado com ID: " + pedidoId));

        if (pedido.getStatus() == StatusPedido.ENTREGUE) {
            throw new IllegalArgumentException("O pedido já foi entregue e não pode ser cancelado.");
        }

        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new IllegalArgumentException("O pedido já está cancelado.");
        }

        pedido.setStatus(StatusPedido.CANCELADO);
        if (motivoCancelamento != null && !motivoCancelamento.isEmpty()) {
            pedido.setObservacoes(pedido.getObservacoes() + " Motivo do cancelamento: " + motivoCancelamento);
        }

        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido atualizarStatus(Long pedidoId, StatusPedido status) {
        Pedido pedido = buscarPorId(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado com ID: " + pedidoId));

        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new IllegalArgumentException("Não é possível atualizar o status de um pedido cancelado.");
        }

        if (pedido.getStatus() == StatusPedido.ENTREGUE) {
            throw new IllegalArgumentException("O pedido já foi entregue e não pode ser alterado.");
        }

        pedido.setStatus(status);
        return pedidoRepository.save(pedido);
    }
}