package com.deliverynatu.delivery_api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deliverynatu.delivery_api.entity.Pedido;
import com.deliverynatu.delivery_api.enums.StatusPedido;
import com.deliverynatu.delivery_api.service.PedidoService;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    // Criar novo pedido
    @PostMapping
    public ResponseEntity<?> criarPedido(@RequestParam Long clienteId, @RequestParam Long restauranteId) {
        try {
            Pedido pedido = pedidoService.criarPedido(clienteId, restauranteId);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedido);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro:" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao interno do servidor");
        }
    }

    // Adicionar item ao pedido
    @PostMapping("/{pedidoId}/itens")
    public ResponseEntity<?> adicionarItem(@PathVariable Long pedidoId, @RequestParam Long produtoId,
            @RequestParam Integer quantidade) {
        try {
            Pedido pedido = pedidoService.adicionarItem(pedidoId, produtoId, quantidade);
            return ResponseEntity.ok(pedido);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro:" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao interno do servidor");
        }
    }

    // Confirmar pedido
    @PutMapping("/{pedidoId}/confirmar")
    public ResponseEntity<?> confirmarPedido(@PathVariable Long pedidoId) {
        try {
            Pedido pedido = pedidoService.confirmarPedido(pedidoId);
            return ResponseEntity.ok(pedido);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro:" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao interno do servidor");
        }
    }

    // Buscar pedido por ID
    @PutMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Optional<Pedido> pedido = pedidoService.buscarPorId(id);

        if (pedido.isPresent()) {
            return ResponseEntity.ok(pedido.get());
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    // listar pedidos por cliente
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Pedido>> listarPorCliente(@PathVariable Long clienteId) {
        List<Pedido> pedidos = pedidoService.listarPorCliente(clienteId);
        return ResponseEntity.ok(pedidos);
    }

    // Buscar pedido por numero
    @GetMapping("/numero/{numeroPedido}")
    public ResponseEntity<?> buscarPorNumero(@PathVariable String numeroPedido) {
        Optional<Pedido> pedido = pedidoService.buscarPorNumero(numeroPedido);
        if (pedido.isPresent()) {
            return ResponseEntity.ok(pedido.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Atualizar Status do pedido
    @PutMapping("/{pedidoId}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long pedidoId,
            @RequestParam StatusPedido status) {
        try {
            Pedido pedido = pedidoService.atualizarStatus(pedidoId, status);
            return ResponseEntity.ok(pedido);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }

    // Cancelar pedido
    @PutMapping("/{pedidoId}/cancelar")
    public ResponseEntity<?> cancelarPedido(@PathVariable Long pedidoId,
            @RequestParam(required = false) String moƟvo) {
        try {
            Pedido pedido = pedidoService.cancelarPedido(pedidoId, moƟvo);
            return ResponseEntity.ok(pedido);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }
}
