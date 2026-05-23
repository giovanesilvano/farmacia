package br.com.farmacia.usuarios.service;

import br.com.farmacia.shared.exception.RecursoNaoEncontradoException;
import br.com.farmacia.usuarios.model.LogOperacao;
import br.com.farmacia.usuarios.model.Usuario;
import br.com.farmacia.usuarios.repository.LogRepository;
import br.com.farmacia.usuarios.repository.UsuarioRepository;
import br.com.farmacia.usuarios.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private LogRepository logRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public String autenticar(String login, String senha) {

        Usuario u = usuarioRepo
                .findByLogin(login)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Usuário não encontrado"));

        if (!passwordEncoder.matches(senha, u.getSenha())) {
            throw new RecursoNaoEncontradoException("Senha incorreta");
        }

        registrarLog(u.getId(), "LOGIN");

        return jwtService.gerarToken(u);
    }

    public Usuario salvar(Usuario u) {

        u.setSenha(passwordEncoder.encode(u.getSenha()));

        return usuarioRepo.save(u);
    }

    public List<Usuario> listar() {
        return usuarioRepo.findAll();
    }

    public void registrarLog(Long usuarioId, String acao) {

        LogOperacao log = new LogOperacao();

        log.setUsuarioId(usuarioId);
        log.setAcao(acao);
        log.setDataHora(LocalDateTime.now());

        logRepo.save(log);
    }

    public List<LogOperacao> listarLogs() {
        return logRepo.findAll();
    }
}