package br.com.farmacia.vendas.strategy;
import br.com.farmacia.vendas.model.ItemVenda;
import br.com.farmacia.vendas.model.Venda;
import br.com.farmacia.shared.exception.EstoqueInsuficienteException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
@Component
public class VendaComumStrategy implements VendaStrategy {
    @Value("${estoque.service.url}") private String estoqueUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    @Override
    public void validar(ItemVenda item) {
        Integer saldo = restTemplate.getForObject(estoqueUrl + "/api/estoque/saldo/" + item.getProdutoId(), Integer.class);
        if (saldo == null || saldo < item.getQuantidade()) throw new EstoqueInsuficienteException("Estoque insuficiente para: " + item.getNomeProduto());
    }
    @Override
    public void processar(ItemVenda item, Venda venda) {
        restTemplate.postForObject(estoqueUrl + "/api/estoque/saida?produtoId=" + item.getProdutoId() + "&quantidade=" + item.getQuantidade() + "&motivo=Venda%20" + venda.getId(), null, Void.class);
    }
}
