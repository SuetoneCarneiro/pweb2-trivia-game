package br.edu.ifpb.pweb2.psp.trivia.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.edu.ifpb.pweb2.psp.trivia.entities.Participante;
import br.edu.ifpb.pweb2.psp.trivia.repositories.ParticipanteRepository;

// Carrega o participante pelo nome
@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final ParticipanteRepository participanteRepository;

    public UsuarioDetailsService(ParticipanteRepository participanteRepository) {
        this.participanteRepository = participanteRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String nome) throws UsernameNotFoundException {
        Participante participante = participanteRepository.findByNomeIgnoreCase(nome)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + nome));
        return new UsuarioDetails(participante);
    }
}
