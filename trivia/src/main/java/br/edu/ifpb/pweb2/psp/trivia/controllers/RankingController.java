package br.edu.ifpb.pweb2.psp.trivia.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @GetMapping("/ranking")
    public String ranking(
            @RequestParam(required = false) Long corridaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            HttpSession session, Model model) {

        // Verifica se o usuário está logado (feito pelo Spring Security, mas mantido)
        Participante participante = (Participante) session.getAttribute("participanteLogado");
        if (participante == null) {
            return "redirect:/login";
        }

        // Carrega a lista de corridas para o filtro 
        model.addAttribute("corridas", corridaService.listarAtivas());

        Page<Resultado> pageResultados;
        if (corridaId != null) {
            Corrida corrida = corridaService.buscarPorId(corridaId);
            // Busca página filtrada por corrida
            pageResultados = resultadoService.listarRankingPorCorridaPaginado(corridaId, page, size);
            model.addAttribute("corridaSelecionada", corrida);
            model.addAttribute("corridaIdFiltro", corridaId);
        } else {
            // Busca página do ranking geral
            pageResultados = resultadoService.listarRankingGeralPaginado(page, size);
        }

        // Adiciona ao modelo os dados necessários para a view
        model.addAttribute("resultados", pageResultados.getContent());   // lista da página atual
        model.addAttribute("currentPage", pageResultados.getNumber());   // página atual (0-based)
        model.addAttribute("totalPages", pageResultados.getTotalPages());
        model.addAttribute("totalItems", pageResultados.getTotalElements());
        model.addAttribute("pageSize", size);   // para manter o valor no seletor

        return "ranking";
    }
}