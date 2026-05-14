package br.com.farmacia.usuarios.repository;
import br.com.farmacia.usuarios.model.Usuario;
import br.com.farmacia.shared.persistence.JsonFileHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public class UsuarioRepository {
    @Value("${dados.path}") private String path;
    private String arquivo() { return path + "usuarios.json"; }
    public List<Usuario> findAll() { return JsonFileHandler.lerArquivo(arquivo(), Usuario[].class); }
    public Optional<Usuario> findByLogin(String login) { return findAll().stream().filter(u -> u.getLogin().equals(login)).findFirst(); }
    public Usuario save(Usuario u) {
        List<Usuario> lista = findAll();
        if (u.getId() == null) u.setId(lista.stream().mapToLong(x -> x.getId()).max().orElse(0L) + 1);
        lista.removeIf(x -> x.getId().equals(u.getId()));
        lista.add(u);
        JsonFileHandler.escreverArquivo(arquivo(), lista);
        return u;
    }
}
