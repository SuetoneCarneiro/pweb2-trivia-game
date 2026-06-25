package br.edu.ifpb.pweb2.psp.trivia.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.edu.ifpb.pweb2.psp.trivia.config.UploadProperties;
import jakarta.annotation.PostConstruct;

@Service
public class ImagemUploadService {

    private static final Logger log = LoggerFactory.getLogger(ImagemUploadService.class);

    private static final byte[] PNG_SIGNATURE = {
            (byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A
    };

    private final UploadProperties uploadProperties;

    public ImagemUploadService(UploadProperties uploadProperties) {
        this.uploadProperties = uploadProperties;
    }

    @PostConstruct
    public void initUploadDir() throws IOException {
        var dir = uploadProperties.getResolvedDir();
        Files.createDirectories(dir);
        log.info("Pasta de uploads criada/usada em: {}", dir);
        log.info("Exemplo de URL: http://localhost:8080{}teste.png",
                uploadProperties.getNormalizedUrlPrefix());
    }

    public String salvarPng(MultipartFile arquivo) throws IOException {
        // Leitura do conteúdo feita uma única vez. Valida/grava a partir dos mesmos bytes
        byte[] conteudo = lerEValidarPng(arquivo);

        String nomeArquivo = UUID.randomUUID() + ".png";
        Path destino = uploadProperties.getResolvedDir().resolve(nomeArquivo).normalize();

        if (!destino.startsWith(uploadProperties.getResolvedDir())) {
            throw new IllegalArgumentException("Nome de arquivo inválido.");
        }

        Files.write(destino, conteudo);

        return uploadProperties.getNormalizedUrlPrefix() + nomeArquivo;
    }

    public void excluirPorUrl(String urlImagem) throws IOException {
        if (urlImagem == null || urlImagem.isBlank()) {
            return;
        }

        String prefixo = uploadProperties.getNormalizedUrlPrefix();
        if (!urlImagem.startsWith(prefixo)) {
            return;
        }

        String nomeArquivo = urlImagem.substring(prefixo.length());
        if (nomeArquivo.contains("..") || nomeArquivo.contains("/") || nomeArquivo.contains("\\")) {
            throw new IllegalArgumentException("URL de imagem inválida.");
        }

        Path arquivo = uploadProperties.getResolvedDir().resolve(nomeArquivo).normalize();
        if (!arquivo.startsWith(uploadProperties.getResolvedDir())) {
            throw new IllegalArgumentException("URL de imagem inválida.");
        }

        Files.deleteIfExists(arquivo);
    }

    private byte[] lerEValidarPng(MultipartFile arquivo) throws IOException {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new IllegalArgumentException("Selecione um arquivo PNG.");
        }

        String nomeOriginal = arquivo.getOriginalFilename();
        if (nomeOriginal == null || !nomeOriginal.toLowerCase().endsWith(".png")) {
            throw new IllegalArgumentException("A extensão do arquivo deve ser .png.");
        }

        // A validação real é feita pela assinatura (magic bytes) do PNG
        byte[] conteudo = arquivo.getBytes();
        if (conteudo.length < PNG_SIGNATURE.length
                || !Arrays.equals(Arrays.copyOf(conteudo, PNG_SIGNATURE.length), PNG_SIGNATURE)) {
            throw new IllegalArgumentException("O arquivo enviado não é um PNG válido.");
        }

        return conteudo;
    }
}
