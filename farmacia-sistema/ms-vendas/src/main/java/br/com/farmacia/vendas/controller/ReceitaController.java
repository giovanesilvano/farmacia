package br.com.farmacia.vendas.controller;
import br.com.farmacia.vendas.model.Receita;
import br.com.farmacia.vendas.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/receitas")
public class ReceitaController {
    @Autowired private ReceitaRepository repo;
    @GetMapping public ResponseEntity<List<Receita>> listar() { return ResponseEntity.ok(repo.findAll()); }
    @PostMapping public ResponseEntity<Receita> criar(@RequestBody Receita r) { return ResponseEntity.ok(repo.save(r)); }
}
