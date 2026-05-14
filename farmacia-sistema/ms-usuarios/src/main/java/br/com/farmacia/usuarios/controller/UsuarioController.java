package br.com.farmacia.usuarios.controller;
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
    @Autowired private UsuarioService service;
    @GetMapping public ResponseEntity<List<Usuario>> listar() { return ResponseEntity.ok(service.listar()); }
    @PostMapping public ResponseEntity<Usuario> criar(@RequestBody Usuario u) { return ResponseEntity.ok(service.salvar(u)); }
    @PostMapping("/login") public ResponseEntity<Usuario> login(@RequestBody Map<String, String> creds) { return ResponseEntity.ok(service.autenticar(creds.get("login"), creds.get("senha"))); }
    @GetMapping("/logs") public ResponseEntity<?> logs() { return ResponseEntity.ok(service.listarLogs()); }
}
