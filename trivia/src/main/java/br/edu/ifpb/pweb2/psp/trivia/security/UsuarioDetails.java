package br.edu.ifpb.pweb2.psp.trivia.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.edu.ifpb.pweb2.psp.trivia.entities.Participante;

// Adapta um Participante para o contrato de UserDetails do Spring Security,
// mantendo a referência à entidade para uso após o login
public class UsuarioDetails implements UserDetails {

    private final Participante participante;

    public UsuarioDetails(Participante participante) {
        this.participante = participante;
    }

    public Participante getParticipante() {
        return participante;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return participante.getPerfis().stream()
                .map(perfil -> new SimpleGrantedAuthority("ROLE_" + perfil.name()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return participante.getSenha();
    }

    @Override
    public String getUsername() {
        return participante.getNome();
    }
}
