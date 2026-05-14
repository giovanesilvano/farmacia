package br.com.farmacia.estoque.repository;
import br.com.farmacia.estoque.model.MovimentacaoEstoque;
import br.com.farmacia.shared.persistence.JsonFileHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public class MovimentacaoRepository {
    @Value("${dados.path}") private String path;
    private String arquivo() { return path + "movimentacoes.json"; }
    public List<MovimentacaoEstoque> findAll() { return JsonFileHandler.lerArquivo(arquivo(), MovimentacaoEstoque[].class); }
    public MovimentacaoEstoque save(MovimentacaoEstoque m) {
        List<MovimentacaoEstoque> lista = findAll();
        if (m.getId() == null) m.setId(lista.stream().mapToLong(i -> i.getId()).max().orElse(0L) + 1);
        lista.add(m);
        JsonFileHandler.escreverArquivo(arquivo(), lista);
        return m;
    }
}
