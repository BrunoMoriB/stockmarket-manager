/*TODO: Transformar todos os campos 'id' em bigint */

CREATE SEQUENCE setor_id_seq START WITH 1 INCREMENT BY 1 CACHE 100;
CREATE TABLE Setor(
	id INTEGER PRIMARY KEY,
	nome VARCHAR(50) NOT NULL
);
ALTER TABLE Setor ALTER COLUMN id SET DEFAULT nextval('setor_id_seq');

CREATE SEQUENCE empresa_id_seq START WITH 1 INCREMENT BY 1 CACHE 100;
CREATE TABLE Empresa(
	id INTEGER PRIMARY KEY,
	razao_social VARCHAR(70) NOT NULL,
    nome_pregao VARCHAR(70) NOT NULL CONSTRAINT empresa_nome_pregao_unq UNIQUE,
    cnpj VARCHAR(14) NOT NULL CONSTRAINT empresa_cnpj_unq UNIQUE,
    quantidade_papeis BIGINT
);
ALTER TABLE Empresa ALTER COLUMN id SET DEFAULT nextval('empresa_id_seq');

CREATE TABLE EmpresaSetor(
    id_empresa INTEGER REFERENCES Empresa(id),
    id_setor INTEGER REFERENCES Setor(id),
    PRIMARY KEY(id_empresa, id_setor)
);

CREATE SEQUENCE acao_id_seq START WITH 1 INCREMENT BY 1 CACHE 100;
CREATE TABLE Acao (
	codigo VARCHAR(6),
	id INTEGER PRIMARY KEY,
	id_empresa INTEGER REFERENCES Empresa(id) NOT NULL
);
ALTER TABLE Acao ALTER COLUMN id SET DEFAULT nextval('acao_id_seq');

CREATE SEQUENCE provento_id_seq START WITH 1 INCREMENT BY 1 CACHE 100;
CREATE TABLE Provento (
	id INTEGER PRIMARY KEY,
	tipo VARCHAR(11) NOT NULL,
	valor NUMERIC NOT NULL,
	data_ex DATE NOT NULL,
	data_pagamento DATE,
	id_acao INTEGER REFERENCES Acao(id) NOT NULL
);
ALTER TABLE Provento ALTER COLUMN id SET DEFAULT nextval('provento_id_seq');

CREATE SEQUENCE cotacao_id_seq START WITH 1 INCREMENT BY 1 CACHE 100;
CREATE TABLE Cotacao (
	id BIGINT PRIMARY KEY,
	data DATE NOT NULL,
	valor NUMERIC NOT NULL,
	id_acao BIGINT REFERENCES Acao(id) NOT NULL,
	isdailyupdated BOOLEAN
);
ALTER TABLE Cotacao ALTER COLUMN id SET DEFAULT nextval('cotacao_id_seq');

CREATE SEQUENCE balanco_id_seq START WITH 1 INCREMENT BY 1 CACHE 100;
CREATE TABLE Balanco (
	id INTEGER PRIMARY KEY,
	lucroliq_trimestral BIGINT,
	id_empresa INTEGER REFERENCES Empresa(id),
	trimestre INTEGER,
	lucroliq_anual BIGINT,
	patrimonioliquido BIGINT,
	dividabruta BIGINT,
	caixadisponivel BIGINT,
	ebit BIGINT,
	liquidez_corrente BIGINT,
	receita_liquida BIGINT,
	ano INTEGER,
	isdailyupdated BOOLEAN
);
ALTER TABLE Balanco ALTER COLUMN id SET DEFAULT nextval('balanco_id_seq');

CREATE SEQUENCE desempenho_id_seq START WITH 1 INCREMENT BY 1 CACHE 100;
CREATE TABLE Desempenhofinanceiro (
	id INTEGER PRIMARY KEY,
	evolucaolucroliquido_trimestral NUMERIC,
	evolucaolucroliquido_anual NUMERIC,
	hascrescimentolucroliquido_tresanos BOOLEAN,
	id_balanco INTEGER REFERENCES Balanco(id)
);
ALTER TABLE Desempenhofinanceiro ALTER COLUMN id SET DEFAULT nextval('desempenho_id_seq');

CREATE SEQUENCE multiplos_id_seq START WITH 1 INCREMENT BY 1 CACHE 100;
CREATE TABLE Multiplosfundamentalistas (
	id INTEGER PRIMARY KEY,
	preco_lucro NUMERIC,
	preco_valorpatrimonial NUMERIC,
	roe NUMERIC,
	dividabruta_patrimonioliquido NUMERIC,
	media_precolucro NUMERIC,
	media_precovalorpatrimonial NUMERIC,
	caixadisponivel_dividabruta NUMERIC,
	nota INTEGER,
	justificativa_nota VARCHAR(1000),
	id_balanco INTEGER REFERENCES Balanco(id),
	id_acao INTEGER REFERENCES Acao(id)
);
ALTER TABLE Multiplosfundamentalistas ALTER COLUMN id SET DEFAULT nextval('multiplos_id_seq');

CREATE SEQUENCE usuario_id_seq START WITH 1 INCREMENT BY 1 CACHE 100;
CREATE TABLE Usuario (
	id INTEGER PRIMARY KEY,
	apelido VARCHAR(30) NOT NULL,
	email VARCHAR(70) NOT NULL,
	senha VARCHAR(100) NOT NULL,
	papel VARCHAR (13) NOT NULL
);
ALTER TABLE Usuario ALTER COLUMN id SET DEFAULT nextval('usuario_id_seq');

CREATE SEQUENCE units_id_seq START WITH 1 INCREMENT BY 1 CACHE 100;
CREATE TABLE Units (
	id INTEGER PRIMARY KEY,
	id_acao INTEGER REFERENCES Acao(id) NOT NULL,
	qtd_on INTEGER NOT NULL,
	qtd_pn INTEGER NOT NULL	
);
ALTER TABLE Units ALTER COLUMN id SET DEFAULT nextval('units_id_seq');