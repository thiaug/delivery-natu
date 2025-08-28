package com.deliverynatu.delivery_api.config;

import java.math.BigDecimal;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.deliverynatu.delivery_api.entity.Cliente;
import com.deliverynatu.delivery_api.entity.Restaurante;
import com.deliverynatu.delivery_api.repository.*;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("== INICIANDO CARGA DE TESTES ==");

        // Limpar dados existentes
        pedidoRepository.deleteAll();
        produtoRepository.deleteAll();
        clienteRepository.deleteAll();
        restauranteRepository.deleteAll();

        // Inserir dados de Teste
        inserirClientes();
        inserirRestaurantes();
        // ! inserirProdutos();
        // ! inserirPedidos();

        testarConsultas();

        System.out.println("=== CARGA DE DADOS CONCLUÍDA ===");

    }

    private void inserirClientes() {
        System.out.println("--- Inserindo Clientes ---");

        Cliente cliente1 = new Cliente();
        cliente1.setNome("João Silva");
        cliente1.setEmail("joao@gmail.com");
        cliente1.setTelefone("1194219934");
        cliente1.setEndereco("Travessa Antônio Ferreira, 23");
        cliente1.setAtivo(true);

        Cliente cliente2 = new Cliente();
        cliente2.setNome("Beatrice Johnson");
        cliente2.setEmail("beatricem@yahoo.com");
        cliente2.setTelefone("1399865234");
        cliente2.setEndereco("Rua Domingos Olímpio, 456");
        cliente2.setAtivo(true);

        Cliente cliente3 = new Cliente();
        cliente3.setNome("Lora Simon");
        cliente3.setEmail("lora@uol.com");
        cliente3.setTelefone("14998435789");
        cliente3.setEndereco("Avenida Esbertalina Barbosa Damiani, 789");
        cliente3.setAtivo(false);

        clienteRepository.saveAll(Arrays.asList(cliente1, cliente2, cliente3));
        System.out.println("--- Clientes inseridos com sucesso! ---");
    }

    private void inserirRestaurantes() {
        System.out.println("--- Inserindo Restaurantes ---");

        Restaurante restaurante1 = new Restaurante();
        restaurante1.setNome("Verdejar");
        restaurante1.setCategoria("Vegano");
        restaurante1.setEndereco("Avenida Governador José Malcher, 12");
        restaurante1.setTelefone("13988647347");
        restaurante1.setTaxaEntrega(new BigDecimal("3.50"));
        restaurante1.setAtivo(true);

        Restaurante restaurante2 = new Restaurante();
        restaurante2.setNome("Casa Broto");
        restaurante2.setCategoria("Vegetariano");
        restaurante2.setEndereco("Avenida Governador José Malcher, 12");
        restaurante2.setTelefone("12988647347");
        restaurante2.setTaxaEntrega(new BigDecimal("2.50"));
        restaurante2.setAtivo(true);

        Restaurante restaurante3 = new Restaurante();
        restaurante3.setNome("Semente Pura");
        restaurante3.setCategoria("Natural");
        restaurante3.setEndereco("Travessa Iaçá, 102");
        restaurante3.setTelefone("13987659805");
        restaurante3.setTaxaEntrega(new BigDecimal("5.00"));
        restaurante3.setAtivo(true);

        restauranteRepository.saveAll(Arrays.asList(restaurante1, restaurante2, restaurante3));
        System.out.println("--- Restaurantes inseridos com sucesso! ---");
    }

    private void testarConsultas() {
        System.out.println("== TESTANDO CONSULTAS ==");

        // Teste Cliente Repository
        System.out.println("\n -- Teste Cliente Repository --");

        var clientePorEmail = clienteRepository.findByEmail("beatricem@yahoo.com");
        System.out.println("Cliente encontrado por email: "
                + (clientePorEmail.isPresent() ? clientePorEmail.get().getNome() : "Não encontrado"));

        var clientesAtivos = clienteRepository.findByAtivoTrue();
        System.out.println("Clientes ativos: " + clientesAtivos.size());

        var clientesPorNome = clienteRepository.findByNomeContainingIgnoreCase("silva");
        System.out.println("Clientes com 'silva' no nome: " + clientesPorNome.size());

        boolean emailExiste = clienteRepository.existsByEmail("beatricem@yahoo.com");
        System.out.println("Email existe: " + emailExiste);

        // Teste Restaurante Repository
        System.out.println("\n -- Teste Restaurante Repository --");
        var restaurantePorNome = restauranteRepository.findByNome("Verdejar");
        System.out.println("Restaurante encontrado por nome: "
                + (restaurantePorNome.isPresent() ? restaurantePorNome.get().getNome() : "Não encontrado"));

        var restaurantesAtivos = restauranteRepository.findByAtivoTrue();
        System.out.println("Restaurantes ativos: " + restaurantesAtivos.size());

        var restaurantesPorCategoria = restauranteRepository.findByCategoria("Vegetariano");
        System.out.println("Restaurantes com categoria 'Vegetariano': " + restaurantesPorCategoria.size());

        boolean restauranteExiste = restauranteRepository.existsByNome("Semente Pura");
        System.out.println("Restaurante existe: " + restauranteExiste);

    }

}
