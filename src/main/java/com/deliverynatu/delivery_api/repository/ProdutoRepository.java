package com.deliverynatu.delivery_api.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deliverynatu.delivery_api.entity.Produto;
import com.deliverynatu.delivery_api.entity.Restaurante;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    // Buscar produtos disponiveis de um restaurante
    List<Produto> findByRestauranteAndDisponivelTrue(Restaurante restaurante);

    // Buscar produtos disponiveis por restauranteId
    List<Produto> findByRestauranteIdAndDisponivelTrue(Long restauranteId);

    // Buscar produtos disponiveis por categoria
    List<Produto> findByCategoriaAndDisponivelTrue(String categoria);

    // Buscar por nome contendo
    List<Produto> findByNomeContainingIgnoreCaseAndDisponivelTrue(String nome);

    // Buscar por faixa de preço
    List<Produto> findByPrecoBetweenAndDisponivelTrue(BigDecimal precoMin, BigDecimal precoMax);

    // Buscar produtos mais baratos que um determinado preço
    List<Produto> findByPrecoLessThanEqualAndDisponivelTrue(BigDecimal preco);

    // Ordenar produtos por preço ascendente
    List<Produto> findByDisponivelTrueOrderByPrecoAsc();

    // Ordenar produtos por preço descendente
    List<Produto> findByDisponivelTrueOrderByPrecoDesc();

    // Produtos mais vendidos
    @Query("SELECT ip.produto FROM ItemPedido ip GROUP BY ip.produto ORDER BY COUNT(ip) DESC")
    List<Produto> findProdutosMaisVendidos();

    // Buscar produtos por restaurante e categoria
    List<Produto> findByRestauranteAndCategoria(@Param("restauranteId") Restaurante restaurante,
            @Param("categoria") String categoria);

    // List<Produto> findByRestauranteAndCategoria(@Param("restauranteId") Long
    // restauranteId,
    // @Param("categoria") String categoria);

    // Contar produtos disponiveis por restaurante
    @Query("SELECT COUNT(p) FROM Produto p WHERE p.restaurante.id = :restauranteId AND p.disponivel = true")
    Long countByRestauranteId();

}
