
CREATE SEQUENCE setor_id_seq START WITH 1 INCREMENT BY 1 CACHE 100;
CREATE TABLE Setor(
	id INTEGER PRIMARY KEY,
	nome VARCHAR(50) NOT NULL,
	subsetor VARCHAR(50)
);
ALTER TABLE Setor ALTER COLUMN id SET DEFAULT nextval('setor_id_seq');

CREATE SEQUENCE acao_id_seq START WITH 1 INCREMENT BY 1 CACHE 100;
CREATE TABLE Acao (
	nome VARCHAR(75),
	codigo VARCHAR(6),
	quantidade BIGINT,
	id INTEGER PRIMARY KEY,
	id_setor INTEGER REFERENCES Setor(id)
);
ALTER TABLE Acao ALTER COLUMN id SET DEFAULT nextval('acao_id_seq');

CREATE SEQUENCE desempenho_id_seq START WITH 1 INCREMENT BY 1 CACHE 100;
CREATE TABLE Desempenhofinanceiro (
	id INTEGER PRIMARY KEY,
	evolucaolucroliquido_trimestral NUMERIC,
	evolucaolucroliquido_anual NUMERIC,
	hascrescimentolucroliquido_tresanos BOOLEAN	
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
	caixadisponivel_dividabruta NUMERIC	
);
ALTER TABLE Multiplosfundamentalistas ALTER COLUMN id SET DEFAULT nextval('multiplos_id_seq');

CREATE SEQUENCE balanco_id_seq START WITH 1 INCREMENT BY 1 CACHE 100;
CREATE TABLE Balanco (
	id INTEGER PRIMARY KEY,
	data DATE,
	lucroliq_trimestral BIGINT,
	nota INTEGER,
	id_acao INTEGER REFERENCES Acao(id),
	trimestre VARCHAR(55),
	id_multiplos INTEGER REFERENCES Multiplosfundamentalistas(id),
	id_desempenho INTEGER REFERENCES Desempenhofinanceiro(id),
	lucroliq_anual BIGINT,
	cotacao NUMERIC,
	patrimonioliquido BIGINT,
	dividabruta BIGINT,
	caixadisponivel BIGINT,
	justificativa_nota VARCHAR(1000),
	isdailyupdated BOOLEAN
);
ALTER TABLE Balanco ALTER COLUMN id SET DEFAULT nextval('balanco_id_seq');

CREATE SEQUENCE usuario_id_seq START WITH 1 INCREMENT BY 1 CACHE 100;
CREATE TABLE Usuario (
	id INTEGER PRIMARY KEY,
	apelido VARCHAR(30) NOT NULL,
	email VARCHAR(70) NOT NULL,
	senha VARCHAR(100) NOT NULL,
	papel VARCHAR (13) NOT NULL
);
ALTER TABLE Usuario ALTER COLUMN id SET DEFAULT nextval('usuario_id_seq');