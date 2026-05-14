package br.com.farmacia.regulatorio.controller;
import br.com.farmacia.regulatorio.model.RegistroSNGPC;
import br.com.farmacia.regulatorio.service.SNGPCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/sngpc")
public class SNGPCController {
    @Autowired private SNGPCService service;
    @PostMapping("/registrar") public ResponseEntity<RegistroSNGPC> registrar(@RequestBody Map<String, Object> dados) { return ResponseEntity.ok(service.registrar(dados)); }
    @GetMapping public ResponseEntity<List<RegistroSNGPC>> listar() { return ResponseEntity.ok(service.listar()); }
}
