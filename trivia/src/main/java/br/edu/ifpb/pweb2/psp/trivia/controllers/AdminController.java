package br.edu.ifpb.pweb2.psp.trivia.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import br.edu.ifpb.pweb2.psp.trivia.entities.Corrida;
import br.edu.ifpb.pweb2.psp.trivia.entities.Pergunta;
import br.edu.ifpb.pweb2.psp.trivia.entities.Participante;
import br.edu.ifpb.pweb2.psp.trivia.entities.Resultado;
import br.edu.ifpb.pweb2.psp.trivia.services.CorridaService;
import br.edu.ifpb.pweb2.psp.trivia.services.PerguntaService;
import br.edu.ifpb.pweb2.psp.trivia.services.ResultadoService;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CorridaService corridaService;

    @Autowired
    private PerguntaService perguntaService;

    @Autowired
    private ResultadoService resultadoService;

    private boolean isAdmin(HttpSession session) {
        Participante p = (Participante) session.getAttribute("participanteLogado");
        return p != null && Boolean.TRUE.equals(p.getAdm());
    }

    // Dashboard principal
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model, RedirectAttributes ra) {
        if (!isAdmin(session)) {
            ra.addFlashAttribute("mensagem", "Acesso negado.");
            return "redirect:/lobby";
        }
        List<Corrida> corridas = corridaService.listarTodas();
        model.addAttribute("corridas", corridas);
        return "admin/dashboard";
    }

    //corridas
    @GetMapping("/corridas/nova")
    public String novaCorrida(HttpSession session, Model model, RedirectAttributes ra) {
        if (!isAdmin(session)) {
            ra.addFlashAttribute("mensagem", "Acesso negado.");
            return "redirect:/lobby";
        }
        model.addAttribute("corrida", new Corrida());
        return "admin/corrida-form";
    }

    @GetMapping("/corridas/editar/{id}")
    public String editarCorrida(@PathVariable Long id, HttpSession session, Model model, RedirectAttributes ra) {
        if (!isAdmin(session)) {
            ra.addFlashAttribute("mensagem", "Acesso negado.");
            return "redirect:/lobby";
        }
        Corrida corrida = corridaService.buscarPorId(id);
        if (corrida == null) {
            ra.addFlashAttribute("mensagem", "Corrida não encontrada.");
            return "redirect:/admin/dashboard";
        }
        model.addAttribute("corrida", corrida);
        return "admin/corrida-form";
    }

    @PostMapping("/corridas/salvar")
    public String salvarCorrida(@ModelAttribute Corrida corrida, HttpSession session, RedirectAttributes ra) {
        if (!isAdmin(session)) {
            ra.addFlashAttribute("mensagem", "Acesso negado.");
            return "redirect:/lobby";
        }
        if (corrida.getAtivo() == null) corrida.setAtivo(false);
        corridaService.salvar(corrida);
        ra.addFlashAttribute("mensagem", "Corrida salva com sucesso!");
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/corridas/excluir/{id}")
    public String excluirCorrida(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        if (!isAdmin(session)) {
            ra.addFlashAttribute("mensagem", "Acesso negado.");
            return "redirect:/lobby";
        }
        corridaService.excluir(id);
        ra.addFlashAttribute("mensagem", "Corrida excluída.");
        return "redirect:/admin/dashboard";
    }

    //toda a parte das perguntas de uma corrida
    @GetMapping("/perguntas/{idCorrida}")
    public String listarPerguntas(@PathVariable Long idCorrida, HttpSession session, Model model, RedirectAttributes ra) {
        if (!isAdmin(session)) {
            ra.addFlashAttribute("mensagem", "Acesso negado.");
            return "redirect:/lobby";
        }
        Corrida corrida = corridaService.buscarPorId(idCorrida);
        if (corrida == null) {
            ra.addFlashAttribute("mensagem", "Corrida não encontrada.");
            return "redirect:/admin/dashboard";
        }
        model.addAttribute("corrida", corrida);
        model.addAttribute("perguntas", perguntaService.listarPorCorrida(idCorrida));
        return "admin/perguntas";
    }

    @GetMapping("/perguntas/nova/{idCorrida}")
    public String novaPergunta(@PathVariable Long idCorrida, HttpSession session, Model model, RedirectAttributes ra) {
        if (!isAdmin(session)) {
            ra.addFlashAttribute("mensagem", "Acesso negado.");
            return "redirect:/lobby";
        }
        Corrida corrida = corridaService.buscarPorId(idCorrida);
        if (corrida == null) {
            ra.addFlashAttribute("mensagem", "Corrida não encontrada.");
            return "redirect:/admin/dashboard";
        }
        Pergunta pergunta = new Pergunta();
        pergunta.setIdCorrida(corrida);
        model.addAttribute("pergunta", pergunta);
        model.addAttribute("corrida", corrida);
        return "admin/pergunta-form";
    }

    @PostMapping("/perguntas/salvar")
    public String salvarPergunta(@ModelAttribute Pergunta pergunta,
                                 @RequestParam("indiceResposta") int indiceResposta,
                                 @RequestParam("alternativasList") String alternativasRaw,
                                 HttpSession session, RedirectAttributes ra) {
        if (!isAdmin(session)) {
            ra.addFlashAttribute("mensagem", "Acesso negado.");
            return "redirect:/lobby";
        }
        // processar alternativas (separadas por ;) ajustar depois com a equipe
        List<String> alternativas = Arrays.asList(alternativasRaw.split("\\s*;\\s*"));
        pergunta.setAlternativas(alternativas);
        pergunta.setResposta(indiceResposta);
        perguntaService.salvar(pergunta);
        ra.addFlashAttribute("mensagem", "Pergunta salva com sucesso!");
        return "redirect:/admin/perguntas/" + pergunta.getIdCorrida().getId();
    }

    @GetMapping("/perguntas/excluir/{id}")
    public String excluirPergunta(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        if (!isAdmin(session)) {
            ra.addFlashAttribute("mensagem", "Acesso negado.");
            return "redirect:/lobby";
        }
        Pergunta p = perguntaService.buscarPorId(id);
        if (p == null) {
            ra.addFlashAttribute("mensagem", "Pergunta não encontrada.");
            return "redirect:/admin/dashboard";
        }
        Long idCorrida = p.getIdCorrida().getId();
        perguntaService.excluir(id);
        ra.addFlashAttribute("mensagem", "Pergunta excluída.");
        return "redirect:/admin/perguntas/" + idCorrida;
    }

    // preparado para mostrar os participantes de determinada corrida  
    @GetMapping("/participantes/{idCorrida}")
    public String verParticipantes(@PathVariable Long idCorrida, HttpSession session, Model model, RedirectAttributes ra) {
        if (!isAdmin(session)) {
            ra.addFlashAttribute("mensagem", "Acesso negado.");
            return "redirect:/lobby";
        }
        Corrida corrida = corridaService.buscarPorId(idCorrida);
        if (corrida == null) {
            ra.addFlashAttribute("mensagem", "Corrida não encontrada.");
            return "redirect:/admin/dashboard";
        }
        List<Resultado> resultados = resultadoService.listarRankingPorCorrida(idCorrida);
        model.addAttribute("corrida", corrida);
        model.addAttribute("participantes", resultados);
        return "admin/participantes";
    }
}