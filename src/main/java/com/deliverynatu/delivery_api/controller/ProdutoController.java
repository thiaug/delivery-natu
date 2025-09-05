package com.deliverynatu.delivery_api.controller;

import com.deliverynatu.delivery_api.dto.request.ProdutoRequest;
import com.deliverynatu.delivery_api.dto.response.ProdutoResponse;
import com.deliverynatu.delivery_api.entity.Produto;
import com.deliverynatu.delivery_api.service.ProdutoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@CrossOrigin(origins = "*")
@Tag(name = "Produtos", description = "Operações relacionadas a gerenciamento de produtos")
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
        @Operation(summary = "Cadastrar um novo produto", description = "Endpoint para cadastrar um novo produto")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Produto cadastrado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Erro de validação dos dados"),
                        @ApiResponse(responseCode = "409", description = "O produto já existe"),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        })
        public ResponseEntity<ProdutoResponse> cadastrar(
                        @Valid @RequestBody @Parameter(name = "produtoRequest", required = true, description = "Dados do produto a ser cadastrado") ProdutoRequest produtoRequest,
                        @PathVariable Long restauranteId) {
                Produto produto = modelMapper.map(produtoRequest, Produto.class);
                Produto novoProduto = produtoService.cadastrar(produto, restauranteId);
                return new ResponseEntity<>(convertToResponse(novoProduto), HttpStatus.CREATED);
        }

        @GetMapping("/{id}")
        @Operation(summary = "Buscar produto por ID", description = "Endpoint para buscar um produto por ID")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Produto encontrado"),
                        @ApiResponse(responseCode = "400", description = "Erro de validação dos dados"),
                        @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        })
        public ResponseEntity<ProdutoResponse> buscarPorId(
                        @PathVariable @Parameter(name = "id", required = true, description = "Buscar por ID do produto") Long id) {
                return produtoService.buscarPorId(id)
                                .map(produto -> ResponseEntity.ok(convertToResponse(produto)))
                                .orElse(ResponseEntity.notFound().build());
        }

        @GetMapping("/disponiveis")
        @Operation(summary = "Listar produtos disponíveis", description = "Endpoint para listar todos os produtos disponíveis")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Lista de produtos encontrados"),
                        @ApiResponse(responseCode = "400", description = "Erro de validação dos dados"),
                        @ApiResponse(responseCode = "404", description = "Nenhum produto encontrado"),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        })
        public ResponseEntity<List<ProdutoResponse>> listarTodos() {
                List<ProdutoResponse> responses = produtoService.listarTodosDisponiveis().stream()
                                .map(this::convertToResponse)
                                .collect(Collectors.toList());
                return ResponseEntity.ok(responses);
        }

        @GetMapping("/restaurante/{restauranteId}")
        @Operation(summary = "Listar produtos por restaurante", description = "Endpoint para listar produtos por restaurante")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Lista de produtos encontrada"),
                        @ApiResponse(responseCode = "400", description = "Erro de validação dos dados"),
                        @ApiResponse(responseCode = "404", description = "Nenhum produto encontrado"),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        })
        public ResponseEntity<List<ProdutoResponse>> listarPorRestaurante(
                        @PathVariable @Parameter(name = "restauranteId", required = true, description = "Listar por ID do restaurante") Long restauranteId) {
                List<ProdutoResponse> responses = produtoService.listarPorRestaurante(restauranteId).stream()
                                .map(this::convertToResponse)
                                .collect(Collectors.toList());
                return ResponseEntity.ok(responses);
        }

        @GetMapping("/categoria/{categoria}")
        @Operation(summary = "Buscar produtos por categoria", description = "Endpoint para buscar produtos por categoria")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Produtos encontrados"),
                        @ApiResponse(responseCode = "400", description = "Erro de validação dos dados"),
                        @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        })
        public ResponseEntity<List<ProdutoResponse>> buscarPorCategoria(
                        @PathVariable @Parameter(name = "categoria", required = true, description = "Categoria dos produtos") String categoria) {
                List<ProdutoResponse> responses = produtoService.buscarPorCategoria(categoria).stream()
                                .map(this::convertToResponse)
                                .collect(Collectors.toList());
                return ResponseEntity.ok(responses);
        }

        @GetMapping("/preco")
        @Operation(summary = "Buscar produtos por faixa de preço", description = "Endpoint para buscar produtos por faixa de preço")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Produtos encontrados"),
                        @ApiResponse(responseCode = "400", description = "Erro de validação dos dados"),
                        @ApiResponse(responseCode = "404", description = "Nenhum produto encontrado"),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        })
        public ResponseEntity<List<ProdutoResponse>> buscarPorFaixaDePreco(
                        @RequestParam @Parameter(name = "precoMin", required = true, description = "Preço mínimo") BigDecimal precoMin,
                        @RequestParam @Parameter(name = "precoMax", required = true, description = "Preço máximo") BigDecimal precoMax) {
                List<ProdutoResponse> responses = produtoService.buscarPorFaixaDePreco(precoMin, precoMax).stream()
                                .map(this::convertToResponse)
                                .collect(Collectors.toList());
                return ResponseEntity.ok(responses);
        }

        @PutMapping("/{id}")
        @Operation(summary = "Atualizar produto", description = "Endpoint para atualizar um produto")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Erro de validação dos dados"),
                        @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        })
        public ResponseEntity<ProdutoResponse> atualizar(@PathVariable Long id,
                        @Valid @RequestBody @Parameter(name = "id", required = true, description = "ID do produto") ProdutoRequest produtoRequest) {
                Produto produto = modelMapper.map(produtoRequest, Produto.class);
                Produto produtoAtualizado = produtoService.atualizar(id, produto);
                return ResponseEntity.ok(convertToResponse(produtoAtualizado));
        }

        @PatchMapping("/{id}/disponibilidade")
        @Operation(summary = "Alterar disponibilidade do produto", description = "Endpoint para alterar a disponibilidade de um produto")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Disponibilidade do produto alterada com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Erro de validação dos dados"),
                        @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")

        })
        public ResponseEntity<Void> alterarDisponibilidade(@PathVariable Long id,
                        @RequestParam @Parameter(name = "disponivel", required = true, description = "Disponibilidade do produto") boolean disponivel) {
                produtoService.alterarDisponibilidade(id, disponivel);
                return ResponseEntity.noContent().build();
        }
}