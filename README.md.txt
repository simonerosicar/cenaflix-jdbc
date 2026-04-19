# 🎬 Cenaflix — Gerenciador de Vídeos

> **Instituição:** SENAC
> **Curso:** Técnico em Desenvolvimento de Sistemas
> **Disciplina:** Programação para Desktop
> **Atividade:** Sprint 1 e Sprint 2 — CRUD completo com JDBC

---

## 📋 Sobre o projeto

O **Cenaflix** simula o backend de uma plataforma de streaming.
Implementa um CRUD completo de vídeos com interface gráfica
Java Swing e banco de dados MySQL via JDBC.

---

## ✨ Funcionalidades

- ✅ Cadastro com validação de campos e formatação de data
- ✅ Alerta quando ID já existe no banco
- ✅ Listagem em JTable com atualização dinâmica
- ✅ Filtro dinâmico por categoria
- ✅ Edição clicando direto na tabela
- ✅ Exclusão com confirmação de segurança
- ✅ Mensagens de erro descritivas
- ✅ Documentação Javadoc completa

---

## 🖥️ Screenshots

### Tela de Gerenciamento com filtro
![JTable](prints/print_tela_busca.png)

### Banco de dados MySQL
![Workbench](prints/print_workbench.png)

---

## 🗄️ Banco de dados

CREATE DATABASE ATIVIDADE1;
USE ATIVIDADE1;

CREATE TABLE videos (
  id              INT          PRIMARY KEY,
  nome            VARCHAR(100) NOT NULL,
  data_lancamento DATE         NOT NULL,
  categoria       VARCHAR(50)  NOT NULL
);

---

## 🚀 Como executar

### Pré-requisitos
- Java JDK 17+
- MySQL 8+
- NetBeans 17+
- MySQL Connector/J 9.6+

### Passo a passo

1. Clone o repositório
git clone https://github.com/SEU_USUARIO/cenaflix-jdbc.git

2. Crie o banco de dados no MySQL Workbench
CREATE DATABASE ATIVIDADE1;
USE ATIVIDADE1;
CREATE TABLE videos (
  id INT PRIMARY KEY,
  nome VARCHAR(100) NOT NULL,
  data_lancamento DATE NOT NULL,
  categoria VARCHAR(50) NOT NULL
);

3. Configure a conexão em src/conexao/Conexao.java
Troque "sua_senha_aqui" pela sua senha do MySQL

4. Adicione o driver MySQL no NetBeans
Botão direito em Libraries → Add JAR → mysql-connector-j-9.6.0.jar

5. Execute o projeto
F6 ou Run → Run Project

---

## 🏗️ Arquitetura

src/
├── conexao/Conexao.java         → Conexão JDBC com MySQL
├── model/Video.java             → Entidade de dados
├── dao/VideoDAO.java            → CRUD completo
└── TelaCadastro/
    ├── TelaCadastro.java        → Sprint 1 — Cadastro
    └── TelaGerenciar.java       → Sprint 2 — Gerenciamento

Padrão utilizado: DAO (Data Access Object)

---

## 🎓 Contexto acadêmico

Instituição  : SENAC
Curso        : Técnico em Desenvolvimento de Sistemas
Disciplina   : Programação para Desktop
Atividade    : Sprint 1 e Sprint 2
Tecnologias  : Java · Swing · JDBC · MySQL · Javadoc

---

## 👨‍💻 Autor

Simone Cardozo
Estudante de ADS — SENAC
GitHub: https://github.com/simonerosicar
LinkedIn: https://linkedin.com/in/simone-cardozo-23a273362/