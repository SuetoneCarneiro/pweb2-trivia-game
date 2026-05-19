package br.edu.ifpb.pweb2.psp.trivia.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.ifpb.pweb2.psp.trivia.entities.Corrida;
import br.edu.ifpb.pweb2.psp.trivia.entities.Participante;
import br.edu.ifpb.pweb2.psp.trivia.entities.Resultado;
import br.edu.ifpb.pweb2.psp.trivia.services.CorridaService;
import br.edu.ifpb.pweb2.psp.trivia.services.ResultadoService;
import jakarta.servlet.http.HttpSession;

@Controller
public class RankingController {

    @Autowired
    private ResultadoService resultadoService;

    @Autowired
    private CorridaService corridaService;

    @GetMapping("/resultado")
    public String mostrarResultado(HttpSession session, Model model) {
        Participante participante = (Participante) session.getAttribute("participanteLogado");
        if (participante == null) {
            return "redirect:/login";
        }
        BigDecimal pontuacao = (BigDecimal) session.getAttribute("pontuacao");
        String corridaTitulo = (String) session.getAttribute("ultimoCorridaTitulo");

        model.addAttribute("pontuacao", pontuacao != null ? pontuacao : BigDecimal.ZERO);
        model.addAttribute("corridaTitulo", corridaTitulo != null ? corridaTitulo : "Última corrida");
        session.removeAttribute("pontuacao");
        session.removeAttribute("ultimoCorridaTitulo");

        return "resultado";
    }

    @GetMapping("/ranking")
    public String ranking(@RequestParam(required = false) Long corridaId,
                          HttpSession session, Model model) {
        Participante participante = (Participante) session.getAttribute("participanteLogado");
        if (participante == null) {
            return "redirect:/login";
        }

        List<Corrida> corridas = corridaService.listarAtivas();
        model.addAttribute("corridas", corridas);

        if (corridaId != null) {
            Corrida corrida = corridaService.buscarPorId(corridaId);
            List<Resultado> resultados = resultadoService.listarRankingPorCorrida(corridaId);
            model.addAttribute("resultados", resultados);
            model.addAttribute("corridaSelecionada", corrida);
            model.addAttribute("corridaIdFiltro", corridaId);
        } else {
            List<Resultado> resultados = resultadoService.listarRankingGeral();
            model.addAttribute("resultados", resultados);
        }

        return "ranking";
    }
}