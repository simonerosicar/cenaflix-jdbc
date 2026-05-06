-- ============================================================
--  Script SQL - Cenaflix Podcast
--  Banco: MySQL 8+
--  Criação das tabelas e dados iniciais
-- ============================================================

CREATE DATABASE IF NOT EXISTS cenaflix_podcast
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE cenaflix_podcast;

-- ============================================================
--  Tabela de usuários
-- ============================================================
CREATE TABLE IF NOT EXISTS usuarios (
    id           INT          NOT NULL AUTO_INCREMENT,
    usuario      VARCHAR(100) NOT NULL UNIQUE,
    senha        VARCHAR(100) NOT NULL,
    tipo_usuario VARCHAR(50)  NOT NULL COMMENT 'Administrador | Operador | Usuário',
    ativo        TINYINT(1)   NOT NULL DEFAULT 1,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
--  Tabela de podcasts
-- ============================================================
CREATE TABLE IF NOT EXISTS podcasts (
    id               INT          NOT NULL AUTO_INCREMENT,
    produtor         VARCHAR(150) NOT NULL,
    nome_episodio    VARCHAR(200) NOT NULL,
    numero_episodio  INT          NOT NULL,
    duracao          INT          NOT NULL COMMENT 'Duração em minutos',
    url_repositorio  VARCHAR(500) NOT NULL,
    data_criacao     DATETIME     DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
--  Usuários iniciais (3 tipos de permissão)
-- ============================================================
INSERT INTO usuarios (usuario, senha, tipo_usuario, ativo) VALUES
    ('admin',    'admin123',    'Administrador', 1),
    ('operador', 'oper123',     'Operador',      1),
    ('usuario',  'user123',     'Usuário',       1);

-- ============================================================
--  Podcasts de exemplo
-- ============================================================
INSERT INTO podcasts (produtor, nome_episodio, numero_episodio, duracao, url_repositorio) VALUES
    ('Spotify',  'Tecnologia e Inovação',  1, 45, 'https://spotify.com/podcast/tecnologia-1'),
    ('Spotify',  'Inteligência Artificial', 2, 60, 'https://spotify.com/podcast/tecnologia-2'),
    ('Netflix',  'Bastidores das Séries',   1, 30, 'https://netflix.com/podcast/bastidores-1'),
    ('Globo',    'Notícias do Dia',         1, 20, 'https://globo.com/podcast/noticias-1');
