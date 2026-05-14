package br.com.farmacia.regulatorio.model;
import java.time.LocalDateTime;
public class RegistroSNGPC {
    private Long id;
    private Long produtoId;
    private String nomeProduto;
    private TipoRegistroSNGPC tipo;
    private int quantidade;
    private LocalDateTime dataHora;
    private Long vendaId;
    public RegistroSNGPC() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }
    public String getNomeProduto() { return nomeProduto; }
    public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; }
    public TipoRegistroSNGPC getTipo() { return tipo; }
    public void setTipo(TipoRegistroSNGPC tipo) { this.tipo = tipo; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public Long getVendaId() { return vendaId; }
    public void setVendaId(Long vendaId) { this.vendaId = vendaId; }
}
