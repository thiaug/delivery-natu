package com.deliverynatu.delivery_api.controller;

import com.deliverynatu.delivery_api.dto.request.ItemPedidoRequest;
import com.deliverynatu.delivery_api.dto.request.PedidoRequest;
import com.deliverynatu.delivery_api.dto.response.PedidoResponse;
import com.deliverynatu.delivery_api.entity.Pedido;
import com.deliverynatu.delivery_api.enums.StatusPedido;
import com.deliverynatu.delivery_api.service.PedidoService;
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
    public ResponseEntity<PedidoResponse> criarPedido(@Valid @RequestBody PedidoRequest pedidoRequest) {
        Pedido pedido = pedidoService.criarPedido(pedidoRequest.getClienteId(), pedidoRequest.getRestauranteId());
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponse(pedido));
    }

    @PostMapping("/{pedidoId}/itens")
    public ResponseEntity<PedidoResponse> adicionarItem(@PathVariable Long pedidoId,
            @Valid @RequestBody ItemPedidoRequest itemRequest) {
        Pedido pedido = pedidoService.adicionarItem(pedidoId, itemRequest.getProdutoId(), itemRequest.getQuantidade());
        return ResponseEntity.ok(convertToResponse(pedido));
    }

    @PutMapping("/{pedidoId}/confirmar")
    public ResponseEntity<PedidoResponse> confirmarPedido(@PathVariable Long pedidoId) {
        Pedido pedido = pedidoService.confirmarPedido(pedidoId);
        return ResponseEntity.ok(convertToResponse(pedido));
    }

    @GetMapping("/{id}") // Corrigido de PUT para GET
    public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable Long id) {
        return pedidoService.buscarPorId(id)
                .map(pedido -> ResponseEntity.ok(convertToResponse(pedido)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PedidoResponse>> listarPorCliente(@PathVariable Long clienteId) {
        List<Pedido> pedidos = pedidoService.listarPorCliente(clienteId);
        List<PedidoResponse> responses = pedidos.stream().map(this::convertToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/numero/{numeroPedido}")
    public ResponseEntity<PedidoResponse> buscarPorNumero(@PathVariable String numeroPedido) {
        return pedidoService.buscarPorNumero(numeroPedido)
                .map(pedido -> ResponseEntity.ok(convertToResponse(pedido)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{pedidoId}/status")
    public ResponseEntity<PedidoResponse> atualizarStatus(@PathVariable Long pedidoId,
            @RequestParam StatusPedido status) {
        Pedido pedido = pedidoService.atualizarStatus(pedidoId, status);
        return ResponseEntity.ok(convertToResponse(pedido));
    }

    @PutMapping("/{pedidoId}/cancelar")
    public ResponseEntity<PedidoResponse> cancelarPedido(@PathVariable Long pedidoId,
            @RequestParam(required = false) String motivo) {
        Pedido pedido = pedidoService.cancelarPedido(pedidoId, motivo);
        return ResponseEntity.ok(convertToResponse(pedido));
    }
}