package br.com.farmacia.usuarios.controller;

import br.com.farmacia.usuarios.dto.LoginRequest;
import br.com.farmacia.usuarios.dto.LoginResponse;
import br.com.farmacia.usuarios.model.Usuario;
import br.com.farmacia.usuarios.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService service;

    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @PostMapping
    public ResponseEntity<Usuario> criar(@RequestBody Usuario u) {
        return ResponseEntity.ok(service.salvar(u));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        try {

            String token = service.autenticar(
                    request.getLogin(),
                    request.getSenha()
            );

            return ResponseEntity.ok(new LoginResponse(token));

        } catch (Exception e) {

            return ResponseEntity.status(401).body(
                    Map.of(
                            "erro", "Usuário ou senha inválidos"
                    )
            );
        }
    }

    @GetMapping("/logs")
    public ResponseEntity<?> logs() {
        return ResponseEntity.ok(service.listarLogs());
    }
}
