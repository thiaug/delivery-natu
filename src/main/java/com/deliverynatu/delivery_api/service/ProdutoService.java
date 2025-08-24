package com.deliverynatu.delivery_api.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverynatu.delivery_api.entity.Produto;
import com.deliverynatu.delivery_api.entity.Restaurante;
import com.deliverynatu.delivery_api.repository.ProdutoRepository;
import com.deliverynatu.delivery_api.repository.RestauranteRepository;

@Service
@Transactional
public class ProdutoService {

    // Injeção de dependência do repositório de produtos
    @Autowired
    private ProdutoRepository produtoRepository;

    // Injeção de dependência do repositório de restaurantes
    @Autowired
    private RestauranteRepository restauranteRepository;

    // Método para cadastrar um novo produto
    public Produto cadastrar(Produto produto, Long restauranteId) {
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado com ID: " + restauranteId));

        validarDadosProduto(produto);

        return produtoRepository.save(produto);
    }

    // Buscar por ID
    @Transactional(readOnly = true)
    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    // Listar produtos por restaurante
    @Transactional(readOnly = true)
    public List<Produto> listarPorRestaurante(Long restauranteId) {
        return produtoRepository.findByRestauranteIdAndDisponivelTrue(restauranteId);
    }

    // Buscar por categoria
    @Transactional(readOnly = true)
    public List<Produto> buscarPorCategoria(String categoria) {
        return produtoRepository.findByCategoriaAndDisponivelTrue(categoria);
    }

    // Atualizar produto
    public Produto atualizar(Long id, Produto produtoAtualizado) {
        Produto produto = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado com ID: " + id));

        validarDadosProduto(produtoAtualizado);

        produto.setNome(produtoAtualizado.getNome());
        produto.setDescricao(produtoAtualizado.getDescricao());
        produto.setPreco(produtoAtualizado.getPreco());
        produto.setCategoria(produtoAtualizado.getCategoria());

        return produtoRepository.save(produto);

    }

    // Alterar Disponibilidade
    public void alterarDisponibilidade(Long id, boolean disponivel) {
        Produto produto = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado com ID: " + id));
        produto.setDisponivel(disponivel);
        produtoRepository.save(produto);
    }

    // Buscar por faixa de preço
    @Transactional(readOnly = true)
    public List<Produto> buscarPorFaixaDePreco(BigDecimal precoMin, BigDecimal precoMax) {
        return produtoRepository.findByPrecoBetweenAndDisponivelTrue(precoMin, precoMax);
    }

    // Validações do produto
    private void validarDadosProduto(Produto produto) {
        // Implementar validações de negócio para o produto
        if (produto.getNome() == null || produto.getNome().isEmpty()) {
            throw new IllegalArgumentException("O nome do produto não pode ser vazio.");
        }
        if (produto.getPreco() == null || produto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preço do produto deve ser maior que zero.");
        }
    }

}
