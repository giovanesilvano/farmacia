package br.com.farmacia.regulatorio.repository;
import br.com.farmacia.regulatorio.model.RegistroSNGPC;
import br.com.farmacia.shared.persistence.JsonFileHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public class SNGPCRepository {
    @Value("${dados.path}") private String path;
    private String arquivo() { return path + "sngpc.json"; }
    public List<RegistroSNGPC> findAll() { return JsonFileHandler.lerArquivo(arquivo(), RegistroSNGPC[].class); }
    public RegistroSNGPC save(RegistroSNGPC r) {
        List<RegistroSNGPC> lista = findAll();
        if (r.getId() == null) r.setId(lista.stream().mapToLong(x -> x.getId()).max().orElse(0L) + 1);
        lista.add(r);
        JsonFileHandler.escreverArquivo(arquivo(), lista);
        return r;
    }
}
