package br.edu.ifpb.pweb2.psp.trivia.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import br.edu.ifpb.pweb2.psp.trivia.entities.Participante;
import br.edu.ifpb.pweb2.psp.trivia.repositories.ParticipanteRepository;

@Service
public class ParticipanteService {
    @Autowired
    private ParticipanteRepository participanteRepository;

    public Participante buscarOuCriar(String nome) {
        Optional<Participante> existente = participanteRepository.findAll().stream()
                .filter(p -> p.getNome().equalsIgnoreCase(nome))
                .findFirst();
        if (existente.isPresent()) {
            return existente.get();
        }
        Participante novo = new Participante();
        novo.setNome(nome);
        novo.setEmail(nome.toLowerCase().replace(" ", "") + "@trivia.com");
        novo.setAdm(false);
        return participanteRepository.save(novo);
    }

    public Participante buscarPorId(Long id) {
        return participanteRepository.findById(id).orElse(null);
    }

    public Participante salvar(Participante participante) {
        return participanteRepository.save(participante);
    }

    public boolean isAdmin(Participante participante) {
        return participante != null && Boolean.TRUE.equals(participante.getAdm());
    }
}