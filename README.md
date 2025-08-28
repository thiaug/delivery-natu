# API de Delivery Natú

Sistema de Delivery desenvolvido com Spring Boot e Java 21.

## 🚀 Sobre o Projeto

Este projeto é uma API RESTful para um sistema de delivery, permitindo o gerenciamento completo de restaurantes, produtos, clientes e pedidos. A aplicação é construída utilizando as tecnologias mais recentes do ecossistema Java e Spring, com foco em boas práticas de desenvolvimento e arquitetura limpa.

## 🛠️ Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3**
- **Spring Data JPA / Hibernate**
- **Maven**
- **H2 Database** (Banco de dados em memória)
- **ModelMapper** (Mapeamento de objetos)

## 🏗️ Estrutura do Projeto

O projeto é organizado nos seguintes pacotes principais:

- `config`: Classes de configuração, como `ModelMapperConfig` e `DataLoader` para popular o banco com dados iniciais.
- `controller`: Contém os controladores REST que expõem os endpoints da API.
- `dto`: Data Transfer Objects, usados para transferir dados entre o cliente e o servidor.
- `entity`: As classes de entidade JPA que mapeiam as tabelas do banco de dados.
- `enums`: Enumerações utilizadas no projeto, como `StatusPedido`.
- `exception`: Classes para tratamento de exceções globais e customizadas.
- `repository`: Interfaces do Spring Data JPA para acesso aos dados.
- `service`: Onde reside a lógica de negócio da aplicação.

## ✅ Pré-requisitos

- [JDK 21](https://www.oracle.com/java/technologies/downloads/#java21) ou superior.
- [Maven](https://maven.apache.org/download.cgi) ou um IDE com suporte.
- [Git](https://git-scm.com).

## ⚙️ Como Executar o Projeto

1.  **Clone o repositório:**
    ```bash
    git clone <URL_DO_SEU_REPOSITORIO>
    cd delivery-natu
    ```

2.  **Execute a aplicação com o Maven:**
    ```bash
    ./mvnw spring-boot:run
    ```

3.  **Acesse a aplicação:**
    A API estará disponível em `http://localhost:8080`.

## 🗄️ Banco de Dados (H2)

O projeto utiliza um banco de dados H2 em memória. Os dados são recriados a cada reinicialização.

- **URL do JDBC:** `jdbc:h2:mem:deliverydb`
- **Usuário:** `sa`
- **Senha:** (em branco)

Acesse o console do H2 para gerenciar os dados em:
[http://localhost:8080/h2-console](http://localhost:8080/h2-console)

**Importante:** Certifique-se de que a URL do JDBC no console do H2 seja `jdbc:h2:mem:deliverydb`.

## 📝 Endpoints da API

A seguir estão os endpoints disponíveis na aplicação.

### Health Check

- `GET /health`: Verifica o status da aplicação.
- `GET /info`: Exibe informações sobre a aplicação.

### Clientes (`/clientes`)

- `POST /`: Cadastra um novo cliente.
- `GET /`: Lista todos os clientes ativos.
- `GET /{id}`: Busca um cliente por ID.
- `GET /email/{email}`: Busca um cliente por email.
- `GET /buscar?nome={nome}`: Busca clientes por nome.
- `PUT /{id}`: Atualiza os dados de um cliente.
- `DELETE /{id}`: Inativa um cliente (soft delete).

### Restaurantes (`/restaurantes`)

- `POST /`: Cadastra um novo restaurante.
- `GET /`: Lista todos os restaurantes ativos.
- `GET /{id}`: Busca um restaurante por ID.
- `GET /categoria/{categoria}`: Busca restaurantes por categoria.
- `PUT /{id}`: Atualiza os dados de um restaurante.
- `DELETE /{id}`: Inativa um restaurante (soft delete).

### Produtos (`/produtos`)

- `POST /restaurante/{restauranteId}`: Cadastra um novo produto para um restaurante.
- `GET /`: Lista todos os produtos disponíveis.
- `GET /{id}`: Busca um produto por ID.
- `GET /restaurante/{restauranteId}`: Lista os produtos de um restaurante específico.
- `GET /categoria/{categoria}`: Busca produtos por categoria.
- `GET /preco?precoMin={min}&precoMax={max}`: Busca produtos por faixa de preço.
- `PUT /{id}`: Atualiza os dados de um produto.
- `PATCH /{id}/disponibilidade?disponivel={boolean}`: Altera a disponibilidade de um produto.

### Pedidos (`/pedidos`)

- `POST /?clienteId={id}&restauranteId={id}`: Cria um novo pedido.
- `POST /{pedidoId}/itens?produtoId={id}&quantidade={qtd}`: Adiciona um item a um pedido existente.
- `GET /cliente/{clienteId}`: Lista todos os pedidos de um cliente.
- `GET /numero/{numeroPedido}`: Busca um pedido pelo seu número.
- `PUT /{pedidoId}/confirmar`: Confirma um pedido (muda o status para `CONFIRMADO`).
- `PUT /{pedidoId}/status?status={status}`: Atualiza o status de um pedido (ex: `EM_PREPARO`, `A_CAMINHO`).
- `PUT /{pedidoId}/cancelar`: Cancela um pedido.

### Relatório de Vendas

Foi implementada uma consulta para gerar relatórios de vendas diárias. Embora o endpoint ainda não tenha sido exposto na controller, a funcionalidade pode ser acessada através do `PedidoRepository` com o método `findVendasDiarias`.

```java
// Exemplo da consulta no PedidoRepository
@Query("SELECT new com.deliverynatu.delivery_api.repository.RelatorioVendas( ... )")
List<RelatorioVendas> findVendasDiarias(LocalDate data, StatusPedido status);
```