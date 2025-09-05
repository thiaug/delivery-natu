package com.deliverynatu.delivery_api.controller;

import com.deliverynatu.delivery_api.dto.request.ItemPedidoRequest;
import com.deliverynatu.delivery_api.dto.request.PedidoRequest;
import com.deliverynatu.delivery_api.dto.response.PedidoResponse;
import com.deliverynatu.delivery_api.entity.Pedido;
import com.deliverynatu.delivery_api.enums.StatusPedido;
import com.deliverynatu.delivery_api.service.PedidoService;

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
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
@Tag(name = "Pedidos", description = "Operações relacionadas a gerenciamento de pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final ModelMapper modelMapper;

    @Autowired
    public PedidoController(PedidoService pedidoService, ModelMapper modelMapper) {
        this.pedidoService = pedidoService;
        this.modelMapper = modelMapper;
    }

    private PedidoResponse convertToResponse(Pedido pedido) {
        return modelMapper.map(pedido, PedidoResponse.class);
    }

    @PostMapping
    @Operation(summary = "Criar um novo pedido", description = "Endpoint para criar um novo pedido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum pedido encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente ou restaurante não encontrado"),
            @ApiResponse(responseCode = "409", description = "Pedido já existe"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<PedidoResponse> criarPedido(
            @Valid @RequestBody @Parameter(name = "pedidoRequest", required = true, description = "Dados do pedido") PedidoRequest pedidoRequest) {
        Pedido pedido = pedidoService.criarPedido(pedidoRequest.getClienteId(), pedidoRequest.getRestauranteId());
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponse(pedido));
    }

    @PostMapping("/{pedidoId}/itens")
    @Operation(summary = "Adicionar item ao pedido", description = "Endpoint para adicionar um item ao pedido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item adicionado ao pedido"),
            @ApiResponse(responseCode = "201", description = "Item adicionado com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum item encontrado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação dos dados"),
            @ApiResponse(responseCode = "404", description = "Pedido ou produto não encontrado"),
            @ApiResponse(responseCode = "409", description = "Item já existe no pedido"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<PedidoResponse> adicionarItem(
            @PathVariable @Parameter(name = "pedidoId", required = true, description = "ID do pedido") Long pedidoId,
            @Valid @RequestBody ItemPedidoRequest itemRequest) {
        Pedido pedido = pedidoService.adicionarItem(pedidoId, itemRequest.getProdutoId(), itemRequest.getQuantidade());
        return ResponseEntity.ok(convertToResponse(pedido));
    }

    @PutMapping("/{pedidoId}/confirmar")
    @Operation(summary = "Confirmar pedido", description = "Endpoint para confirmar um pedido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido confirmado"),
            @ApiResponse(responseCode = "201", description = "Pedido confirmado com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum pedido encontrado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação dos dados"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "409", description = "Pedido já confirmado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<PedidoResponse> confirmarPedido(
            @PathVariable @Parameter(name = "pedidoId", required = true, description = "ID do pedido") Long pedidoId) {
        Pedido pedido = pedidoService.confirmarPedido(pedidoId);
        return ResponseEntity.ok(convertToResponse(pedido));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID", description = "Endpoint para buscar um pedido por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "201", description = "Pedido cadastrado com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum pedido encontrado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação dos dados"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "409", description = "Pedido já existe"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<PedidoResponse> buscarPorId(
            @PathVariable @Parameter(name = "id", required = true, description = "ID do pedido") Long id) {
        return pedidoService.buscarPorId(id)
                .map(pedido -> ResponseEntity.ok(convertToResponse(pedido)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar pedidos por cliente", description = "Endpoint para listar pedidos por cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de pedidos encontrada"),
            @ApiResponse(responseCode = "201", description = "Lista de pedidos cadastrada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum pedido encontrado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação dos dados"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "409", description = "Pedido já existe"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<PedidoResponse>> listarPorCliente(
            @PathVariable @Parameter(name = "clienteId", required = true, description = "ID do cliente") Long clienteId) {
        List<Pedido> pedidos = pedidoService.listarPorCliente(clienteId);
        List<PedidoResponse> responses = pedidos.stream().map(this::convertToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/numero/{numeroPedido}")
    @Operation(summary = "Buscar pedido por número", description = "Endpoint para buscar um pedido por número")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "201", description = "Pedido cadastrado com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum pedido encontrado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação dos dados"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "409", description = "Pedido já existe"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<PedidoResponse> buscarPorNumero(
            @PathVariable @Parameter(name = "numeroPedido", required = true, description = "Número do pedido") String numeroPedido) {
        return pedidoService.buscarPorNumero(numeroPedido)
                .map(pedido -> ResponseEntity.ok(convertToResponse(pedido)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{pedidoId}/status")
    @Operation(summary = "Atualizar status do pedido", description = "Endpoint para atualizar o status de um pedido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status do pedido atualizado"),
            @ApiResponse(responseCode = "201", description = "Status do pedido atualizado com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum pedido encontrado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação dos dados"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "409", description = "Pedido já existe"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<PedidoResponse> atualizarStatus(
            @PathVariable @Parameter(name = "pedidoId", required = true, description = "ID do pedido") Long pedidoId,
            @RequestParam StatusPedido status) {
        Pedido pedido = pedidoService.atualizarStatus(pedidoId, status);
        return ResponseEntity.ok(convertToResponse(pedido));
    }

    @PutMapping("/{pedidoId}/cancelar")
    @Operation(summary = "Cancelar pedido", description = "Endpoint para cancelar um pedido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido cancelado"),
            @ApiResponse(responseCode = "201", description = "Pedido cancelado com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum pedido encontrado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação dos dados"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "409", description = "Pedido já existe"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<PedidoResponse> cancelarPedido(
            @PathVariable @Parameter(name = "pedidoId", required = true, description = "ID do pedido") Long pedidoId,
            @RequestParam(required = false) String motivo) {
        Pedido pedido = pedidoService.cancelarPedido(pedidoId, motivo);
        return ResponseEntity.ok(convertToResponse(pedido));
    }
}