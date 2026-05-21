package br.com.farmacia.vendas.controller;

import br.com.farmacia.vendas.model.Venda;
import br.com.farmacia.vendas.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {
    @Autowired
    private VendaService service;

    @PostMapping
    public ResponseEntity<Venda> finalizar(@RequestBody Venda venda) {
        return ResponseEntity.ok(service.finalizarVenda(venda));
    }

    @GetMapping
    public ResponseEntity<List<Venda>> listar() {
        return ResponseEntity.ok(service.listarVendas());
    }
}
