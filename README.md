# 🎬 Cenaflix — Gerenciador de Vídeos

Sistema desktop de cadastro e gerenciamento de vídeos desenvolvido em **Java** com interface gráfica **Swing** e persistência em banco de dados **MySQL** via **JDBC**.

> Instituição: SENAC | Curso: Técnico em Desenvolvimento de Sistemas
> Disciplina: Programação para Desktop | Sprint 1 e Sprint 2 — CRUD completo com JDBC

---

## 📋 Funcionalidades

### Sprint 1 — Cadastro (Atividade 1)
- Cadastro de vídeos: ID, nome, data de lançamento e categoria
- Validação de campos obrigatórios
- Máscara automática de data (DD/MM/AAAA)
- Capitalização automática do nome
- Navegação entre campos via tecla Enter
- Feedback visual de sucesso e erro

### Sprint 2 — Gerenciamento (Atividade 2)
- Listagem de todos os vídeos em JTable
- Filtro dinâmico por categoria (em tempo real)
- Edição de registro selecionado na tabela
- Atualização de dados com validação
- Exclusão com confirmação
- Navegação entre telas (Cadastro ↔ Gerenciar)
- Documentação Javadoc em todas as classes

---

## 🛠️ Tecnologias

| Tecnologia | Versão |
|------------|--------|
| Java       | 17+    |
| Swing      | —      |
| MySQL      | 8+     |
| JDBC       | —      |
| NetBeans   | 17+    |
| MySQL Connector/J | 9.6+ |

---

## ⚙️ Configuração

### 1. Banco de dados

Execute no MySQL Workbench:

```sql
CREATE DATABASE ATIVIDADE1;

USE ATIVIDADE1;

CREATE TABLE videos (
    id               INT          PRIMARY KEY,
    nome             VARCHAR(100) NOT NULL,
    data_lancamento  DATE         NOT NULL,
    categoria        VARCHAR(50)  NOT NULL
);
```

### 2. Credenciais

Copie o arquivo de exemplo e preencha com sua senha:

```bash
cp src/db.properties.example src/db.properties
```

Edite `src/db.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/ATIVIDADE1
db.user=root
db.pass=SUA_SENHA
```

> ⚠️ O arquivo `db.properties` está no `.gitignore` e **nunca** será versionado.

### 3. Driver JDBC

No NetBeans: clique com botão direito em **Libraries → Add JAR/Folder** e selecione o `mysql-connector-j-9.6.0.jar`.

Download: [https://dev.mysql.com/downloads/connector/j/](https://dev.mysql.com/downloads/connector/j/)

---

## ▶️ Como executar

1. Clone o repositório:
```bash
git clone https://github.com/simonerosicar/cenaflix-jdbc.git
```
2. Abra o projeto no NetBeans
3. Configure o `src/db.properties`
4. Adicione o driver JDBC nas Libraries
5. Execute com **F6** (classe principal: `TelaCadastro`)

Para testar a conexão, execute `TesteConexao`.

---

## 📁 Estrutura do projeto

```
src/
├── conexao/
│   └── Conexao.java              # Gerenciamento de conexão JDBC
├── model/
│   └── Video.java                # Entidade Video
├── dao/
│   └── VideoDAO.java             # CRUD completo (insert/select/update/delete)
├── TelaCadastro/
│   └── TelaCadastro.java         # Sprint 1 — Tela de cadastro
├── view/
│   └── TelaGerenciar.java        # Sprint 2 — Tela de gerenciamento com JTable
├── db.properties.example         # Modelo de configuração (sem senha)
└── TesteConexao.java             # Utilitário de teste de conexão
```

Padrão: **DAO (Data Access Object)**

---

## 👩‍💻 Autora

**Simone Cardozo**
Estudante — Técnico em Desenvolvimento de Sistemas — SENAC
- GitHub: [https://github.com/simonerosicar](https://github.com/simonerosicar)
- LinkedIn: [https://linkedin.com/in/simone-cardozo-23a273362/](https://linkedin.com/in/simone-cardozo-23a273362/)
