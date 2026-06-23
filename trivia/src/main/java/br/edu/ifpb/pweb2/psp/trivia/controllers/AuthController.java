package br.edu.ifpb.pweb2.psp.trivia.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.edu.ifpb.pweb2.psp.trivia.entities.Corrida;
import br.edu.ifpb.pweb2.psp.trivia.entities.Participante;
import br.edu.ifpb.pweb2.psp.trivia.entities.Resultado;
import br.edu.ifpb.pweb2.psp.trivia.services.CorridaService;
import br.edu.ifpb.pweb2.psp.trivia.services.ResultadoService;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    private CorridaService corridaService;

    @Autowired
    private ResultadoService resultadoService;

    // Autenticação (POST /login) e logout são tratados pelo Spring Security.
    @GetMapping("/")
    public String index(Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }
        boolean admin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return admin ? "redirect:/admin/dashboard" : "redirect:/lobby";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/lobby")
    public String lobby(HttpSession session, Model model) {
        Participante participante = (Participante) session.getAttribute("participanteLogado");
        if (participante == null) {
            return "redirect:/login";
        }

        List<Corrida> corridas = corridaService.listarAtivas();
        List<Resultado> resultados = resultadoService.listarPorParticipante(participante.getId());
        List<Long> corridasRespondidasIds = resultados.stream()
            .map(resultado -> resultado.getIdCorrida().getId())
            .toList();

        model.addAttribute("corridas", corridas);
        model.addAttribute("participante", participante);
        model.addAttribute("resultados", resultados);
        model.addAttribute("corridasRespondidasIds", corridasRespondidasIds);
        return "lobby";
    }
}