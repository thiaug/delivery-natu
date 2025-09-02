package com.deliverynatu.delivery_api.controller;

import com.deliverynatu.delivery_api.dto.request.ProdutoRequest;
import com.deliverynatu.delivery_api.dto.response.ProdutoResponse;
import com.deliverynatu.delivery_api.entity.Produto;
import com.deliverynatu.delivery_api.service.ProdutoService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProdutoController(ProdutoService produtoService, ModelMapper modelMapper) {
        this.produtoService = produtoService;
        this.modelMapper = modelMapper;
    }

    private ProdutoResponse convertToResponse(Produto produto) {
        return modelMapper.map(produto, ProdutoResponse.class);
    }

    @PostMapping("/restaurante/{restauranteId}")
    public ResponseEntity<ProdutoResponse> cadastrar(@Valid @RequestBody ProdutoRequest produtoRequest,
            @PathVariable Long restauranteId) {
        Produto produto = modelMapper.map(produtoRequest, Produto.class);
        Produto novoProduto = produtoService.cadastrar(produto, restauranteId);
        return new ResponseEntity<>(convertToResponse(novoProduto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id)
                .map(produto -> ResponseEntity.ok(convertToResponse(produto)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listarTodos() {
        List<ProdutoResponse> responses = produtoService.listarTodosDisponiveis().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<List<ProdutoResponse>> listarPorRestaurante(@PathVariable Long restauranteId) {
        List<ProdutoResponse> responses = produtoService.listarPorRestaurante(restauranteId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProdutoResponse>> buscarPorCategoria(@PathVariable String categoria) {
        List<ProdutoResponse> responses = produtoService.buscarPorCategoria(categoria).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/preco")
    public ResponseEntity<List<ProdutoResponse>> buscarPorFaixaDePreco(@RequestParam BigDecimal precoMin,
            @RequestParam BigDecimal precoMax) {
        List<ProdutoResponse> responses = produtoService.buscarPorFaixaDePreco(precoMin, precoMax).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(@PathVariable Long id,
            @Valid @RequestBody ProdutoRequest produtoRequest) {
        Produto produto = modelMapper.map(produtoRequest, Produto.class);
        Produto produtoAtualizado = produtoService.atualizar(id, produto);
        return ResponseEntity.ok(convertToResponse(produtoAtualizado));
    }

    @PatchMapping("/{id}/disponibilidade")
    public ResponseEntity<Void> alterarDisponibilidade(@PathVariable Long id, @RequestParam boolean disponivel) {
        produtoService.alterarDisponibilidade(id, disponivel);
        return ResponseEntity.noContent().build();
    }
}