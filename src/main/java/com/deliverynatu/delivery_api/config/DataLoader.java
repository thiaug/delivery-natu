package com.deliverynatu.delivery_api.config;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.deliverynatu.delivery_api.entity.Cliente;
import com.deliverynatu.delivery_api.entity.ItemPedido;
import com.deliverynatu.delivery_api.entity.Pedido;
import com.deliverynatu.delivery_api.entity.Produto;
import com.deliverynatu.delivery_api.entity.Restaurante;
import com.deliverynatu.delivery_api.enums.StatusPedido;
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
        inserirProdutos();
        inserirPedidos();

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

    private void inserirProdutos() {
        System.out.println("--- Inserindo Produtos ---");

        Produto produto1 = new Produto();
        produto1.setNome("Leite vegetal");
        produto1.setDescricao("Bebida nutritiva sem lactose, feita a partir de grãos ou oleaginosas");
        produto1.setPreco(new BigDecimal("10.00"));
        produto1.setCategoria("Vegano");
        produto1.setDisponivel(true);

        Produto produto2 = new Produto();
        produto2.setNome("Iogurte natural");
        produto2.setDescricao("Cremoso e nutritivo, com probióticos benéficos para a saúde intestinal.");
        produto2.setPreco(new BigDecimal("3.00"));
        produto2.setCategoria("Vegetariano");
        produto2.setDisponivel(false);

        Produto produto3 = new Produto();
        produto3.setNome("Farinhas Naturais");
        produto3.setDescricao("Alternativas nutritivas para bolos, pães e panquecas.");
        produto3.setPreco(new BigDecimal("3.00"));
        produto3.setCategoria("Natural");
        produto3.setDisponivel(true);

        Produto produto4 = new Produto();
        produto4.setNome("Chás naturais");
        produto4.setDescricao("Infusões aromáticas com propriedades calmantes e digestivas.");
        produto4.setPreco(new BigDecimal("5.00"));
        produto4.setCategoria("Natural");
        produto4.setDisponivel(false);

        Produto produto5 = new Produto();
        produto5.setNome("Molho Pesto");
        produto5.setDescricao("Cremoso, feito com manjericão fresco, azeite e parmesão.");
        produto5.setPreco(new BigDecimal("25.00"));
        produto5.setCategoria("Vegetariano");
        produto5.setDisponivel(true);

        produtoRepository.saveAll(Arrays.asList(produto1, produto2, produto3, produto4, produto5));
        System.out.println("--- Produtos inseridos com sucesso! ---");

    }

    public void inserirPedidos() {
        System.out.println("--- Inserindo Pedidos ---");

        // Buscar dados já inseridos
        Cliente cliente1 = clienteRepository.findByEmail("joao@gmail.com").orElseThrow();
        Cliente cliente2 = clienteRepository.findByEmail("beatricem@yahoo.com").orElseThrow();

        Restaurante restaurante1 = restauranteRepository.findByNome("Verdejar").orElseThrow();
        Restaurante restaurante2 = restauranteRepository.findByNome("Casa Broto").orElseThrow();

        Produto produto1 = produtoRepository.findByNome("Leite vegetal").orElseThrow();
        Produto produto2 = produtoRepository.findByNome("Iogurte natural").orElseThrow();
        Produto produto3 = produtoRepository.findByNome("Molho Pesto").orElseThrow();

        // === Pedido 1 ===
        Pedido pedido1 = new Pedido();
        pedido1.setCliente(cliente1);
        pedido1.setRestaurante(restaurante1);
        pedido1.setStatus(StatusPedido.CONFIRMADO);
        pedido1.setDataPedido(LocalDateTime.now());

        ItemPedido item1 = new ItemPedido();
        item1.setPedido(pedido1);
        item1.setProduto(produto1);
        item1.setQuantidade(2);
        item1.setPrecoUnitario(produto1.getPreco());
        item1.setSubtotal(produto1.getPreco().multiply(new BigDecimal(item1.getQuantidade())));

        ItemPedido item2 = new ItemPedido();
        item2.setPedido(pedido1);
        item2.setProduto(produto3);
        item2.setQuantidade(1);
        item2.setPrecoUnitario(produto3.getPreco());
        item2.setSubtotal(produto3.getPreco());

        pedido1.setItens(Arrays.asList(item1, item2));
        pedido1.setValorTotal(item1.getSubtotal().add(item2.getSubtotal()));

        // === Pedido 2 ===
        Pedido pedido2 = new Pedido();
        pedido2.setCliente(cliente2);
        pedido2.setRestaurante(restaurante2);
        pedido2.setStatus(StatusPedido.PREPARANDO);
        pedido2.setDataPedido(LocalDateTime.now());

        ItemPedido item3 = new ItemPedido();
        item3.setPedido(pedido2);
        item3.setProduto(produto2);
        item3.setQuantidade(3);
        item3.setPrecoUnitario(produto2.getPreco());
        item3.setSubtotal(produto2.getPreco().multiply(new BigDecimal(item3.getQuantidade())));

        pedido2.setItens(Arrays.asList(item3));
        pedido2.setValorTotal(item3.getSubtotal());

        // Salvar
        pedidoRepository.saveAll(Arrays.asList(pedido1, pedido2));

        System.out.println("--- Pedidos inseridos com sucesso! ---");
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

        // Teste Produto Repository
        System.out.println("\n--- Teste Produto Repository ---");
        var produtosDisponiveis = produtoRepository.findByDisponivelTrue();
        System.out.println("Produtos disponíveis: " + produtosDisponiveis.size());
        var restaurante1 = restauranteRepository.findByNome("Verdejar").orElseThrow();
        var produtosPorRestaurante = produtoRepository.findByRestauranteIdAndDisponivelTrue(restaurante1.getId());
        System.out.println("Produtos disponíveis no restaurante '" + restaurante1.getNome() + "': "
                + produtosPorRestaurante.size());
        var produtosPorCategoria = produtoRepository.findByCategoriaAndDisponivelTrue("Vegetariano");
        System.out.println("Produtos 'Vegetariano' disponíveis: " + produtosPorCategoria.size());
        var produtosPorPreco = produtoRepository.findByPrecoBetweenAndDisponivelTrue(new BigDecimal("3.00"),
                new BigDecimal("15.00"));
        System.out.println("Produtos com preço entre 3.00 e 15.00: " + produtosPorPreco.size());
        var produtosMaisVendidos = produtoRepository.findProdutosMaisVendidos();
        System.out.println("Produtos mais vendidos: "
                + produtosMaisVendidos.stream().map(Produto::getNome).collect(Collectors.joining(", ")));
        var produtosRestauranteECategoria = produtoRepository.findByRestauranteAndCategoria(restaurante1, "Vegano");
        System.out.println(
                "Produtos do restaurante 'Verdejar' e categoria 'Vegano': " + produtosRestauranteECategoria.size());
        // **Correção:** O método countByRestauranteId() não estava recebendo o
        // parâmetro. A chamada abaixo está correta.
        var countProdutos = produtoRepository.countByRestauranteId(restaurante1.getId());
        System.out.println("Contagem de produtos do restaurante '" + restaurante1.getNome() + "': " + countProdutos);

    }

}
