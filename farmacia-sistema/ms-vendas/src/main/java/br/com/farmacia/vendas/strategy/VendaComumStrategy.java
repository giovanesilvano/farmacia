package br.com.farmacia.vendas.strategy;

import br.com.farmacia.vendas.model.ItemVenda;
import br.com.farmacia.vendas.model.Venda;
import br.com.farmacia.shared.exception.EstoqueInsuficienteException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class VendaComumStrategy implements VendaStrategy {
    @Value("${estoque.service.url}")
    private String estoqueUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    private HttpHeaders getHeaders() {

        HttpServletRequest request =
                ((ServletRequestAttributes)
                        RequestContextHolder.currentRequestAttributes())
                        .getRequest();

        String authHeader =
                request.getHeader("Authorization");

        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", authHeader);

        return headers;
    }

    @Override
    public void validar(ItemVenda item) {

        HttpEntity<?> entity = new HttpEntity<>(getHeaders());

        Integer saldo = restTemplate.exchange(
                estoqueUrl + "/api/estoque/saldo/" + item.getProdutoId(),
                HttpMethod.GET,
                entity,
                Integer.class
        ).getBody();

        if (saldo == null || saldo < item.getQuantidade())
            throw new EstoqueInsuficienteException("Estoque insuficiente para: " + item.getNomeProduto());
    }

    @Override
    public void processar(ItemVenda item, Venda venda) {

        HttpEntity<?> entity = new HttpEntity<>(getHeaders());

        restTemplate.exchange(
                estoqueUrl + "/api/estoque/saida?produtoId=" + item.getProdutoId()
                        + "&quantidade=" + item.getQuantidade()
                        + "&motivo=Venda%20" + venda.getId(),
                HttpMethod.POST,
                entity,
                Void.class
        );
    }
}