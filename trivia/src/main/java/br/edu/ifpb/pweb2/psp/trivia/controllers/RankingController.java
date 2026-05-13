package br.edu.ifpb.pweb2.psp.trivia.controllers;

import java.util.List;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import br.edu.ifpb.pweb2.psp.trivia.entities.Participante;
import br.edu.ifpb.pweb2.psp.trivia.entities.Resultado;
import br.edu.ifpb.pweb2.psp.trivia.services.ResultadoService;

@Controller
public class RankingController {

    @Autowired
    private ResultadoService resultadoService;

    @GetMapping("/resultado")
    public String mostrarResultado(HttpSession session, Model model) {
        Participante participante = (Participante) session.getAttribute("participanteLogado");
        if (participante == null) {
            return "redirect:/login";
        }

        model.addAttribute("corridaTitulo", "Última corrida");
        model.addAttribute("pontuacao", 0);
        return "resultado";
    }

    @GetMapping("/ranking")
    public String rankingGeral(HttpSession session, Model model) {
        Participante participante = (Participante) session.getAttribute("participanteLogado");
        if (participante == null) {
            return "redirect:/login";
        }

        List<Resultado> resultados = resultadoService.listarRankingGeral();
        model.addAttribute("resultados", resultados);
        return "ranking";
    }
}