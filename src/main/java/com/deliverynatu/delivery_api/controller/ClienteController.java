package com.deliverynatu.delivery_api.controller;

import com.deliverynatu.delivery_api.dto.request.ClienteRequest;
import com.deliverynatu.delivery_api.dto.response.ClienteResponse;
import com.deliverynatu.delivery_api.entity.Cliente;
import com.deliverynatu.delivery_api.service.ClienteService;

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
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
@Tag(name = "Clientes", description = "API para gerenciamento de clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final ModelMapper modelMapper;

    @Autowired
    public ClienteController(ClienteService clienteService, ModelMapper modelMapper) {
        this.clienteService = clienteService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @Operation(summary = "Cadastrar um novo cliente", description = "Endpoint para cadastrar um novo cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum cliente encontrado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação dos dados"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "409", description = "O cliente já existe"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<ClienteResponse> cadastrarCliente(
            @Valid @Parameter(name = "clienteRequest", required = true, description = "Dados do cliente a ser cadastrado") @RequestBody ClienteRequest clienteRequest) {
        Cliente cliente = modelMapper.map(clienteRequest, Cliente.class);
        Cliente clienteSalvo = clienteService.cadastrarCliente(cliente);
        ClienteResponse clienteResponse = modelMapper.map(clienteSalvo, ClienteResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteResponse);
    }

    @GetMapping
    @Operation(summary = "Listar clientes ativos", description = "Endpoint para listar todos os clientes ativos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de clientes encontrada"),
            @ApiResponse(responseCode = "201", description = "Lista de clientes cadastrada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum cliente encontrado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação dos dados"),
            @ApiResponse(responseCode = "404", description = "Nenhum cliente encontrado"),
            @ApiResponse(responseCode = "409", description = "O cliente já existe")
    })
    public ResponseEntity<List<ClienteResponse>> listarClientes(
            @Parameter(description = "Nome ou parte do nome do cliente para busca") @RequestParam(required = false) String nome) {
        List<Cliente> clientes = clienteService.listarAtivos();
        List<ClienteResponse> clientesResponse = clientes.stream()
                .map(cliente -> modelMapper.map(cliente, ClienteResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(clientesResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID", description = "Endpoint para buscar um cliente por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum cliente encontrado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação dos dados"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "409", description = "O cliente já existe"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<ClienteResponse> buscarPorId(
            @PathVariable @Parameter(name = "id", required = true, description = "ID do cliente") Long id) {
        Optional<Cliente> clienteOptional = clienteService.buscarPorId(id);

        if (clienteOptional.isPresent()) {
            ClienteResponse clienteResponse = modelMapper.map(clienteOptional.get(), ClienteResponse.class);
            return ResponseEntity.ok(clienteResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cliente", description = "Endpoint para atualizar um cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "201", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum cliente encontrado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação dos dados"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "409", description = "O cliente já existe"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<ClienteResponse> atualizarCliente(
            @PathVariable @Parameter(name = "id", required = true, description = "ID do cliente") Long id,
            @Valid @RequestBody ClienteRequest clienteRequest) {
        Cliente cliente = modelMapper.map(clienteRequest, Cliente.class);
        Cliente clienteAtualizado = clienteService.atualizar(id, cliente);
        ClienteResponse clienteResponse = modelMapper.map(clienteAtualizado, ClienteResponse.class);
        return ResponseEntity.ok(clienteResponse);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Inativar cliente", description = "Endpoint para inativar um cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente inativado com sucesso"),
            @ApiResponse(responseCode = "201", description = "Cliente inativado com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum cliente encontrado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação dos dados"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "409", description = "O cliente já existe"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<String> inativarCliente(
            @PathVariable @Parameter(name = "id", required = true, description = "ID do cliente") Long id) {
        clienteService.inativar(id);
        return ResponseEntity.ok("Cliente inativado com sucesso");
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Buscar cliente por email", description = "Endpoint para buscar um cliente por email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum cliente encontrado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação dos dados"),
            @ApiResponse(responseCode = "404", description = "Cliente não cadastrado"),
            @ApiResponse(responseCode = "409", description = "O cliente já existe"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<ClienteResponse> buscarPorEmail(
            @PathVariable @Parameter(name = "email", required = true, description = "Email do cliente") String email) {
        Optional<Cliente> clienteOptional = clienteService.buscarPorEmail(email);

        if (clienteOptional.isPresent()) {
            ClienteResponse clienteResponse = modelMapper.map(clienteOptional.get(), ClienteResponse.class);
            return ResponseEntity.ok(clienteResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar clientes por nome", description = "Busca clientes cujo nome contenha o termo pesquisado")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Lista de clientes encontrada"),
            @ApiResponse(responseCode = "201", description = "Lista de clientes cadastrada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum cliente encontrado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação dos dados"),
            @ApiResponse(responseCode = "404", description = "Nenhum cliente encontrado"),
            @ApiResponse(responseCode = "409", description = "O cliente já existe"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })

    public ResponseEntity<List<ClienteResponse>> buscarPorNome(
            @RequestParam @Parameter(description = "Nome ou parte do nome do cliente para busca") String nome) {
        List<Cliente> clientes = clienteService.buscarPorNome(nome);
        List<ClienteResponse> clientesResponse = clientes.stream()
                .map(cliente -> modelMapper.map(cliente, ClienteResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(clientesResponse);
    }
}