package br.com.projetodevida.servico;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.projetodevida.dominio.Area;
import br.com.projetodevida.dominio.Horizonte;
import br.com.projetodevida.dominio.Meta;
import br.com.projetodevida.dominio.PessoaApoio;
import br.com.projetodevida.dominio.RegraDeNegocioException;
import br.com.projetodevida.repositorio.MetaRepository;
import br.com.projetodevida.repositorio.PessoaApoioRepository;

/** Aba "Planejar": metas nas 6 áreas, linha do tempo e rede de apoio. */
@Service
@Transactional
public class MetaService {

    public static final List<String> PAPEIS_APOIO =
            List.of("Padrinho/Madrinha", "Par (amigo/colega)", "Mentor profissional", "Família");

    /** Um ano (ou período) da linha do tempo com as metas daquele horizonte. */
    public record GrupoLinhaDoTempo(String rotulo, List<Meta> metas) { }

    private final MetaRepository metaRepository;
    private final PessoaApoioRepository pessoaRepository;
    private final Clock relogio;

    public MetaService(MetaRepository metaRepository, PessoaApoioRepository pessoaRepository, Clock relogio) {
        this.metaRepository = metaRepository;
        this.pessoaRepository = pessoaRepository;
        this.relogio = relogio;
    }

    @Transactional(readOnly = true)
    public List<Meta> listar() {
        return metaRepository.findAllByOrderByIdAsc();
    }

    @Transactional(readOnly = true)
    public List<Meta> listarAbertas() {
        return listar().stream().filter(m -> !m.isConcluida()).toList();
    }

    public Meta criar(Area area, Horizonte horizonte, String titulo, String porque, String primeiroPasso, String cha) {
        if (Textos.vazio(titulo)) throw new RegraDeNegocioException("Escreva a meta.");
        if (area == null || horizonte == null) throw new RegraDeNegocioException("Escolha a área e o horizonte.");
        return metaRepository.save(new Meta(area, horizonte, Textos.limpar(titulo),
                Textos.limpar(porque), Textos.limpar(primeiroPasso), Textos.limpar(cha)));
    }

    public void alternarConcluida(Long id) {
        metaRepository.findById(id).ifPresent(Meta::alternarConcluida);
    }

    public void excluir(Long id) {
        metaRepository.deleteById(id);
    }

    /** Quantidade de metas por área (todas as áreas presentes, mesmo com 0). */
    @Transactional(readOnly = true)
    public Map<Area, Long> contagemPorArea() {
        Map<Area, Long> contagem = new EnumMap<>(Area.class);
        for (Area a : Area.values()) contagem.put(a, 0L);
        listar().forEach(m -> contagem.merge(m.getArea(), 1L, Long::sum));
        return contagem;
    }

    @Transactional(readOnly = true)
    public List<GrupoLinhaDoTempo> linhaDoTempo() {
        int ano = LocalDate.now(relogio).getYear();
        List<Meta> metas = listar();
        List<GrupoLinhaDoTempo> grupos = new ArrayList<>();
        for (Horizonte h : Horizonte.values()) {
            List<Meta> doHorizonte = metas.stream().filter(m -> m.getHorizonte() == h).toList();
            if (!doHorizonte.isEmpty()) grupos.add(new GrupoLinhaDoTempo(h.rotuloLinhaDoTempo(ano), doHorizonte));
        }
        return grupos;
    }

    @Transactional(readOnly = true)
    public List<PessoaApoio> redeDeApoio() {
        return pessoaRepository.findAllByOrderByIdAsc();
    }

    public void adicionarPessoa(String nome, String papel) {
        if (Textos.vazio(nome)) throw new RegraDeNegocioException("Escreva o nome da pessoa.");
        pessoaRepository.save(new PessoaApoio(Textos.limpar(nome), Textos.limpar(papel)));
    }

    public void removerPessoa(Long id) {
        pessoaRepository.deleteById(id);
    }
}
