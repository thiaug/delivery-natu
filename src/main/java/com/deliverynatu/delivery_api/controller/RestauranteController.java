package com.deliverynatu.delivery_api.controller;

import com.deliverynatu.delivery_api.dto.request.RestauranteRequest;
import com.deliverynatu.delivery_api.dto.response.RestauranteResponse;
import com.deliverynatu.delivery_api.entity.Restaurante;
import com.deliverynatu.delivery_api.service.RestauranteService;

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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/restaurantes")
@CrossOrigin(origins = "*")
@Tag(name = "Restaurantes", description = "Operações relacionadas a gerenciamento restaurantes")
public class RestauranteController {

        private final RestauranteService restauranteService;
        private final ModelMapper modelMapper;

        @Autowired
        public RestauranteController(RestauranteService restauranteService, ModelMapper modelMapper) {
                this.restauranteService = restauranteService;
                this.modelMapper = modelMapper;
        }

        private RestauranteResponse convertToResponse(Restaurante restaurante) {
                return modelMapper.map(restaurante, RestauranteResponse.class);
        }

        @PostMapping
        @Operation(summary = "Cadastrar um novo restaurante", description = "Endpoint para cadastrar um novo restaurante")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Restaurante cadastrado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Erro de validação dos dados"),
                        @ApiResponse(responseCode = "409", description = "O restaurante já existe"),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        })
        public ResponseEntity<RestauranteResponse> cadastrar(
                        @Valid @RequestBody @Parameter(name = "restauranteRequest", required = true, description = "Dados do restaurante a ser cadastrado") RestauranteRequest restauranteRequest) {
                Restaurante restaurante = modelMapper.map(restauranteRequest, Restaurante.class);
                Restaurante novoRestaurante = restauranteService.cadastrar(restaurante);
                return new ResponseEntity<>(convertToResponse(novoRestaurante), HttpStatus.CREATED);
        }

        @GetMapping("/buscar")
        @Operation(summary = "Buscar restaurantes por nome", description = "Busca restaurantes cujo nome contenha o termo pesquisado")
        @ApiResponses({
                        @ApiResponse(responseCode = "400", description = "Erro de validação dos dados"),
                        @ApiResponse(responseCode = "404", description = "Nenhum restaurante encontrado"),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        })
        public ResponseEntity<List<RestauranteResponse>> buscarPorNome(
                        @Parameter(description = "Nome ou parte do nome do restaurante para busca") @RequestParam String nome) {

                List<RestauranteResponse> responses = restauranteService.buscarPorNome(nome).stream()
                                .map(this::convertToResponse)
                                .collect(Collectors.toList());

                return ResponseEntity.ok(responses);
        }

        @GetMapping("/{id}")
        @Operation(summary = "Buscar restaurante por ID", description = "Endpoint para buscar um restaurante por ID")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
                        @ApiResponse(responseCode = "400", description = "Erro de validação dos dados"),
                        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado"),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        })
        public ResponseEntity<RestauranteResponse> buscarPorId(
                        @Parameter(name = "id", required = true, description = "ID do restaurante") @PathVariable Long id) {
                return restauranteService.buscarPorId(id)
                                .map(restaurante -> ResponseEntity.ok(convertToResponse(restaurante)))
                                .orElse(ResponseEntity.notFound().build());
        }

        @PutMapping("/{id}")
        @Operation(summary = "Atualizar restaurante", description = "Endpoint para atualizar um restaurante")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Restaurante atualizado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Erro de validação dos dados"),
                        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado"),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        })
        public ResponseEntity<RestauranteResponse> atualizar(
                        @Valid @Parameter(name = "id", required = true, description = "ID do restaurante") @PathVariable Long id,
                        @RequestBody RestauranteRequest restauranteRequest) {
                Restaurante restaurante = modelMapper.map(restauranteRequest, Restaurante.class);
                Restaurante restauranteAtualizado = restauranteService.atualizar(id, restaurante);
                return ResponseEntity.ok(convertToResponse(restauranteAtualizado));
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Inativar restaurante", description = "Endpoint para inativar um restaurante")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Restaurante inativado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Erro de validação dos dados"),
                        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado"),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        })
        public ResponseEntity<Void> inativar(
                        @PathVariable Long id) {
                restauranteService.inativar(id);
                return ResponseEntity.noContent().build();
        }

        @GetMapping("/ativos")
        @Operation(summary = "Listar restaurantes ativos", description = "Endpoint para listar todos os restaurantes ativos")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Lista de restaurantes encontrada"),
                        @ApiResponse(responseCode = "400", description = "Erro de validação dos dados"),
                        @ApiResponse(responseCode = "404", description = "Nenhum restaurante encontrado"),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        })

        public ResponseEntity<List<RestauranteResponse>> listarAtivos() {
                List<RestauranteResponse> responses = restauranteService.listarAtivos().stream()
                                .map(this::convertToResponse)
                                .collect(Collectors.toList());
                return ResponseEntity.ok(responses);
        }

        @GetMapping("/categoria/{categoria}")
        @Operation(summary = "Buscar restaurantes por categoria", description = "Endpoint para buscar restaurantes por categoria")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Restaurantes encontrados"),
                        @ApiResponse(responseCode = "400", description = "Erro de validação dos dados"),
                        @ApiResponse(responseCode = "404", description = "Nenhum restaurante encontrado"),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        })
        public ResponseEntity<List<RestauranteResponse>> buscarPorCategoria(
                        @Parameter(name = "categoria", required = true, description = "Categoria dos restaurantes") @PathVariable String categoria) {
                List<RestauranteResponse> responses = restauranteService.buscarPorCategoria(categoria).stream()
                                .map(this::convertToResponse)
                                .collect(Collectors.toList());
                return ResponseEntity.ok(responses);
        }

}