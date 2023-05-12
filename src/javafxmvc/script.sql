-- Database: db_academia

-- Creating Tables

CREATE TABLE admin (
  idAdmin SERIAL PRIMARY KEY,
  usuario VARCHAR(20) UNIQUE NOT NULL,
  senha VARCHAR(20) NOT NULL
);

CREATE TABLE funcionario (
  idFuncionario SERIAL PRIMARY KEY,
  nome VARCHAR(50) NOT NULL,
  cpf VARCHAR(14) UNIQUE NOT NULL,
  endereco VARCHAR(100) NOT NULL,
  tipo VARCHAR(20)NOT NULL,
  usuario VARCHAR(20) UNIQUE NOT NULL,
  senha VARCHAR(20) NOT NULL
);

CREATE TABLE Plano (
    idPlano SERIAL PRIMARY KEY,
    tipo VARCHAR(50) NOT NULL,
    preco FLOAT NOT NULL
);

CREATE TABLE Aluno (
    idAluno SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL,
    endereco VARCHAR(200),
    genero VARCHAR(50) NOT NULL,
    pontos INTEGER NOT NULL,
    plano_id INTEGER NOT NULL REFERENCES Plano(idPlano)
);

CREATE TABLE Pagamento (
    idPagamento SERIAL PRIMARY KEY,
    data DATE NOT NULL,
    valor FLOAT NOT NULL,
    aluno_id INTEGER NOT NULL REFERENCES Aluno(idAluno)
);

CREATE TABLE CheckIn (
    idCheckIn SERIAL PRIMARY KEY,
    data DATE NOT NULL,
    hora TIME NOT NULL,
    aluno_id INTEGER NOT NULL REFERENCES Aluno(idAluno)
);

CREATE TABLE CheckOut (
    idCheckOut SERIAL PRIMARY KEY,
    data DATE NOT NULL,
    hora TIME NOT NULL,
    checkIn_id INTEGER NOT NULL REFERENCES CheckIn(idCheckIn)
);

CREATE TABLE Exercicio (
    idExercicio SERIAL PRIMARY KEY,
    tipo VARCHAR(50) NOT NULL,
    musculo VARCHAR(50) NOT NULL,
    numSeries INTEGER NOT NULL,
    numRepeticoes INTEGER NOT NULL
);

CREATE TABLE Treino (
    idTreino SERIAL PRIMARY KEY,
    dataInicio DATE NOT NULL,
    dataFinal DATE NOT NULL,
    aluno_id INTEGER NOT NULL REFERENCES Aluno(idAluno),
    funcionario_id INTEGER NOT NULL REFERENCES Funcionario(idFuncionario)
);

CREATE TABLE TreinoExercicio (
    idTreinoExercicio SERIAL PRIMARY KEY,
    treino_id INTEGER NOT NULL REFERENCES Treino(idTreino),
    exercicio_id INTEGER NOT NULL REFERENCES Exercicio(idExercicio)
);


-- Inserting Data

INSERT INTO admin (usuario, senha)
VALUES ('admin', 'admin');

INSERT INTO Funcionario (nome, cpf, endereco, tipo, usuario, senha)
VALUES ('João Silva', '123.456.789-00', 'Rua dos Funcionários, 123', 'personal trainer', 'joao.silva', 'senha123');

INSERT INTO Plano (tipo, preco)
VALUES ('Plano mensal', 150.00);

INSERT INTO Aluno (nome, cpf, endereco, genero, pontos, plano_id)
VALUES ('Maria Oliveira', '987.654.321-00', 'Avenida dos Alunos, 456', 'Feminino', 0, 1);

INSERT INTO Pagamento (data, valor, aluno_id)
VALUES ('2023-04-13', 100.00, 1);

INSERT INTO CheckIn (data, hora, aluno_id)
VALUES ('2023-04-13', '10:10:10', 1);

INSERT INTO CheckOut (data, hora, checkIn_id)
VALUES ('2023-04-13', '11:00:00', 1);

INSERT INTO Exercicio (tipo, musculo, numSeries, numRepeticoes)
VALUES ('Supino', 'Peitoral', 3, 12);

INSERT INTO Treino (dataInicio, dataFinal, aluno_id, funcionario_id)
VALUES ('2023-04-13', '2023-04-20', 1, 1);

INSERT INTO TreinoExercicio (treino_id, exercicio_id)
VALUES (1, 1);

-- New Inserts (Populating DB) 
-- Inserts for Aluno (testing)
INSERT INTO Aluno (nome, cpf, endereco, genero, pontos, plano_id) VALUES ('Maria Silva', '123.456.789-00', 'Rua A, 123', 'Feminino', 0, 1);
INSERT INTO Aluno (nome, cpf, endereco, genero, pontos, plano_id) VALUES ('João Santos', '987.654.123-00', 'Avenida B, 456', 'Masculino', 0, 1);
INSERT INTO Aluno (nome, cpf, endereco, genero, pontos, plano_id) VALUES ('Ana Souza', '555.444.333-00', 'Rua C, 789', 'Feminino', 0, 1);
INSERT INTO Aluno (nome, cpf, endereco, genero, pontos, plano_id) VALUES ('Lucas Pereira', '777.888.999-00', 'Avenida D, 321', 'Masculino', 0, 1);
INSERT INTO Aluno (nome, cpf, endereco, genero, pontos, plano_id) VALUES ('Julia Fernandes', '111.222.333-00', 'Rua E, 654', 'Feminino', 0, 1);
INSERT INTO Aluno (nome, cpf, endereco, genero, pontos, plano_id) VALUES ('Pedro Oliveira', '999.888.777-00', 'Avenida F, 987', 'Masculino', 0, 1);
INSERT INTO Aluno (nome, cpf, endereco, genero, pontos, plano_id) VALUES ('Fernanda Santos', '444.555.666-00', 'Rua G, 321', 'Feminino', 0, 1);
INSERT INTO Aluno (nome, cpf, endereco, genero, pontos, plano_id) VALUES ('Gustavo Lima', '222.333.444-00', 'Avenida H, 654', 'Masculino', 0, 1);
INSERT INTO Aluno (nome, cpf, endereco, genero, pontos, plano_id) VALUES ('Mariana Ribeiro', '777.777.777-00', 'Rua I, 987', 'Feminino', 0, 1);
INSERT INTO Aluno (nome, cpf, endereco, genero, pontos, plano_id) VALUES ('Felipe Alves', '444.444.444-00', 'Avenida J, 654', 'Masculino', 0, 1);
INSERT INTO Aluno (nome, cpf, endereco, genero, pontos, plano_id) VALUES ('Rafaela Santos', '888.888.888-00', 'Rua K, 321', 'Feminino', 0, 1);
INSERT INTO Aluno (nome, cpf, endereco, genero, pontos, plano_id) VALUES ('Rodrigo Silva', '555.555.555-00', 'Avenida L, 654', 'Masculino', 0, 1);
INSERT INTO Aluno (nome, cpf, endereco, genero, pontos, plano_id) VALUES ('Carla Oliveira', '999.999.999-00', 'Rua M, 987', 'Feminino', 0, 1);
