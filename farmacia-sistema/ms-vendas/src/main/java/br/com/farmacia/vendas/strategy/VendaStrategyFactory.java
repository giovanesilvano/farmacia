package br.com.farmacia.vendas.strategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class VendaStrategyFactory {
    @Autowired private VendaComumStrategy comum;
    @Autowired private VendaControladaStrategy controlada;
    public VendaStrategy getStrategy(boolean controlado) { return controlado ? controlada : comum; }
}
