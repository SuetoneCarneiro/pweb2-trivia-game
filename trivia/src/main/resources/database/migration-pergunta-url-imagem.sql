-- Migração: adiciona suporte a imagem na tabela Pergunta
-- Executar em bancos já existentes (criados antes desta alteração).
-- Bancos novos podem usar trivia-game.sql diretamente.

ALTER TABLE Pergunta
    ADD COLUMN IF NOT EXISTS url_imagem varchar(500);
