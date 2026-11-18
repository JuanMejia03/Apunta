-- Creación de la tabla Usuario
CREATE TABLE usuarios (
    id VARCHAR(36) PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    xp_total INT NOT NULL DEFAULT 0,
    meta_xp INT NOT NULL DEFAULT 100,
    dia_racha INT NOT NULL DEFAULT 0,
    ultima_actividad DATETIME NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Creación de la tabla Documento
CREATE TABLE documentos (
    id VARCHAR(36) PRIMARY KEY,
    usuario_id VARCHAR(36) NOT NULL,
    nombre_archivo VARCHAR(255) NOT NULL,
    tipo ENUM('PDF', 'TXT') NOT NULL,
    tamano BIGINT NOT NULL,
    ruta_almacenamiento VARCHAR(500) NOT NULL,
    texto_extraido LONGTEXT NULL,
    fecha_subido DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_documento_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Creación de la tabla Pregunta
CREATE TABLE preguntas (
    id VARCHAR(36) PRIMARY KEY,
    documento_id VARCHAR(36) NOT NULL,
    enunciado TEXT NOT NULL,
    tema VARCHAR(255) NOT NULL,
    retroalimentacion TEXT NULL,
    CONSTRAINT fk_pregunta_documento FOREIGN KEY (documento_id) REFERENCES documentos(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Creación de la tabla Opcion (4 opciones por pregunta)
CREATE TABLE opciones (
    id VARCHAR(36) PRIMARY KEY,
    pregunta_id VARCHAR(36) NOT NULL,
    literal CHAR(1) NOT NULL,
    texto TEXT NOT NULL,
    es_correcta BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_opcion_pregunta FOREIGN KEY (pregunta_id) REFERENCES preguntas(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Creación de la tabla Reto
CREATE TABLE retos (
    id VARCHAR(36) PRIMARY KEY,
    usuario_id VARCHAR(36) NOT NULL,
    documento_id VARCHAR(36) NULL,
    fecha_inicio DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_fin DATETIME NULL,
    duracion_segundos INT NOT NULL DEFAULT 0,
    total_preguntas INT NOT NULL DEFAULT 0,
    respuestas_acertadas INT NOT NULL DEFAULT 0,
    xp_ganado INT NOT NULL DEFAULT 0,
    estado ENUM('EN_PROCESO', 'COMPLETADO', 'ABONADO') NOT NULL DEFAULT 'EN_PROCESO',
    CONSTRAINT fk_reto_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_reto_documento FOREIGN KEY (documento_id) REFERENCES documentos(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Creación de la tabla Respuesta Usuario (Historial de respuestas por cada reto)
CREATE TABLE respuestas_usuario (
    id VARCHAR(36) PRIMARY KEY,
    reto_id VARCHAR(36) NOT NULL,
    pregunta_id VARCHAR(36) NOT NULL,
    opcion_id VARCHAR(36) NOT NULL,
    es_correcta BOOLEAN NOT NULL,
    orden_pregunta INT NOT NULL,
    CONSTRAINT fk_resp_usuario_reto FOREIGN KEY (reto_id) REFERENCES retos(id) ON DELETE CASCADE,
    CONSTRAINT fk_resp_usuario_pregunta FOREIGN KEY (pregunta_id) REFERENCES preguntas(id) ON DELETE CASCADE,
    CONSTRAINT fk_resp_usuario_opcion FOREIGN KEY (opcion_id) REFERENCES opciones(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Índices para realizar consultas frecuentes
CREATE INDEX idx_documentos_usuario ON documentos(usuario_id);
CREATE INDEX idx_preguntas_documento ON preguntas(documento_id);
CREATE INDEX idx_opciones_pregunta ON opciones(pregunta_id);
CREATE INDEX idx_retos_usuario ON retos(usuario_id);
CREATE INDEX idx_resp_usuario_reto ON respuestas_usuario(reto_id);