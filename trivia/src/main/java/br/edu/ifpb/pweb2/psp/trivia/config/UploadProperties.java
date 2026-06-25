package br.edu.ifpb.pweb2.psp.trivia.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "trivia.upload")
public class UploadProperties {

    private String dir = "./uploads/perguntas";
    private String urlPrefix = "/uploads/perguntas/";

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }

    public Path getResolvedDir() {
        return Paths.get(dir).toAbsolutePath().normalize();
    }

    public String getNormalizedUrlPrefix() {
        if (urlPrefix.endsWith("/")) {
            return urlPrefix;
        }
        return urlPrefix + "/";
    }

    public String getFileLocationUri() {
        String path = getResolvedDir().toString().replace('\\', '/');
        if (!path.endsWith("/")) {
            path += "/";
        }
        return "file:///" + path;
    }
}
