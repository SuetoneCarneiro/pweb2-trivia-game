package br.edu.ifpb.pweb2.psp.trivia.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import br.edu.ifpb.pweb2.psp.trivia.entities.Participante;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Após autenticar, guarda o participante na sessão e redireciona conforme o perfil
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        UsuarioDetails usuario = (UsuarioDetails) authentication.getPrincipal();
        Participante participante = usuario.getParticipante();
        request.getSession().setAttribute("participanteLogado", participante);

        if (participante.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        } else {
            response.sendRedirect(request.getContextPath() + "/lobby");
        }
    }
}
