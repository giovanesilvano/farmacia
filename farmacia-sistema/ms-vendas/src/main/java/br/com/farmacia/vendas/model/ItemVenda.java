package br.com.farmacia.vendas.model;
import java.math.BigDecimal;
public class ItemVenda {
    private Long produtoId;
    private String nomeProduto;
    private boolean controlado;
    private int quantidade;
    private BigDecimal precoUnitario;
    private Long receitaId;
    public ItemVenda() {}
    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }
    public String getNomeProduto() { return nomeProduto; }
    public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; }
    public boolean isControlado() { return controlado; }
    public void setControlado(boolean controlado) { this.controlado = controlado; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }
    public Long getReceitaId() { return receitaId; }
    public void setReceitaId(Long receitaId) { this.receitaId = receitaId; }
    public BigDecimal getSubtotal() { return precoUnitario.multiply(BigDecimal.valueOf(quantidade)); }
}
