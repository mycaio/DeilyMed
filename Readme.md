
-----

# DeilyMed - Sistema de Gerenciamento de Clínicas

DeilyMed é um sistema de gerenciamento para clínicas e hospitais, desenvolvido em Java com uma interface de console interativa. O projeto permite realizar cadastros, agendamentos e o gerenciamento básico do fluxo de atendimento, utilizando um banco de dados local SQLite para persistência de dados.

## ✨ Funcionalidades

* **Gerenciamento Estrutural:**
    * Cadastro de Pacientes, Médicos e Especialidades.
    * Listagem de todos os dados cadastrados.
* **Agendamento de Consultas:**
    * Criação de novas consultas, associando um paciente a um médico em uma data e hora específicas.
    * Validação para evitar conflitos de horário (implementada na camada de lógica).
* **Fluxo de Atendimento (Simulado):**
    * Visualização das consultas agendadas para o dia.
    * Alteração do status de uma consulta para "REALIZADA" ou "CANCELADA".

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java (JDK 11 ou superior)
* **Banco de Dados:** SQLite (em arquivo)
* **Driver de Conexão:** [SQLite JDBC](https://github.com/xerial/sqlite-jdbc)
* **Gerenciador de Dependências (Recomendado):** Apache Maven ou Gradle

## 🚀 Como Executar o Projeto

Siga os passos abaixo para configurar e executar a aplicação em seu ambiente de desenvolvimento.

### Pré-requisitos

* **Java Development Kit (JDK):** Versão 11 ou mais recente.
* **Maven ou Gradle:** Para gerenciamento automático das dependências (como o driver do SQLite).
* **Uma IDE Java:** Como IntelliJ IDEA, Eclipse ou VS Code com o Java Extension Pack.

### 1\. Clonar o Repositório

Primeiro, clone este repositório para a sua máquina local:

```bash
git clone <https://github.com/mycaio/DeilyMed.git>
```

### 2\. Configurar o Projeto

Abra o projeto na sua IDE de preferência. Se você estiver usando Maven ou Gradle, a IDE irá detectar o arquivo (`pom.xml` ou `build.gradle`) e baixar automaticamente todas as dependências necessárias, incluindo o driver do SQLite.

### 3\. Executar a Aplicação

1.  Navegue até o arquivo `Main.java` dentro da estrutura de pacotes.
2.  Clique com o botão direito sobre o arquivo e selecione a opção **"Run 'Main.main()'"**.
3.  A aplicação será iniciada no console da sua IDE.

Na primeira execução, o sistema irá automaticamente:

* Verificar se o arquivo de banco de dados (`deilymed.db`) existe.
* Como não existe, ele criará o arquivo na pasta raiz do projeto.
* Em seguida, executará o script `schema.sql` (localizado em `src/main/resources/sql`) para criar todas as tabelas.

Nas execuções seguintes, o sistema detectará que o banco de dados já existe e pulará a etapa de criação das tabelas.

## 🏛️ Arquitetura e Estrutura de Classes

O projeto foi estruturado seguindo padrões de design que promovem a organização, a separação de responsabilidades e a manutenibilidade do código.

### 1\. Camada de Modelo (Pacote `model`)

Esta camada contém as classes que representam as entidades do mundo real do nosso sistema. São classes "POJO" (Plain Old Java Object), responsáveis por carregar os dados.

* `Pessoa.java`: Uma classe **abstrata** que define atributos comuns (id, nome, cpf), evitando duplicação de código nas classes `Paciente` e `Medico`.
* `Paciente.java` e `Medico.java`: Herdam de `Pessoa` e adicionam seus atributos específicos.
* `Especialidade.java` e `Consulta.java`: Representam as demais entidades centrais do sistema.

### 2\. Camada de Acesso a Dados (Pacote `database`)

Esta é a camada mais importante para a persistência. Utilizamos o padrão **DAO (Data Access Object)** para isolar completamente a lógica de acesso ao banco de dados do resto da aplicação.

* **O Padrão DAO:** Cada classe DAO (ex: `PacienteDAO`, `MedicoDAO`) é responsável exclusivamente pelas operações de CRUD (Create, Read, Update, Delete) de sua respectiva tabela no banco de dados. Isso significa que se trocarmos o SQLite por outro banco no futuro, apenas as classes DAO precisarão ser modificadas.
* **Classes Utilizadas:**
    * `PacienteDAO`: Gerencia a tabela `pacientes`.
    * `MedicoDAO`: Gerencia a tabela `medicos` e suas relações com `especialidades`.
    * `EspecialidadeDAO`: Gerencia a tabela `especialidades`.
    * `ConsultaDAO`: Orquestra as operações na tabela `consultas`, utilizando os outros DAOs para construir os objetos completos.

### 3\. Camada de Visualização/Interface (Classe `Main.java`)

Para este projeto, a classe `Main.java` atua como a nossa "View" (interface com o usuário).

* **Responsabilidades:**
    * Exibir os menus de forma interativa no console.
    * Utilizar um objeto `Scanner` para capturar as entradas do usuário.
    * Orquestrar o fluxo da aplicação, chamando os métodos apropriados dos DAOs com base na escolha do usuário.

## 🗃️ Implementação com SQLite

A escolha do banco de dados e a forma como ele foi integrado são pontos-chave do projeto.

### Por que SQLite?

SQLite foi escolhido por ser um banco de dados **serverless** (não precisa de um servidor rodando), **baseado em arquivo** e de **configuração zero**. Isso o torna perfeito para aplicações desktop, projetos de aprendizado e cenários onde a simplicidade e a portabilidade são mais importantes que a alta concorrência de acessos.

### Driver JDBC e Conexão

* A comunicação entre Java e SQLite é feita através do driver **SQLite JDBC**. A dependência é gerenciada pelo Maven/Gradle, o que simplifica a configuração.
* A classe `DatabaseConnection.java` centraliza toda a lógica de conexão. Ela fornece um método estático `getConnection()` que é usado por todos os DAOs, garantindo um ponto único de gerenciamento da conexão.

### Schema e Inicialização Automática

* A estrutura completa do banco de dados (tabelas, colunas, chaves) está definida no arquivo `src/main/resources/sql/schema.sql`. Manter o schema em um arquivo separado facilita a visualização, o versionamento e a manutenção da estrutura do banco.
* O método `DatabaseConnection.initializeDatabase()` contém uma lógica inteligente: ele verifica se o arquivo `deilymed.db` existe. Se não existir, ele lê o `schema.sql` e executa os comandos para criar todas as tabelas. Isso automatiza completamente a configuração inicial para um novo usuário.