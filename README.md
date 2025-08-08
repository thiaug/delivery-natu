# API de Delivery Natú

Sistema de Delivery desenvolvido com Spring Boot e Java 21

## 🚀 Sobre o Projeto

Este projeto é uma API para um sistema de delivery, permitindo o gerenciamento de restaurantes, produtos, clientes e pedidos. A aplicação é construída utilizando as tecnologias mais recentes do ecossistema Java e Spring.

## 🛠️ Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3**
- **Spring Data JPA / Hibernate**
- **Maven**
- **H2 Database** (Banco de dados em memória)

## ✅ Pré-requisitos

Antes de começar, você vai precisar ter instalado em sua máquina as seguintes ferramentas:

- [JDK 21](https://www.oracle.com/java/technologies/downloads/#java21) ou superior.
- [Maven](https://maven.apache.org/download.cgi) ou um IDE com suporte ao Maven (IntelliJ, VSCode, Eclipse).
- [Git](https://git-scm.com) (ou outra ferramenta de controle de versão).

## ⚙️ Como Executar o Projeto

Siga os passos abaixo para executar a aplicação localmente:

1.  **Clone o repositório:**

    ```bash
    git clone <URL_DO_SEU_REPOSITORIO>
    cd delivery-natu
    ```

2.  **Execute a aplicação com o Maven:**

    ```bash
    ./mvnw spring-boot:run
    ```

    _Se você não estiver usando o Maven Wrapper (`mvnw`), use o comando `mvn spring-boot:run`._

3.  **Acesse a aplicação:**
    A API estará disponível em `http://localhost:8080`.

## 🗄️ Banco de Dados H2

O projeto utiliza um banco de dados H2 em memória para facilitar o desenvolvimento e os testes. Os dados são recriados a cada reinicialização da aplicação (`spring.jpa.hibernate.ddl-auto=create-drop`).

- **URL do JDBC:** `jdbc:h2:mem:deliverydb`
- **Usuário:** `sa`
- **Senha:** (em branco)

Você pode acessar o console do H2 para visualizar e gerenciar os dados através do seguinte endereço no seu navegador:
http://localhost:8080/h2-console

**Importante:** Ao se conectar, certifique-se de que a URL do JDBC no console do H2 corresponde exatamente à configurada no projeto (`jdbc:h2:mem:deliverydb`).

## 📝 Endpoints da API (Exemplos)

A API ainda está em desenvolvimento, mas aqui estão alguns exemplos dos endpoints que podem ser criados:

#### Restaurantes

- `GET /restaurantes`: Lista todos os restaurantes.
- `GET /restaurantes/{id}`: Busca um restaurante por ID.
- `POST /restaurantes`: Cria um novo restaurante.

#### Produtos

- `GET /restaurantes/{restauranteId}/produtos`: Lista os produtos de um restaurante.
- `POST /restaurantes/{restauranteId}/produtos`: Adiciona um novo produto a um restaurante.

#### Pedidos

- `POST /pedidos`: Cria um novo pedido.
- `GET /pedidos/{id}`: Busca um pedido por ID.
