package br.edu.ifpb.pweb2.psp.trivia.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import br.edu.ifpb.pweb2.psp.trivia.entities.Corrida;
import br.edu.ifpb.pweb2.psp.trivia.repositories.CorridaRepository;

@Service
public class CorridaService {
    @Autowired
    private CorridaRepository corridaRepository;

    public List<Corrida> listarTodas() {
        return corridaRepository.findAll();
    }

    public List<Corrida> listarAtivas() {
        return corridaRepository.findAll().stream()
                .filter(c -> Boolean.TRUE.equals(c.getAtivo()))
                .toList();
    }

    public Corrida buscarPorId(Long id) {
        return corridaRepository.findById(id).orElse(null);
    }

    public Corrida salvar(Corrida corrida) {
        return corridaRepository.save(corrida);
    }

    public void excluir(Long id) {
        corridaRepository.deleteById(id);
    }

    // Soft delete - apenas desativa a corrida em vez de removê-la do banco. A corrida não aparece mais no lobby
    public Corrida desativar(Long id) {
        Corrida corrida = buscarPorId(id);
        if (corrida == null) {
            return null;
        }
        corrida.setAtivo(false);
        return corridaRepository.save(corrida);
    }

    // Reativa uma corrida desativada, tornando-a visível novamente no lobby.
    public Corrida reativar(Long id) {
        Corrida corrida = buscarPorId(id);
        if (corrida == null) {
            return null;
        }
        corrida.setAtivo(true);
        return corridaRepository.save(corrida);
    }
}