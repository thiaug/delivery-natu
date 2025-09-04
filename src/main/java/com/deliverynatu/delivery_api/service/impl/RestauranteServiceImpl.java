package com.deliverynatu.delivery_api.service.impl;

import com.deliverynatu.delivery_api.entity.Restaurante;
import com.deliverynatu.delivery_api.repository.RestauranteRepository;
import com.deliverynatu.delivery_api.service.RestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RestauranteServiceImpl implements RestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Override
    public Restaurante cadastrar(Restaurante restaurante) {
        if (restauranteRepository.findByNomeContainingIgnoreCase(restaurante.getNome()).isPresent()) {
            throw new IllegalArgumentException("Restaurante já cadastrado com o nome: " + restaurante.getNome());
        }
        restaurante.setAtivo(true);
        return restauranteRepository.save(restaurante);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Restaurante> buscarPorNome(String nome) {
        return restauranteRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Restaurante> buscarPorId(Long id) {
        return restauranteRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Restaurante> listarAtivos() {
        return restauranteRepository.findByAtivoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Restaurante> buscarPorCategoria(String categoria) {
        return restauranteRepository.findByCategoriaAndAtivoTrue(categoria);
    }

    @Override
    public Restaurante atualizar(Long id, Restaurante restauranteAtualizado) {
        Restaurante restaurante = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado com ID: " + id));

        if (!restaurante.getNome().equals(restauranteAtualizado.getNome()) &&
                restauranteRepository.findByNomeContainingIgnoreCase(restauranteAtualizado.getNome()).isPresent()) {
            throw new IllegalArgumentException(
                    "Restaurante já cadastrado com o nome: " + restauranteAtualizado.getNome());
        }

        restaurante.setNome(restauranteAtualizado.getNome());
        restaurante.setCategoria(restauranteAtualizado.getCategoria());
        restaurante.setEndereco(restauranteAtualizado.getEndereco());
        restaurante.setTelefone(restauranteAtualizado.getTelefone());
        restaurante.setTaxaEntrega(restauranteAtualizado.getTaxaEntrega());

        return restauranteRepository.save(restaurante);
    }

    @Override
    public void inativar(Long id) {
        Restaurante restaurante = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado com ID: " + id));
        restaurante.setAtivo(false);
        restauranteRepository.save(restaurante);
    }
}