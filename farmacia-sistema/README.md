# Sistema de Controle de Farmácia — Microsserviços Spring Boot

## Microsserviços
| Serviço         | Porta | Responsabilidade              |
|-----------------|-------|-------------------------------|
| ms-estoque      | 8081  | Produtos, estoque, movimentações |
| ms-vendas       | 8082  | Vendas, receitas, Strategy Pattern |
| ms-regulatorio  | 8083  | SNGPC, conformidade ANVISA    |
| ms-usuarios     | 8084  | Usuários, autenticação, logs  |
| shared-commons  | -     | Persistência JSON, exceções   |

## Como rodar no VS Code

### Pré-requisitos
- Java 17+
- Maven 3.8+
- Extensão: Extension Pack for Java (Microsoft)

### Passo a passo

1. Instale o shared-commons primeiro:
   cd shared-commons && mvn install

2. Abra cada pasta de microsserviço no VS Code como projeto separado
   ou abra a pasta raiz farmacia-sistema/

3. Rode cada microsserviço:
   cd ms-estoque && mvn spring-boot:run
   cd ms-vendas && mvn spring-boot:run
   cd ms-regulatorio && mvn spring-boot:run
   cd ms-usuarios && mvn spring-boot:run

4. Teste os endpoints com o Thunder Client (extensão VS Code) ou Postman

## Endpoints principais

### ms-estoque (:8081)
- GET  /api/produtos
- POST /api/produtos
- POST /api/estoque/entrada
- POST /api/estoque/saida
- GET  /api/estoque/saldo/{produtoId}
- GET  /api/estoque/historico
- GET  /api/estoque/alertas

### ms-vendas (:8082)
- POST /api/vendas
- GET  /api/vendas
- POST /api/receitas
- GET  /api/receitas

### ms-regulatorio (:8083)
- POST /api/sngpc/registrar
- GET  /api/sngpc

### ms-usuarios (:8084)
- POST /api/usuarios
- GET  /api/usuarios
- POST /api/usuarios/login
- GET  /api/usuarios/logs

## Persistência
Os dados são salvos em arquivos JSON na pasta dados/ de cada microsserviço.
