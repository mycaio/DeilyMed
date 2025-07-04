-- Habilita o suporte a chaves estrangeiras. É essencial para a integridade dos dados.
PRAGMA foreign_keys = ON;

-- Tabela: especialidades
-- Armazena as especialidades médicas.
CREATE TABLE IF NOT EXISTS especialidades (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL UNIQUE
);

-- Tabela: pacientes
-- Armazena os dados dos pacientes.
CREATE TABLE IF NOT EXISTS pacientes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    cpf TEXT NOT NULL UNIQUE,
    telefone TEXT,
    email TEXT UNIQUE
);

-- Tabela: medicos
-- Armazena os dados dos médicos, com referência à sua especialidade.
CREATE TABLE IF NOT EXISTS medicos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    cpf TEXT NOT NULL UNIQUE,
    crm TEXT NOT NULL UNIQUE,
    especialidade_id INTEGER NOT NULL,
    FOREIGN KEY (especialidade_id) REFERENCES especialidades (id)
);

-- Tabela: horarios_padrao_medicos
-- Armazena a grade de horários padrão de cada médico.
CREATE TABLE IF NOT EXISTS horarios_padrao_medicos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    medico_id INTEGER NOT NULL,
    dia_semana INTEGER NOT NULL CHECK(dia_semana >= 1 AND dia_semana <= 7), -- 1=Domingo, 2=Segunda, etc.
    horario TEXT NOT NULL, -- Formato 'HH:MM'
    FOREIGN KEY (medico_id) REFERENCES medicos (id),
    UNIQUE (medico_id, dia_semana, horario) -- Evita duplicidade de horários
);

-- Tabela: consultas
-- Tabela principal que conecta médicos e pacientes em um horário específico.
CREATE TABLE IF NOT EXISTS consultas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    paciente_id INTEGER NOT NULL,
    medico_id INTEGER NOT NULL,
    data_hora TEXT NOT NULL, -- Armazenar como texto no formato ISO8601 'YYYY-MM-DD HH:MM:SS'
    status TEXT NOT NULL DEFAULT 'AGENDADA' CHECK(status IN ('AGENDADA', 'CANCELADA', 'REALIZADA')),
    FOREIGN KEY (paciente_id) REFERENCES pacientes (id),
    FOREIGN KEY (medico_id) REFERENCES medicos (id),
    UNIQUE(medico_id, data_hora) -- Impede conflito de horário no banco
);