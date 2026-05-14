package br.com.farmacia.estoque.controller;
import br.com.farmacia.estoque.model.Produto;
import br.com.farmacia.estoque.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {
    @Autowired private ProdutoRepository repo;
    @GetMapping public ResponseEntity<List<Produto>> listar() { return ResponseEntity.ok(repo.findAll()); }
    @GetMapping("/{id}") public ResponseEntity<Produto> buscar(@PathVariable Long id) { return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }
    @PostMapping public ResponseEntity<Produto> criar(@RequestBody Produto p) { return ResponseEntity.ok(repo.save(p)); }
    @PutMapping("/{id}") public ResponseEntity<Produto> atualizar(@PathVariable Long id, @RequestBody Produto p) { p.setId(id); return ResponseEntity.ok(repo.save(p)); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> deletar(@PathVariable Long id) { repo.deleteById(id); return ResponseEntity.ok().build(); }
}
