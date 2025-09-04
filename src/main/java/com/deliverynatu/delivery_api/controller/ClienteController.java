package com.deliverynatu.delivery_api.controller;

import com.deliverynatu.delivery_api.dto.request.ClienteRequest;
import com.deliverynatu.delivery_api.dto.response.ClienteResponse;
import com.deliverynatu.delivery_api.entity.Cliente;
import com.deliverynatu.delivery_api.service.ClienteService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
@Tag(name = "Clientes", description = "API para gerenciamento de clientes")
public class ClienteController {

    // 1. Atributos agora são 'final' para garantir imutabilidade após a injeção.
    private final ClienteService clienteService;
    private final ModelMapper modelMapper;

    // 2. A injeção é feita através do construtor.
    @Autowired

    public ClienteController(ClienteService clienteService, ModelMapper modelMapper) {
        this.clienteService = clienteService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> cadastrarCliente(@Valid @RequestBody ClienteRequest clienteRequest) {
        Cliente cliente = modelMapper.map(clienteRequest, Cliente.class);
        Cliente clienteSalvo = clienteService.cadastrarCliente(cliente);
        ClienteResponse clienteResponse = modelMapper.map(clienteSalvo, ClienteResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteResponse);
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listarClientes() {
        List<Cliente> clientes = clienteService.listarAtivos();
        List<ClienteResponse> clientesResponse = clientes.stream()
                .map(cliente -> modelMapper.map(cliente, ClienteResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(clientesResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable Long id) {
        Optional<Cliente> clienteOptional = clienteService.buscarPorId(id);

        if (clienteOptional.isPresent()) {
            ClienteResponse clienteResponse = modelMapper.map(clienteOptional.get(), ClienteResponse.class);
            return ResponseEntity.ok(clienteResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> atualizarCliente(@PathVariable Long id,
            @Valid @RequestBody ClienteRequest clienteRequest) {
        Cliente cliente = modelMapper.map(clienteRequest, Cliente.class);
        Cliente clienteAtualizado = clienteService.atualizar(id, cliente);
        ClienteResponse clienteResponse = modelMapper.map(clienteAtualizado, ClienteResponse.class);
        return ResponseEntity.ok(clienteResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> inativarCliente(@PathVariable Long id) {
        clienteService.inativar(id);
        return ResponseEntity.ok("Cliente inativado com sucesso");
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ClienteResponse> buscarPorEmail(@PathVariable String email) {
        Optional<Cliente> clienteOptional = clienteService.buscarPorEmail(email);

        if (clienteOptional.isPresent()) {
            ClienteResponse clienteResponse = modelMapper.map(clienteOptional.get(), ClienteResponse.class);
            return ResponseEntity.ok(clienteResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ClienteResponse>> buscarPorNome(@RequestParam String nome) {
        List<Cliente> clientes = clienteService.buscarPorNome(nome);
        List<ClienteResponse> clientesResponse = clientes.stream()
                .map(cliente -> modelMapper.map(cliente, ClienteResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(clientesResponse);
    }
}