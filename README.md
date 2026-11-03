# EstoqueWeb

Aplicação de controle de estoque em Java, Spring Boot, Spring MVC, Thymeleaf e Spring Data JPA.

**Banco de dados padrão desta distribuição:** MySQL.

## Funcionalidades

- Cadastro, consulta, edição e exclusão de produtos, categorias, fornecedores e usuários.
- Movimentações de entrada e saída com atualização da quantidade disponível.
- Edição e exclusão de movimentações com recálculo do estoque.
- Dashboard com contagens, últimas movimentações e produtos com estoque baixo.
- Pesquisa local nas tabelas e interface responsiva.

## Executar no Windows

1. Extraia o ZIP completamente.
2. Execute `INICIAR_ESTOQUEWEB.cmd`.
3. Aguarde a mensagem `Started EstoqueWebApplication`.
4. Abra `http://localhost:8080` (o inicializador também tentará abrir o navegador). O PowerShell permanecer aberto enquanto o sistema estiver rodando.
5. Para interromper, pressione `Ctrl + C` na janela do PowerShell.

O inicializador detecta **JDK 17+** e **Maven**. Se não encontrar, tenta baixar cópias portáteis em `.ferramentas/`, sem instalar no Windows. A primeira execução requer acesso à internet para obter as dependências Java. Políticas de execução/rede podem impedir esse processo.

## Banco de dados

Requer um **MySQL Server** em execução em `127.0.0.1:3306`. O inicializador verifica a porta e solicita usuário e senha em tempo de execução; não armazena credenciais no código.

O endereço padrão é `jdbc:mysql://127.0.0.1:3306/estoqueweb`. O banco é criado automaticamente se a conta possuir a permissão correspondente. Alternativamente, execute `CREATE DATABASE IF NOT EXISTS estoqueweb;` com uma conta autorizada.

**O inicializador não instala o MySQL Server**, pois essa configuração pode exigir permissões administrativas.

O conteúdo existente em H2 **não é transferido automaticamente** para MySQL, nem vice-versa. São bancos separados.

## Executar pelo terminal

Na pasta onde está `pom.xml`, defina o JDK 17 e o Maven no PATH e execute:

```powershell
$env:DB_USER = 'root'  # ajuste se usar outra conta
$env:DB_PASSWORD = Read-Host 'Senha do MySQL'
mvn spring-boot:run
```

A senha definida no PowerShell vale apenas para o terminal atual. Nunca publique senhas em arquivos versionados.

## Organização

- `src/main/java`: entidades, repositórios, serviços e controladores.
- `src/main/resources/templates`: telas Thymeleaf.
- `src/main/resources/static`: CSS e JavaScript.
- `src/main/resources/application*.properties`: configuração por perfil (`h2` / `mysql`).
- `scripts/`: verificação de requisitos e execução.
- `docs/modelagem.md`: diagrama de classes e modelo relacional.
- `pom.xml`: dependências Java.
- `requirements.txt`: resumo dos pré-requisitos; não utilizar `pip`.

## Publicação e limitações

- Publique os **arquivos extraídos**, não apenas um ZIP. Não inclua `target/`, `.ferramentas/`, `dados/*.mv.db` ou credenciais.
- O sistema aceita conexões somente em `127.0.0.1:8080`; o link não é público.
- A aplicação não possui autenticação, autorização nem sincronização de cadastros entre computadores.
- Cadastros referenciados por outras tabelas podem ter exclusão impedida por chaves estrangeiras.
- O armazenamento e a execução dependem de permissões de escrita e das políticas de segurança do dispositivo.

## Verificação rápida

1. Abra o sistema, cadastre uma categoria, um fornecedor e um usuário.
2. Cadastre um produto com quantidade inicial 10.
3. Registre uma saída de 2 unidades e verifique que o estoque ficou em 8.
4. Registre uma entrada de 5 unidades e verifique que o estoque ficou em 13.
5. Encerre e reabra a aplicação; confirme que os registros permanecem.

**Nota:** este projeto precisa ser executado no ambiente de destino para confirmar a persistência e o funcionamento dos cadastros.

A aplicação web escuta somente no endereço local `127.0.0.1`. Se o Windows solicitar permissão de rede ao Java, não é necessário autorizar acesso externo para utilizar `localhost`; a conexão com o servidor MySQL já existente continua sendo necessária.

## Integrantes

- Vinícius da Motta Carvalho Faria
- Luís Miguel de Aguida Madureira dos Santos
- Erik Uller Souza
- João Pedro
