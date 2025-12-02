-- V1__init.sql - Initial schema creation
-- This script creates the initial tables for the task management system

CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_email ON usuario(email);
CREATE INDEX idx_nombre ON usuario(nombre);

CREATE TABLE IF NOT EXISTS categoria (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    color VARCHAR(7),
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_categoria_nombre ON categoria(nombre);

CREATE TABLE IF NOT EXISTS tarea (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(255) NOT NULL,
    descripcion TEXT,
    estado VARCHAR(50) NOT NULL DEFAULT 'PENDIENTE',
    fecha_vencimiento TIMESTAMP,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    prioridad VARCHAR(50) DEFAULT 'MEDIA',
    usuario_id BIGINT NOT NULL,
    categoria_id BIGINT,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (categoria_id) REFERENCES categoria(id) ON DELETE SET NULL
);

CREATE INDEX idx_usuario_id ON tarea(usuario_id);
CREATE INDEX idx_estado ON tarea(estado);
CREATE INDEX idx_fecha_vencimiento ON tarea(fecha_vencimiento);
CREATE INDEX idx_categoria_id ON tarea(categoria_id);

CREATE TABLE IF NOT EXISTS usuario_categoria (
    usuario_id BIGINT NOT NULL,
    categoria_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, categoria_id),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (categoria_id) REFERENCES categoria(id) ON DELETE CASCADE
);

CREATE INDEX idx_usuario_categoria ON usuario_categoria(usuario_id, categoria_id);
