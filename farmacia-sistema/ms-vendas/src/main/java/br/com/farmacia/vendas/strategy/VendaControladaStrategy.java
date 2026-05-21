package br.com.farmacia.vendas.strategy;

import br.com.farmacia.vendas.model.ItemVenda;
import br.com.farmacia.vendas.model.Venda;
import br.com.farmacia.vendas.repository.ReceitaRepository;
import br.com.farmacia.shared.exception.ReceitaInvalidaException;
import br.com.farmacia.shared.exception.EstoqueInsuficienteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class VendaControladaStrategy implements VendaStrategy {
    @Autowired
    private ReceitaRepository receitaRepo;
    @Value("${estoque.service.url}")
    private String estoqueUrl;
    @Value("${sngpc.service.url}")
    private String sngpcUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void validar(ItemVenda item) {
        if (item.getReceitaId() == null)
            throw new ReceitaInvalidaException("Receita obrigatória para medicamento controlado");
        receitaRepo.findById(item.getReceitaId()).filter(r -> r.isValida() && !r.isVencida())
                .orElseThrow(() -> new ReceitaInvalidaException("Receita inválida ou vencida"));
        Integer saldo = restTemplate.getForObject(estoqueUrl + "/api/estoque/saldo/" + item.getProdutoId(), Integer.class);
        if (saldo == null || saldo < item.getQuantidade())
            throw new EstoqueInsuficienteException("Estoque insuficiente para: " + item.getNomeProduto());
    }

    @Override
    public void processar(ItemVenda item, Venda venda) {
        restTemplate.postForObject(estoqueUrl + "/api/estoque/saida?produtoId=" + item.getProdutoId() + "&quantidade=" + item.getQuantidade() + "&motivo=Venda%20Controlada%20" + venda.getId(), null, Void.class);
        Map<String, Object> registro = new HashMap<>();
        registro.put("produtoId", item.getProdutoId());
        registro.put("nomeProduto", item.getNomeProduto());
        registro.put("quantidade", item.getQuantidade());
        registro.put("tipo", "SAIDA");
        registro.put("vendaId", venda.getId());
        restTemplate.postForObject(sngpcUrl + "/api/sngpc/registrar", registro, Void.class);
    }
}
