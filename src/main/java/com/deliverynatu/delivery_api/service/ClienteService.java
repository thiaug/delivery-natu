package com.deliverynatu.delivery_api.service;

import com.deliverynatu.delivery_api.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {

    Cliente cadastrarCliente(Cliente cliente);

    Optional<Cliente> buscarPorId(Long id);

    Optional<Cliente> buscarPorEmail(String email);

    List<Cliente> listarAtivos();

    Cliente atualizar(Long id, Cliente clienteAtualizado);

    void inativar(Long id);

    List<Cliente> buscarPorNome(String nome);
}
