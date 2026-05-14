package br.com.farmacia.estoque.repository;
import br.com.farmacia.estoque.model.ItemEstoque;
import br.com.farmacia.shared.persistence.JsonFileHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Repository
public class ItemEstoqueRepository {
    @Value("${dados.path}") private String path;
    private String arquivo() { return path + "estoque.json"; }
    public List<ItemEstoque> findAll() { return JsonFileHandler.lerArquivo(arquivo(), ItemEstoque[].class); }
    public List<ItemEstoque> findByProdutoId(Long produtoId) { return findAll().stream().filter(i -> i.getProdutoId().equals(produtoId)).collect(Collectors.toList()); }
    public Optional<ItemEstoque> findById(Long id) { return findAll().stream().filter(i -> i.getId().equals(id)).findFirst(); }
    public ItemEstoque save(ItemEstoque item) {
        List<ItemEstoque> lista = findAll();
        if (item.getId() == null) item.setId(lista.stream().mapToLong(i -> i.getId()).max().orElse(0L) + 1);
        lista.removeIf(i -> i.getId().equals(item.getId()));
        lista.add(item);
        JsonFileHandler.escreverArquivo(arquivo(), lista);
        return item;
    }
}
