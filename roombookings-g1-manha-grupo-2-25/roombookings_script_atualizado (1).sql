
-- Criação do banco de dados
CREATE DATABASE roombookings;
\c roombookings;

-- Tabela de tipos de sala
CREATE TABLE tiposala (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(20) UNIQUE NOT NULL,
    capacidade INTEGER NOT NULL,
    CHECK (nome IN ('STANDARD', 'PREMIUM', 'VIP'))
);

-- Tabela de recurso
CREATE TABLE recursosala (
    id SERIAL PRIMARY KEY,
    recurso_nome VARCHAR(50) UNIQUE NOT NULL
);

-- Tabela de sala
CREATE TABLE sala (
    id SERIAL PRIMARY KEY,
    capacidade INTEGER NOT NULL,
    tipo_id INTEGER NOT NULL,
    FOREIGN KEY (tipo_id) REFERENCES tiposala (id)
);

-- Relacionamento sala x recurso (N para N)
CREATE TABLE sala_recurso (
    sala_id INTEGER NOT NULL,
    recurso_id INTEGER NOT NULL,
    PRIMARY KEY (sala_id, recurso_id),
    FOREIGN KEY (sala_id) REFERENCES sala(id) ON DELETE CASCADE,
    FOREIGN KEY (recurso_id) REFERENCES recursosala(id) ON DELETE CASCADE
);

-- Tabela de cliente
CREATE TABLE cliente (
    documento VARCHAR(20) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    corporativo BOOLEAN NOT NULL DEFAULT FALSE
);

-- Tabela de reserva
CREATE TABLE reserva (
    id SERIAL PRIMARY KEY,
    cliente_documento VARCHAR(20) NOT NULL,
    sala_id INTEGER NOT NULL,
    inicio TIMESTAMP NOT NULL,
    fim TIMESTAMP NOT NULL,
    cancelada BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (cliente_documento) REFERENCES cliente(documento) ON DELETE CASCADE,
    FOREIGN KEY (sala_id) REFERENCES sala(id) ON DELETE CASCADE
);

-- Inserindo tipos de sala
INSERT INTO tiposala (nome, capacidade) VALUES
('STANDARD', 100),
('PREMIUM', 80),
('VIP', 150);

-- Inserindo recursos disponíveis
INSERT INTO recursosala (recurso_nome) VALUES
('Projetor'),
('Ar Condicionado');

-- Inserindo salas
INSERT INTO sala (capacidade, tipo_id) VALUES
(100, 1), -- Standard
(80, 2),  -- Premium
(150, 3); -- VIP

-- Relacionando salas aos recursos
-- Standard → Nenhum recurso

-- Premium → Projetor e Ar Condicionado
INSERT INTO sala_recurso VALUES (2, 1); -- Projetor
INSERT INTO sala_recurso VALUES (2, 2); -- Ar Condicionado

-- VIP → Todos os recursos
INSERT INTO sala_recurso VALUES (3, 1);
INSERT INTO sala_recurso VALUES (3, 2);

-- Inserindo clientes
INSERT INTO cliente (documento, nome, corporativo) VALUES
('12345678900', 'João da Silva', FALSE),
('98765432000100', 'Empresa XYZ', TRUE);

-- Inserindo reservas (exemplos)
INSERT INTO reserva (cliente_documento, sala_id, inicio, fim, cancelada) VALUES
('12345678900', 1, '2025-06-07 10:00:00', '2025-06-07 12:00:00', FALSE),
('98765432000100', 2, '2025-06-08 14:00:00', '2025-06-08 16:00:00', FALSE);
