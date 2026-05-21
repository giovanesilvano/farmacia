package br.com.farmacia.vendas.strategy;

import br.com.farmacia.vendas.model.ItemVenda;
import br.com.farmacia.vendas.model.Venda;

public interface VendaStrategy {
    void validar(ItemVenda item);

    void processar(ItemVenda item, Venda venda);
}
