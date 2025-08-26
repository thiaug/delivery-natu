package com.deliverynatu.delivery_api.repository;

import com.deliverynatu.delivery_api.entity.Pedido;
import com.deliverynatu.delivery_api.entity.Cliente;

import java.io.ObjectInputFilter.Status;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    // Busca pedidos por cliente e ordena por data do pedido em ordem decrescente
    List<Pedido> findByClienteOrderByDataPedidoDesc(Cliente cliente);

    // Busca pedidos por clienteId e ordena por data do pedido em ordem decrescente
    List<Pedido> findByClienteIdOrderByDataPedidoDesc(long clienteId);

    // Busca pedidos por status e ordena por data do pedido em ordem decrescente
    List<Pedido> findByStatusOrderByDataPedidoDesc(Status status);

    // Busca pedidos por numeroPedido
    Pedido findByNumeroPedido(String numeroPedido);

    // Busca pedidos por periodo
    List<Pedido> findByDataPedidoBetweenOrderByDataPedidoDesc(LocalDateTime inicio, LocalDateTime fim);

    // Busca pedidos do dia
    // @Query("SELECT p FROM Pedido p WHERE DATE(p.dataPedido) = CURRENT_DATE ORDER
    // BY p.dataPedido DESC")
    // List<Pedido> findPedidosDoDia();

    // Busca pedidos por restauranteId e ordena por data do pedido em ordem
    // decrescente
    @Query("SELECT p FROM Pedido p WHERE p.restaurante.id = :restauranteId ORDER BY p.dataPedido DESC")
    List<Pedido> findByRestauranteId(@Param("restauranteId") Long restauranteId);

    // Busca pedidos por status
    @Query("SELECT p.status, COUNT(p) FROM Pedido p GROUP BY p.status")
    List<Object[]> countPedidosByStatus();

    // Busca pedidos pendentes (dashboard)
    @Query("SELECT p FROM Pedido p WHERE p.status IN ('PENDENTE','CONFIRMADO','PREPARANDO') ORDER BY p.dataPedido ASC")
    List<Pedido> findPedidosPendentes();

    // Valor total de vendas por periodo
    @Query("SELECT SUM(p.valorTotal) FROM Pedido p WHERE p.dataPedido BETWEEN :inicio AND :fim")
    BigDecimal calcularVendasPorPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

}
