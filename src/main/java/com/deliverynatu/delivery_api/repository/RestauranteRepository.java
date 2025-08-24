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

    // Buscar restaurantes ativos
    List<Restaurante> findByAtivoTrue();

    // Buscar restaurante por categoria
    List<Restaurante> findByCategoriaAndAtivoTrue(String categoria);

    // Buscar restaurante por nome contendo (case insensitive)
    List<Restaurante> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);

    // Buscar restaurante por avaliação minima
    List<Restaurante> findByAvaliacaoGreaterThanEqualAndAtivo(BigDecimal avaliacao);

    // Ordenar restaurantes por avaliação
    List<Restaurante> findByAtivoTrueOrderByAvaliacaoDesc();

    // Query customizada - restaurantes com produtos
    @Query("SELECT DISTINCT r FROM Restaurante r JOIN r.produtos p WHERE r.ativo = true AND p.ativo = true ")
    List<Restaurante> findRestaurantesComProdutos();

    // Buscar por faixa de tava de entrega
    @Query("SELECT r FROM Restaurante r WHRE r.taxaEntrega BETWEEN :min AND :max AND r.ativo = true")
    List<Restaurante> findByTaxaEntregaBetween(@Param("min") BigDecimal min, @Param("max") BigDecimal max);

    // Categorias disponíveis
    @Query("SELECT DISTINCT r.categoria FROM Restaurante r WHERE r.ativo = true ORDER BY r.categoria")
    List<String> findCategoriasDisponiveis();

}
