CREATE TABLE Participante(
id     serial  PRIMARY KEY,
nome   varchar(80),
email  varchar(80) UNIQUE,
adm    boolean   
);

CREATE TABLE Corrida(
id         serial PRIMARY KEY,
titulo     varchar(80),
descricao  text,
tempo      integer,
ativo      boolean
);

CREATE TABLE Pergunta(
id          serial PRIMARY KEY,
enunciado   text,
resposta    integer,
id_corrida  integer   
);

CREATE TABLE Resultado(
id                serial PRIMARY KEY,
pontuacao         numeric(10,2),
data_hora         timestamp DEFAULT CURRENT_TIMESTAMP,
id_participante   integer,
id_corrida        integer
);

ALTER TABLE Pergunta ADD CONSTRAINT FK_Corrida FOREIGN KEY(id_corrida) REFERENCES Corrida(id);
ALTER TABLE Resultado ADD CONSTRAINT FK_Participante FOREIGN KEY(id_participante) REFERENCES Participante(id);
ALTER TABLE Resultado ADD CONSTRAINT FK_Corrida FOREIGN KEY(id_corrida) REFERENCES Corrida(id);

-- DROP TABLE RESULTADO;
-- DROP TABLE PERGUNTA;
-- DROP TABLE CORRIDA;
-- DROP TABLE PARTICIPANTE;
