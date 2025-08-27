package com.deliverynatu.delivery_api.controller;

import com.deliverynatu.delivery_api.entity.Produto;
import com.deliverynatu.delivery_api.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    // Criar produto em um restaurante
    @PostMapping("/restaurante/{restauranteId}")
    public ResponseEntity<Produto> cadastrar(@RequestBody Produto produto, @PathVariable Long restauranteId) {
        return ResponseEntity.ok(produtoService.cadastrar(produto, restauranteId));
    }

    // Buscar produto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Listar todos os produtos disponíveis
    @GetMapping
    public ResponseEntity<List<Produto>> listarTodos() {
        return ResponseEntity.ok(produtoService.listarTodosDisponiveis());
    }

    // Listar produtos por restaurante
    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<List<Produto>> listarPorRestaurante(@PathVariable Long restauranteId) {
        return ResponseEntity.ok(produtoService.listarPorRestaurante(restauranteId));
    }

    // Buscar por categoria
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Produto>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(produtoService.buscarPorCategoria(categoria));
    }

    // Buscar por faixa de preço
    @GetMapping("/preco")
    public ResponseEntity<List<Produto>> buscarPorFaixaDePreco(@RequestParam BigDecimal precoMin,
            @RequestParam BigDecimal precoMax) {
        return ResponseEntity.ok(produtoService.buscarPorFaixaDePreco(precoMin, precoMax));
    }

    // Atualizar produto
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @RequestBody Produto produtoAtualizado) {
        try {
            return ResponseEntity.ok(produtoService.atualizar(id, produtoAtualizado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Alterar disponibilidade do produto
    @PatchMapping("/{id}/disponibilidade")
    public ResponseEntity<Void> alterarDisponibilidade(@PathVariable Long id, @RequestParam boolean disponivel) {
        try {
            produtoService.alterarDisponibilidade(id, disponivel);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
