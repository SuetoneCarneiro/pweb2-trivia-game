package br.edu.ifpb.pweb2.psp.trivia.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class UploadExceptionHandler {

    // limite recuperado dinamicamente do application.properties.
    @Value("${spring.servlet.multipart.max-file-size}")
    private String tamanhoMaximo;

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String tratarImagemGrande(HttpServletRequest request, RedirectAttributes ra) {
        ra.addFlashAttribute("mensagem",
                "A imagem excede o tamanho máximo permitido (" + tamanhoMaximo + ").");

        // Volta ao formulário de origem e exibe a mensagem
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/admin/dashboard");
    }
}
