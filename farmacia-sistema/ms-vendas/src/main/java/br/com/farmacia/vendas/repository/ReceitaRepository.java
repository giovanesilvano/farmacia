package br.com.farmacia.vendas.repository;

import br.com.farmacia.vendas.model.Receita;
import br.com.farmacia.shared.persistence.JsonFileHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList; // Importado
import java.util.List;
import java.util.Optional;

@Repository
public class ReceitaRepository {
    @Value("${dados.path}")
    private String path;

    private String arquivo() {
        return path + "receitas.json";
    }

    public List<Receita> findAll() {
        return JsonFileHandler.lerArquivo(arquivo(), Receita[].class);
    }

    public Optional<Receita> findById(Long id) {
        return findAll().stream().filter(r -> r.getId().equals(id)).findFirst();
    }

    public Receita save(Receita r) {
        List<Receita> dadosAtuais = findAll();

        // PRINT 1: Quantos itens o Java conseguiu ler do arquivo antes de salvar o novo?
        System.out.println("👉 Itens lidos do arquivo JSON: " + (dadosAtuais != null ? dadosAtuais.size() : 0));

        List<Receita> lista = new ArrayList<>(dadosAtuais != null ? dadosAtuais : List.of());

        if (r.getId() == null) {
            r.setId(
                    lista.stream()
                            .mapToLong(x -> x.getId() == null ? 0L : x.getId())
                            .max()
                            .orElse(0L) + 1
            );
        }

        // PRINT 2: Qual ID o Java gerou para essa receita?
        System.out.println("👉 ID gerado para a nova receita: " + r.getId());

        lista.removeIf(x -> x.getId().equals(r.getId()));
        lista.add(r);

        JsonFileHandler.escreverArquivo(arquivo(), lista);

        return r;
    }
}