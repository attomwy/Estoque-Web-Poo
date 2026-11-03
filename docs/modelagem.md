# Modelagem de dados

## Entidades

- **Categoria**: identificador, nome e descrição.
- **Fornecedor**: identificador, nome e CNPJ.
- **Produto**: identificador, nome, preço, quantidade, categoria e fornecedor.
- **Usuario**: identificador, nome e e-mail.
- **Movimentacao**: identificador, tipo (ENTRADA/SAIDA), quantidade, data, produto e usuário.

## Diagrama de classes (simplificado)

```mermaid
classDiagram
    class Categoria {
        +Long id
        +String nome
        +String descricao
    }
    class Fornecedor {
        +Long id
        +String nome
        +String cnpj
    }
    class Produto {
        +Long id
        +String nome
        +BigDecimal preco
        +Integer quantidade
    }
    class Usuario {
        +Long id
        +String nome
        +String email
    }
    class Movimentacao {
        +Long id
        +TipoMovimentacao tipo
        +Integer quantidade
        +LocalDateTime data
    }
    Categoria "1" --> "0..*" Produto
    Fornecedor "1" --> "0..*" Produto
    Produto "1" --> "0..*" Movimentacao
    Usuario "1" --> "0..*" Movimentacao
```

## Modelo relacional (MER lógico)

```mermaid
erDiagram
    CATEGORIA ||--o{ PRODUTO : possui
    FORNECEDOR ||--o{ PRODUTO : fornece
    PRODUTO ||--o{ MOVIMENTACAO : registra
    USUARIO ||--o{ MOVIMENTACAO : realiza
    CATEGORIA {
        bigint id PK
        varchar nome
        varchar descricao
    }
    FORNECEDOR {
        bigint id PK
        varchar nome
        varchar cnpj
    }
    PRODUTO {
        bigint id PK
        varchar nome
        decimal preco
        integer quantidade
        bigint categoria_id FK
        bigint fornecedor_id FK
    }
    USUARIO {
        bigint id PK
        varchar nome
        varchar email
    }
    MOVIMENTACAO {
        bigint id PK
        varchar tipo
        integer quantidade
        timestamp data
        bigint produto_id FK
        bigint usuario_id FK
    }
```

As tabelas são geradas automaticamente pelo Hibernate com `spring.jpa.hibernate.ddl-auto=update`. Os nomes de colunas podem variar conforme o dialeto e a estratégia de nomenclatura do JPA.
