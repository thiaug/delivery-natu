package com.deliverynatu.delivery_api.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deliverynatu.delivery_api.entity.Produto;
import com.deliverynatu.delivery_api.entity.Restaurante;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
        // Buscar todos os produtos de um restaurante
        List<Produto> findByRestauranteId(Long restauranteId);

        // Buscar produto por nome
        Optional<Produto> findByNome(String nome);

        // Buscar todos os produtos disponíveis (independente do restaurante)
        List<Produto> findByDisponivelTrue();

        // Buscar produtos por categoria
        List<Produto> findByCategoria(String categoria);

        // Buscar produtos disponiveis de um restaurante
        List<Produto> findByRestauranteAndDisponivelTrue(Restaurante restaurante);

        // Buscar produtos disponiveis por restauranteId
        List<Produto> findByRestauranteIdAndDisponivelTrue(Long restauranteId);

        // Buscar produtos disponiveis por categoria
        List<Produto> findByCategoriaAndDisponivelTrue(String categoria);

        // Buscar por nome contendo
        Optional<Produto> findByNomeContainingIgnoreCaseAndDisponivelTrue(String nome);

        // Buscar por faixa de preço
        List<Produto> findByPrecoBetweenAndDisponivelTrue(BigDecimal precoMin, BigDecimal precoMax);

        // Buscar produtos mais baratos que um determinado preço
        List<Produto> findByPrecoLessThanEqualAndDisponivelTrue(BigDecimal preco);

        // Ordenar produtos por preço ascendente
        List<Produto> findByDisponivelTrueOrderByPrecoAsc();

        // Ordenar produtos por preço descendente
        List<Produto> findByDisponivelTrueOrderByPrecoDesc();

        // Buscar Restaurante e Categoria
        List<Produto> findByRestauranteAndCategoria(Restaurante restaurante, String string);

        @Query(value = "SELECT p.nome, COUNT(ip.produto_id) as quanƟdade_vendida " +
                        "FROM produto p " +
                        "LEFT JOIN item_pedido ip ON p.id = ip.produto_id " +
                        "GROUP BY p.id, p.nome " +
                        "ORDER BY quanƟdade_vendida DESC " +
                        "LIMIT 5", nativeQuery = true)
        List<Object[]> produtosMaisVendidos();

        // Produtos mais vendidos
        @Query("SELECT ip.produto FROM ItemPedido ip GROUP BY ip.produto ORDER BY COUNT(ip) DESC")
        List<Produto> findProdutosMaisVendidos();

        // Buscar produtos por restaurante e categoria
        List<Produto> findByRestauranteIdAndCategoria(Long restauranteId, String categoria);

        // Contar produtos disponiveis por restaurante
        @Query("SELECT COUNT(p) FROM Produto p WHERE p.restaurante.id = :restauranteId AND p.disponivel = true")
        Long countByRestauranteId(@Param("restauranteId") Long restauranteId);

}
