package com.deliverynatu.delivery_api.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverynatu.delivery_api.entity.Restaurante;
import com.deliverynatu.delivery_api.repository.RestauranteRepository;

@Service
@Transactional
public class RestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    public Restaurante cadastrar(Restaurante restaurante) {
        // Verifica se já existe um restaurante com o mesmo nome
        if (restauranteRepository.findByNome(restaurante.getNome()).isPresent()) {
            throw new IllegalArgumentException("Restaurante já cadastrado com o nome: " + restaurante.getNome());
        }

        // Validações de negócio
        validarDadosRestaurante(restaurante);

        // Define o status do restaurante como ativo ao cadastrar
        restaurante.setAtivo(true);

        return restauranteRepository.save(restaurante);
    }

    // Buscar por ID
    @Transactional(readOnly = true)
    public Optional<Restaurante> buscarPorId(Long id) {
        return restauranteRepository.findById(id);
    }

    // Listar todos os restaurantes ativos
    @Transactional(readOnly = true)
    public List<Restaurante> listarAtivos() {
        return restauranteRepository.findByAtivoTrue();
    }

    // Buscar por categoria
    @Transactional(readOnly = true)
    public List<Restaurante> buscarPorCategoria(String categoria) {
        return restauranteRepository.findByCategoriaAndAtivoTrue(categoria);
    }

    public Restaurante atualizar(Long id, Restaurante restauranteAtualizado) {
        Restaurante restaurante = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado com ID: " + id));
        // Verifica se o nome foi alterado e se já existe outro restaurante com o novo
        // nome
        if (!restaurante.getNome().equals(restauranteAtualizado.getNome())
                && restauranteRepository.findByNome(restauranteAtualizado.getNome()).isPresent()) {
            throw new IllegalArgumentException(
                    "Restaurante já cadastrado com o nome: " + restauranteAtualizado.getNome());

        }
        // Validações de negócio
        restaurante.setNome(restauranteAtualizado.getNome());
        restaurante.setCategoria(restauranteAtualizado.getCategoria());
        restaurante.setEndereco(restauranteAtualizado.getEndereco());
        restaurante.setTelefone(restauranteAtualizado.getTelefone());
        restaurante.setTaxaEntrega(restauranteAtualizado.getTaxaEntrega());

        return restauranteRepository.save(restaurante);
    }

    public void inativar(Long id) {
        Restaurante restaurante = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado com ID: " + id));
        restaurante.setAtivo(false);

        restauranteRepository.save(restaurante);
    }

    // Validar dados do restaurante
    private void validarDadosRestaurante(Restaurante restaurante) {
        if (restaurante.getNome() == null || restaurante.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do restaurante é obrigatório");
        }

        // if (restaurante.getCategoria() == null ||
        // restaurante.getCategoria().trim().isEmpty()) {
        // throw new IllegalArgumentException("Categoria do restaurante é obrigatória");
        // }

        if (restaurante.getTaxaEntrega() != null && restaurante.getTaxaEntrega().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Taxa de entrega deve ser maior ou igual a zero");
        }
    }

}
