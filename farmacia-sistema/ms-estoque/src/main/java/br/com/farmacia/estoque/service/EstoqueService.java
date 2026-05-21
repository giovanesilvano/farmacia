package br.com.farmacia.estoque.service;

import br.com.farmacia.estoque.model.*;
import br.com.farmacia.estoque.repository.*;
import br.com.farmacia.shared.exception.EstoqueInsuficienteException;
import br.com.farmacia.shared.exception.RecursoNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EstoqueService {
    @Autowired
    private ProdutoRepository produtoRepo;
    @Autowired
    private ItemEstoqueRepository itemRepo;
    @Autowired
    private MovimentacaoRepository movRepo;

    public ItemEstoque registrarEntrada(ItemEstoque item) {
        produtoRepo.findById(item.getProdutoId()).orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado"));
        ItemEstoque salvo = itemRepo.save(item);
        MovimentacaoEstoque mov = new MovimentacaoEstoque();
        mov.setProdutoId(item.getProdutoId());
        mov.setTipo(TipoMovimentacao.ENTRADA);
        mov.setQuantidade(item.getQuantidade());
        mov.setDataHora(LocalDateTime.now());
        mov.setMotivo("Entrada de material");
        movRepo.save(mov);
        return salvo;
    }

    public void registrarSaida(Long produtoId, int quantidade, TipoMovimentacao tipo, String motivo) {
        List<ItemEstoque> itens = itemRepo.findByProdutoId(produtoId);
        int total = itens.stream().mapToInt(ItemEstoque::getQuantidade).sum();
        if (total < quantidade)
            throw new EstoqueInsuficienteException("Estoque insuficiente para o produto " + produtoId);
        int restante = quantidade;
        for (ItemEstoque item : itens) {
            if (restante <= 0) break;
            if (item.getQuantidade() <= restante) {
                restante -= item.getQuantidade();
                item.setQuantidade(0);
            } else {
                item.setQuantidade(item.getQuantidade() - restante);
                restante = 0;
            }
            itemRepo.save(item);
        }
        MovimentacaoEstoque mov = new MovimentacaoEstoque();
        mov.setProdutoId(produtoId);
        mov.setTipo(tipo);
        mov.setQuantidade(quantidade);
        mov.setDataHora(LocalDateTime.now());
        mov.setMotivo(motivo);
        movRepo.save(mov);
    }

    public int consultarSaldo(Long produtoId) {
        return itemRepo.findByProdutoId(produtoId).stream().mapToInt(ItemEstoque::getQuantidade).sum();
    }

    public List<MovimentacaoEstoque> consultarHistorico() {
        return movRepo.findAll();
    }

    public List<Produto> verificarAlertaMinimo() {
        return produtoRepo.findAll().stream()
                .filter(p -> consultarSaldo(p.getId()) <= p.getEstoqueMinimo())
                .collect(java.util.stream.Collectors.toList());
    }
}
