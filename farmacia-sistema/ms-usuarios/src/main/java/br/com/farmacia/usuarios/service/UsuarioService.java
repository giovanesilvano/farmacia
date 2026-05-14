package br.com.farmacia.usuarios.service;
import br.com.farmacia.usuarios.model.*;
import br.com.farmacia.usuarios.repository.*;
import br.com.farmacia.shared.exception.RecursoNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
@Service
public class UsuarioService {
    @Autowired private UsuarioRepository usuarioRepo;
    @Autowired private LogRepository logRepo;
    public Usuario autenticar(String login, String senha) {
        Usuario u = usuarioRepo.findByLogin(login).orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
        if (!u.getSenha().equals(senha)) throw new RecursoNaoEncontradoException("Senha incorreta");
        registrarLog(u.getId(), "LOGIN");
        return u;
    }
    public Usuario salvar(Usuario u) { return usuarioRepo.save(u); }
    public List<Usuario> listar() { return usuarioRepo.findAll(); }
    public void registrarLog(Long usuarioId, String acao) {
        LogOperacao log = new LogOperacao();
        log.setUsuarioId(usuarioId);
        log.setAcao(acao);
        log.setDataHora(LocalDateTime.now());
        logRepo.save(log);
    }
    public List<LogOperacao> listarLogs() { return logRepo.findAll(); }
}
