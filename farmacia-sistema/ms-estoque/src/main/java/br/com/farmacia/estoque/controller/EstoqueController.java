package br.com.farmacia.estoque.controller;
import br.com.farmacia.estoque.model.*;
import br.com.farmacia.estoque.service.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {
    @Autowired private EstoqueService service;
    @PostMapping("/entrada")
    public ResponseEntity<ItemEstoque> entrada(@RequestBody ItemEstoque item) { return ResponseEntity.ok(service.registrarEntrada(item)); }
    @PostMapping("/saida")
    public ResponseEntity<Void> saida(@RequestParam Long produtoId, @RequestParam int quantidade, @RequestParam String motivo) {
        service.registrarSaida(produtoId, quantidade, TipoMovimentacao.SAIDA_VENDA, motivo);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/saldo/{produtoId}")
    public ResponseEntity<Integer> saldo(@PathVariable Long produtoId) { return ResponseEntity.ok(service.consultarSaldo(produtoId)); }
    @GetMapping("/historico")
    public ResponseEntity<List<MovimentacaoEstoque>> historico() { return ResponseEntity.ok(service.consultarHistorico()); }
    @GetMapping("/alertas")
    public ResponseEntity<List<Produto>> alertas() { return ResponseEntity.ok(service.verificarAlertaMinimo()); }
}
