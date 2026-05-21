package br.com.farmacia.vendas.repository;

import br.com.farmacia.vendas.model.Venda;
import br.com.farmacia.shared.persistence.JsonFileHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class VendaRepository {
    @Value("${dados.path}")
    private String path;

    private String arquivo() {
        return path + "vendas.json";
    }

    public List<Venda> findAll() {
        return JsonFileHandler.lerArquivo(arquivo(), Venda[].class);
    }

    public Optional<Venda> findById(Long id) {
        return findAll().stream().filter(v -> v.getId().equals(id)).findFirst();
    }

    public Venda save(Venda v) {
        List<Venda> lista = findAll();
        if (v.getId() == null) v.setId(lista.stream().mapToLong(x -> x.getId()).max().orElse(0L) + 1);
        lista.removeIf(x -> x.getId().equals(v.getId()));
        lista.add(v);
        JsonFileHandler.escreverArquivo(arquivo(), lista);
        return v;
    }
}
