package br.edu.ifpb.pweb2.psp.trivia.controllers;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.pweb2.psp.trivia.entities.Corrida;
import br.edu.ifpb.pweb2.psp.trivia.entities.Participante;
import br.edu.ifpb.pweb2.psp.trivia.services.CorridaService;
import br.edu.ifpb.pweb2.psp.trivia.services.ParticipanteService;

@Controller
public class AuthController {

    @Autowired
    private ParticipanteService participanteService;

    @Autowired
    private CorridaService corridaService;

    @GetMapping("/")
    public String index(HttpSession session) {
        Participante participante = (Participante) session.getAttribute("participanteLogado");
        if (participante != null) {
            return "redirect:/lobby";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String nome, HttpSession session, RedirectAttributes redirectAttributes) {
        if (nome == null || nome.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("mensagem", "Por favor, informe seu nome.");
            return "redirect:/login";
        }

        Participante participante = participanteService.buscarOuCriar(nome.trim());
        session.setAttribute("participanteLogado", participante);
        return "redirect:/lobby";
    }

    @GetMapping("/lobby")
    public String lobby(HttpSession session, Model model) {
        Participante participante = (Participante) session.getAttribute("participanteLogado");
        if (participante == null) {
            return "redirect:/login";
        }

        List<Corrida> corridas = corridaService.listarAtivas();
        model.addAttribute("corridas", corridas);
        model.addAttribute("participante", participante);
        return "lobby";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
