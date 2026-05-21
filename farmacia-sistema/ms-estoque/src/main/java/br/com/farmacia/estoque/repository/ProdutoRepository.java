package br.com.farmacia.estoque.repository;

import br.com.farmacia.estoque.model.Produto;
import br.com.farmacia.shared.persistence.JsonFileHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProdutoRepository {
    @Value("${dados.path}")
    private String path;

    private String arquivo() {
        return path + "produtos.json";
    }

    public List<Produto> findAll() {
        return JsonFileHandler.lerArquivo(arquivo(), Produto[].class);
    }

    public Optional<Produto> findById(Long id) {
        return findAll().stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public Produto save(Produto produto) {
        List<Produto> lista = findAll();
        if (produto.getId() == null) produto.setId(lista.stream().mapToLong(p -> p.getId()).max().orElse(0L) + 1);
        lista.removeIf(p -> p.getId().equals(produto.getId()));
        lista.add(produto);
        JsonFileHandler.escreverArquivo(arquivo(), lista);
        return produto;
    }

    public void deleteById(Long id) {
        List<Produto> lista = findAll();
        lista.removeIf(p -> p.getId().equals(id));
        JsonFileHandler.escreverArquivo(arquivo(), lista);
    }
}
