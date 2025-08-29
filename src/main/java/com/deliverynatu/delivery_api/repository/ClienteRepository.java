package com.deliverynatu.delivery_api.repository;

import com.deliverynatu.delivery_api.entity.Cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Buscar cliente por email
    Optional<Cliente> findByEmail(String email);

    // Verificar se email já existe
    boolean existsByEmail(String email);

    // Buscar clientes ativos
    List<Cliente> findByAtivoTrue();

    // Buscar clientes por nome
    List<Cliente> findByNomeContainingIgnoreCase(String nome);

    // Buscar cliente por telefone
    Optional<Cliente> findByTelefone(String telefone);

    @Query(value = "SELECT c.nome, COUNT(p.id) as total_pedidos " +
            "FROM cliente c " +
            "LEFT JOIN pedido p ON c.id = p.cliente_id " +
            "GROUP BY c.id, c.nome " +
            "ORDER BY total_pedidos DESC " +
            "LIMIT 10", nativeQuery = true)
    List<Object[]> rankingClientesPorPedidos();

    // Query nativa - clientes por cidade
    @Query(value = "SELECT * FROM clientes WHERE endereco LIKE %:cidade% AND ativo = true", nativeQuery = true)
    List<Cliente> findByCidade(@Param("cidade") String cidade);

    // Contar clientes ativos
    @Query("SELECT COUNT(c) FROM Cliente c WHERE c.ativo = true")
    Long countClientesAtivos();
}
