package com.deliverynatu.delivery_api.repository;

import com.deliverynatu.delivery_api.entity.Restaurante;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    // Buscar restaurante por nome
    Optional<Restaurante> findByNome(String nome);

    Optional<Restaurante> findByNomeContainingIgnoreCase(String nome);

    // Verifica se existe por nome
    boolean existsByNome(String nome);

    // Buscar por categoria
    List<Restaurante> findByCategoria(String categoria);

    // Buscar restaurantes ativos
    List<Restaurante> findByAtivoTrue();

    // Buscar restaurante por categoria e ativo
    List<Restaurante> findByCategoriaAndAtivoTrue(String categoria);

    // Por taxa de entrega menor ou igual
    List<Restaurante> findByTaxaEntregaLessThanEqual(BigDecimal taxa);

    // Top 5 restaurantes por nome (ordem alfabetica)
    List<Restaurante> findTop5ByOrderByNomeAsc();

    // Buscar restaurante por nome contendo (case insensitive)
    Optional<Restaurante> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);

    // Buscar restaurante por avaliação minima
    List<Restaurante> findByAvaliacaoGreaterThanEqualAndAtivo(BigDecimal avaliacao, boolean ativo);

    // Ordenar restaurantes por avaliação
    List<Restaurante> findByAtivoTrueOrderByAvaliacaoDesc();

    // Query customizada - restaurantes com produtos
    @Query("SELECT DISTINCT r FROM Restaurante r JOIN r.produtos p WHERE r.ativo = true AND p.disponivel = true")
    List<Restaurante> findRestaurantesComProdutos();

    @Query("SELECT r.nome as nomeRestaurante, " +
            "SUM(p.valorTotal) as totalVendas, " +
            "COUNT(p.id) as quantidadePedidos " +
            "FROM Restaurante r " +
            "LEFT JOIN Pedido p ON r.id = p.restaurante.id " +
            "GROUP BY r.id, r.nome")
    List<RelatorioVendas> relatorioVendasPorRestaurante();

    // Buscar por faixa de tava de entrega
    @Query("SELECT r FROM Restaurante r WHERE r.taxaEntrega BETWEEN :min AND :max AND r.ativo = true")
    List<Restaurante> findByTaxaEntregaBetween(@Param("min") BigDecimal min, @Param("max") BigDecimal max);

    // Categorias disponíveis
    @Query("SELECT DISTINCT r.categoria FROM Restaurante r WHERE r.ativo = true ORDER BY r.categoria")
    List<String> findCategoriasDisponiveis();

}
