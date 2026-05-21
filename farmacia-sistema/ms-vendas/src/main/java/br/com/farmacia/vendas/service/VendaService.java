package br.com.farmacia.vendas.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.farmacia.vendas.model.ItemVenda;
import br.com.farmacia.vendas.model.StatusVenda;
import br.com.farmacia.vendas.model.Venda;
import br.com.farmacia.vendas.repository.VendaRepository;
import br.com.farmacia.vendas.strategy.VendaStrategy;
import br.com.farmacia.vendas.strategy.VendaStrategyFactory;

@Service
public class VendaService {
    @Autowired
    private VendaRepository vendaRepo;
    @Autowired
    private VendaStrategyFactory strategyFactory;

    public Venda finalizarVenda(Venda venda) {
        venda.setDataHora(LocalDateTime.now());
        venda.setStatus(StatusVenda.ABERTA);
        Venda salva = vendaRepo.save(venda);
        BigDecimal total = BigDecimal.ZERO;
        for (ItemVenda item : salva.getItens()) {
            VendaStrategy strategy = strategyFactory.getStrategy(item.isControlado());
            strategy.validar(item);
            strategy.processar(item, salva);
            total = total.add(item.getSubtotal());
        }
        salva.setTotal(total);
        salva.setStatus(StatusVenda.FINALIZADA);
        return vendaRepo.save(salva);
    }

    public List<Venda> listarVendas() {
        return vendaRepo.findAll();
    }
}
