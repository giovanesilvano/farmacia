package br.com.farmacia.estoque.model;
import java.time.LocalDate;
public class NotaFiscalEntrada {
    private Long id;
    private String numero;
    private LocalDate dataEmissao;
    private String fornecedor;
    private boolean divergencia;
    public NotaFiscalEntrada() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public LocalDate getDataEmissao() { return dataEmissao; }
    public void setDataEmissao(LocalDate dataEmissao) { this.dataEmissao = dataEmissao; }
    public String getFornecedor() { return fornecedor; }
    public void setFornecedor(String fornecedor) { this.fornecedor = fornecedor; }
    public boolean isDivergencia() { return divergencia; }
    public void setDivergencia(boolean divergencia) { this.divergencia = divergencia; }
}
