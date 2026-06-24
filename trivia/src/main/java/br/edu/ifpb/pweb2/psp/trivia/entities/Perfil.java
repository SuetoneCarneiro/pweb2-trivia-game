package br.edu.ifpb.pweb2.psp.trivia.entities;

// Nossos 2 perfis de acesso de um Participante. São mapeados como authorities do Spring
// Security no formato ROLE_{<NOME>} (exemplo: ROLE_ADMIN, ROLE_PARTICIPANTE)
public enum Perfil {
    ADMIN,
    PARTICIPANTE
}
