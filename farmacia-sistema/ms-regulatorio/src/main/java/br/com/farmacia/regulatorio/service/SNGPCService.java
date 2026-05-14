package br.com.farmacia.regulatorio.service;
import br.com.farmacia.regulatorio.model.RegistroSNGPC;
import br.com.farmacia.regulatorio.model.TipoRegistroSNGPC;
import br.com.farmacia.regulatorio.repository.SNGPCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
@Service
public class SNGPCService {
    @Autowired private SNGPCRepository repo;
    public RegistroSNGPC registrar(Map<String, Object> dados) {
        RegistroSNGPC r = new RegistroSNGPC();
        r.setProdutoId(Long.valueOf(dados.get("produtoId").toString()));
        r.setNomeProduto(dados.get("nomeProduto").toString());
        r.setQuantidade(Integer.parseInt(dados.get("quantidade").toString()));
        r.setTipo(TipoRegistroSNGPC.valueOf(dados.get("tipo").toString()));
        r.setDataHora(LocalDateTime.now());
        if (dados.containsKey("vendaId")) r.setVendaId(Long.valueOf(dados.get("vendaId").toString()));
        return repo.save(r);
    }
    public List<RegistroSNGPC> listar() { return repo.findAll(); }
}
