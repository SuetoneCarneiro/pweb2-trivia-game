package br.edu.ifpb.pweb2.psp.trivia.controllers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.pweb2.psp.trivia.entities.Corrida;
import br.edu.ifpb.pweb2.psp.trivia.entities.Participante;
import br.edu.ifpb.pweb2.psp.trivia.entities.Pergunta;
import br.edu.ifpb.pweb2.psp.trivia.entities.Resultado;
import br.edu.ifpb.pweb2.psp.trivia.services.CorridaService;
import br.edu.ifpb.pweb2.psp.trivia.services.PerguntaService;
import br.edu.ifpb.pweb2.psp.trivia.services.ResultadoService;
import jakarta.servlet.http.HttpSession;

@Controller
public class CorridaController {

    @Autowired
    private CorridaService corridaService;

    @Autowired
    private PerguntaService perguntaService;

    @Autowired
    private ResultadoService resultadoService;

    private static final String SESSAO_CORRIDA_ID = "corridaId";
    private static final String SESSAO_PERGUNTAS = "perguntas";
    private static final String SESSAO_INDICE = "indiceAtual";
    private static final String SESSAO_PONTUACAO = "pontuacao";
    private static final String SESSAO_INICIO = "inicioTimestamp";

    @GetMapping("/corrida/iniciar/{id}")
    public String iniciarCorrida(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        Participante participante = (Participante) session.getAttribute("participanteLogado");
        if (participante == null) {
            return "redirect:/login";
        }

        Corrida corrida = corridaService.buscarPorId(id);
        if (corrida == null || !corrida.getAtivo()) {
            redirectAttributes.addFlashAttribute("mensagem", "Corrida não disponível.");
            return "redirect:/lobby";
        }

        List<Pergunta> perguntas = perguntaService.listarPorCorrida(id);
        if (perguntas.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensagem", "Esta corrida não possui perguntas.");
            return "redirect:/lobby";
        }

        session.setAttribute(SESSAO_CORRIDA_ID, id);
        session.setAttribute(SESSAO_PERGUNTAS, perguntas);
        session.setAttribute(SESSAO_INDICE, 0);
        session.setAttribute(SESSAO_PONTUACAO, BigDecimal.ZERO);
        session.setAttribute(SESSAO_INICIO, System.currentTimeMillis());

        return "redirect:/corrida/pergunta";
    }

    @GetMapping("/corrida/pergunta")
    public String mostrarPergunta(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Participante participante = (Participante) session.getAttribute("participanteLogado");
        if (participante == null) {
            return "redirect:/login";
        }

        Long corridaId = (Long) session.getAttribute(SESSAO_CORRIDA_ID);
        if (corridaId == null) {
            redirectAttributes.addFlashAttribute("mensagem", "Nenhuma corrida em andamento.");
            return "redirect:/lobby";
        }

        Integer indice = (Integer) session.getAttribute(SESSAO_INDICE);
        List<Pergunta> perguntas = (List<Pergunta>) session.getAttribute(SESSAO_PERGUNTAS);
        BigDecimal pontuacao = (BigDecimal) session.getAttribute(SESSAO_PONTUACAO);
        Long inicio = (Long) session.getAttribute(SESSAO_INICIO);

        Corrida corrida = corridaService.buscarPorId(corridaId);
        int tempoDecorrido = (int) ((System.currentTimeMillis() - inicio) / 1000);
        int tempoRestante = corrida.getTempo() - tempoDecorrido;

        if (tempoRestante <= 0) {
            finalizarCorrida(session, redirectAttributes);
            redirectAttributes.addFlashAttribute("mensagem", "Tempo esgotado!");
            return "redirect:/resultado";
        }

        if (indice >= perguntas.size()) {
            finalizarCorrida(session, redirectAttributes);
            return "redirect:/resultado";
        }

        Pergunta perguntaAtual = perguntas.get(indice);
        model.addAttribute("pergunta", perguntaAtual);
        model.addAttribute("corridaTitulo", corrida.getTitulo());
        model.addAttribute("pontuacao", pontuacao);
        model.addAttribute("tempoRestante", tempoRestante);
        return "pergunta";
    }

    @PostMapping("/corrida/responder")
    public String responder(@RequestParam("alternativaEscolhida") int alternativaEscolhida,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        Participante participante = (Participante) session.getAttribute("participanteLogado");
        if (participante == null) {
            return "redirect:/login";
        }

        Integer indice = (Integer) session.getAttribute(SESSAO_INDICE);
        List<Pergunta> perguntas = (List<Pergunta>) session.getAttribute(SESSAO_PERGUNTAS);
        BigDecimal pontuacaoAtual = (BigDecimal) session.getAttribute(SESSAO_PONTUACAO);
        Long inicio = (Long) session.getAttribute(SESSAO_INICIO);
        Long corridaId = (Long) session.getAttribute(SESSAO_CORRIDA_ID);

        Corrida corrida = corridaService.buscarPorId(corridaId);
        int tempoDecorrido = (int) ((System.currentTimeMillis() - inicio) / 1000);
        if (tempoDecorrido > corrida.getTempo()) {
            finalizarCorrida(session, redirectAttributes);
            redirectAttributes.addFlashAttribute("mensagem", "Tempo esgotado!");
            return "redirect:/resultado";
        }

        Pergunta perguntaAtual = perguntas.get(indice);
        if (perguntaAtual.getResposta() != null && perguntaAtual.getResposta().equals(alternativaEscolhida)) {
            pontuacaoAtual = pontuacaoAtual.add(BigDecimal.ONE);
            session.setAttribute(SESSAO_PONTUACAO, pontuacaoAtual);
        }

        int proximoIndice = indice + 1;
        if (proximoIndice >= perguntas.size()) {
            finalizarCorrida(session, redirectAttributes);
            return "redirect:/resultado";
        } else {
            session.setAttribute(SESSAO_INDICE, proximoIndice);
            return "redirect:/corrida/pergunta";
        }
    }

    private void finalizarCorrida(HttpSession session, RedirectAttributes redirectAttributes) {
        Long corridaId = (Long) session.getAttribute(SESSAO_CORRIDA_ID);
        BigDecimal pontuacao = (BigDecimal) session.getAttribute("pontuacao");
        Participante participante = (Participante) session.getAttribute("participanteLogado");

        if (corridaId != null && participante != null && pontuacao != null) {
            Corrida corrida = corridaService.buscarPorId(corridaId);
            Resultado resultado = new Resultado();
            resultado.setPontuacao(pontuacao);
            resultado.setDataHora(LocalDateTime.now());
            resultado.setIdParticipante(participante);
            resultado.setIdCorrida(corrida);
            resultadoService.salvar(resultado);
            redirectAttributes.addFlashAttribute("mensagem", "Parabéns! Você marcou " + pontuacao + " pontos!");
            session.setAttribute("ultimoCorridaTitulo", corrida.getTitulo());
        }

        session.removeAttribute(SESSAO_CORRIDA_ID);
        session.removeAttribute(SESSAO_PERGUNTAS);
        session.removeAttribute(SESSAO_INDICE);
        // session.removeAttribute(SESSAO_PONTUACAO);
        session.removeAttribute(SESSAO_INICIO);
    }
}