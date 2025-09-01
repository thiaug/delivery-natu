package com.deliverynatu.delivery_api.service;

import com.deliverynatu.delivery_api.entity.Produto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProdutoService {

    Produto cadastrar(Produto produto, Long restauranteId);

    Optional<Produto> buscarPorId(Long id);

    List<Produto> listarTodosDisponiveis();

    List<Produto> listarPorRestaurante(Long restauranteId);

    List<Produto> buscarPorCategoria(String categoria);

    List<Produto> buscarPorFaixaDePreco(BigDecimal precoMin, BigDecimal precoMax);

    Produto atualizar(Long id, Produto produtoAtualizado);

    void alterarDisponibilidade(Long id, boolean disponivel);
}