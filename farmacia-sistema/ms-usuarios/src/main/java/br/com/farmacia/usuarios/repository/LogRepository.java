package br.com.farmacia.usuarios.repository;

import br.com.farmacia.usuarios.model.LogOperacao;
import br.com.farmacia.shared.persistence.JsonFileHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LogRepository {
    @Value("${dados.path}")
    private String path;

    private String arquivo() {
        return path + "logs.json";
    }

    public List<LogOperacao> findAll() {
        return JsonFileHandler.lerArquivo(arquivo(), LogOperacao[].class);
    }

    public LogOperacao save(LogOperacao l) {
        List<LogOperacao> lista = findAll();
        if (l.getId() == null) l.setId(lista.stream().mapToLong(x -> x.getId()).max().orElse(0L) + 1);
        lista.add(l);
        JsonFileHandler.escreverArquivo(arquivo(), lista);
        return l;
    }
}
