package br.com.projetodevida.backup;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.projetodevida.dominio.Area;
import br.com.projetodevida.dominio.Configuracao;
import br.com.projetodevida.dominio.Habito;
import br.com.projetodevida.dominio.Horizonte;
import br.com.projetodevida.dominio.ItemEspaco;
import br.com.projetodevida.dominio.ItemTop3;
import br.com.projetodevida.dominio.Meta;
import br.com.projetodevida.dominio.NotaRoda;
import br.com.projetodevida.dominio.PassoJornada;
import br.com.projetodevida.dominio.PessoaApoio;
import br.com.projetodevida.dominio.RegistroHabito;
import br.com.projetodevida.dominio.RespostaJornada;
import br.com.projetodevida.dominio.Revisao;
import br.com.projetodevida.dominio.TipoEspaco;
import br.com.projetodevida.dominio.TipoRevisao;
import br.com.projetodevida.repositorio.ConfiguracaoRepository;
import br.com.projetodevida.repositorio.HabitoRepository;
import br.com.projetodevida.repositorio.ItemEspacoRepository;
import br.com.projetodevida.repositorio.ItemTop3Repository;
import br.com.projetodevida.repositorio.MetaRepository;
import br.com.projetodevida.repositorio.NotaRodaRepository;
import br.com.projetodevida.repositorio.PessoaApoioRepository;
import br.com.projetodevida.repositorio.RegistroHabitoRepository;
import br.com.projetodevida.repositorio.RespostaJornadaRepository;
import br.com.projetodevida.repositorio.RevisaoRepository;
import br.com.projetodevida.servico.JornadaService;

/** Exportação, importação e limpeza total dos dados. */
@Service
@Transactional
public class BackupService {

    private final RespostaJornadaRepository respostaRepository;
    private final NotaRodaRepository notaRepository;
    private final MetaRepository metaRepository;
    private final PessoaApoioRepository pessoaRepository;
    private final HabitoRepository habitoRepository;
    private final RegistroHabitoRepository registroRepository;
    private final ItemTop3Repository top3Repository;
    private final ItemEspacoRepository espacoRepository;
    private final RevisaoRepository revisaoRepository;
    private final ConfiguracaoRepository configuracaoRepository;
    private final JornadaService jornadaService;

    public BackupService(RespostaJornadaRepository respostaRepository, NotaRodaRepository notaRepository,
                         MetaRepository metaRepository, PessoaApoioRepository pessoaRepository,
                         HabitoRepository habitoRepository, RegistroHabitoRepository registroRepository,
                         ItemTop3Repository top3Repository, ItemEspacoRepository espacoRepository,
                         RevisaoRepository revisaoRepository, ConfiguracaoRepository configuracaoRepository,
                         JornadaService jornadaService) {
        this.respostaRepository = respostaRepository;
        this.notaRepository = notaRepository;
        this.metaRepository = metaRepository;
        this.pessoaRepository = pessoaRepository;
        this.habitoRepository = habitoRepository;
        this.registroRepository = registroRepository;
        this.top3Repository = top3Repository;
        this.espacoRepository = espacoRepository;
        this.revisaoRepository = revisaoRepository;
        this.configuracaoRepository = configuracaoRepository;
        this.jornadaService = jornadaService;
    }

    @Transactional(readOnly = true)
    public BackupJson exportar() {
        Map<String, String> jornada = new LinkedHashMap<>();
        respostaRepository.findAll().forEach(r -> jornada.put(r.getPasso(), r.getTexto()));

        Map<String, Integer> roda = new LinkedHashMap<>();
        notaRepository.findAll().forEach(n -> roda.put(n.getArea().getCodigo(), n.getNota()));

        List<BackupJson.Goal> metas = metaRepository.findAllByOrderByIdAsc().stream()
                .map(m -> new BackupJson.Goal(m.getId(), m.getArea().getCodigo(), m.getHorizonte().getRotulo(),
                        m.getTitulo(), m.getPorque(), m.getPrimeiroPasso(), m.getCha(), m.isConcluida()))
                .toList();

        List<String> habitos = habitoRepository.findAllByOrderByIdAsc().stream().map(Habito::getNome).toList();

        Map<String, Map<String, Boolean>> log = new TreeMap<>();
        registroRepository.findAll().forEach(r ->
                log.computeIfAbsent(r.getDia().toString(), d -> new LinkedHashMap<>()).put(r.getNomeHabito(), true));

        Map<String, List<BackupJson.Top3>> top3 = new TreeMap<>();
        top3Repository.findAll().forEach(t -> top3.computeIfAbsent(t.getSemana(), s -> new ArrayList<>())
                .add(new BackupJson.Top3(t.getTexto(), t.getMetaId() == null ? null : t.getMetaId().toString(),
                        t.isConcluido())));

        List<BackupJson.Space> espaco = espacoRepository.findAllByOrderByIdAsc().stream()
                .map(e -> new BackupJson.Space(e.getTexto(), e.getTipo().getCodigo())).toList();

        List<BackupJson.Support> apoio = pessoaRepository.findAllByOrderByIdAsc().stream()
                .map(p -> new BackupJson.Support(p.getNome(), p.getPapel())).toList();

        List<BackupJson.Review> revisoes = new ArrayList<>(revisaoRepository.findAllByOrderByRegistradaEmDesc().stream()
                .map(r -> new BackupJson.Review(r.getTipo().getCodigo(), r.getRegistradaEm().toString(),
                        r.getAvancos(), r.getAjustes(), r.getMetas(), r.getRumo()))
                .toList());
        Collections.reverse(revisoes); // mais antiga primeiro, como na versão web

        Configuracao c = jornadaService.configuracao();
        return new BackupJson(jornada, roda, metas, c.getPassoAtual(), habitos, log, top3, espaco, apoio,
                revisoes, c.getProximaVigilia() == null ? null : c.getProximaVigilia().toString());
    }

    /** Substitui TODOS os dados atuais pelos do backup. */
    public void importar(BackupJson b) {
        apagarTudo();

        if (b.journey() != null) {
            b.journey().forEach((passo, texto) -> {
                if (PassoJornada.porCodigo(passo) != null && texto != null) {
                    respostaRepository.save(new RespostaJornada(passo, texto));
                }
            });
        }
        if (b.wheel() != null) {
            b.wheel().forEach((area, nota) -> {
                if (nota != null) notaRepository.save(new NotaRoda(Area.porCodigo(area), nota));
            });
        }

        // ids antigos das metas -> ids novos, para religar os itens do top 3
        Map<String, Long> idsMetas = new HashMap<>();
        if (b.goals() != null) {
            for (BackupJson.Goal g : b.goals()) {
                Meta m = new Meta(Area.porCodigo(g.area()), Horizonte.porRotulo(g.horizon()), g.title(),
                        g.why(), g.step(), g.cha());
                m.setConcluida(g.done());
                m = metaRepository.save(m);
                if (g.id() != null) idsMetas.put(g.id().toString(), m.getId());
            }
        }

        if (b.habits() != null) {
            b.habits().stream().distinct().forEach(h -> habitoRepository.save(new Habito(h)));
        }
        if (b.habitLog() != null) {
            b.habitLog().forEach((dia, marcados) -> {
                LocalDate data = LocalDate.parse(dia);
                marcados.forEach((habito, feito) -> {
                    if (Boolean.TRUE.equals(feito)) registroRepository.save(new RegistroHabito(habito, data));
                });
            });
        }
        if (b.top3() != null) {
            b.top3().forEach((semana, itens) -> itens.forEach(t -> {
                Long metaId = t.goalId() == null ? null : idsMetas.get(t.goalId());
                ItemTop3 item = new ItemTop3(semana, t.text(), metaId);
                item.setConcluido(t.done());
                top3Repository.save(item);
            }));
        }
        if (b.space() != null) {
            b.space().forEach(s -> espacoRepository.save(new ItemEspaco(TipoEspaco.porCodigo(s.type()), s.text())));
        }
        if (b.support() != null) {
            b.support().forEach(p -> pessoaRepository.save(new PessoaApoio(p.name(), p.role())));
        }
        if (b.reviews() != null) {
            b.reviews().forEach(r -> revisaoRepository.save(new Revisao(TipoRevisao.porCodigo(r.type()),
                    Instant.parse(r.date()), r.wins(), r.changes(), r.goals(), r.next())));
        }

        Configuracao c = jornadaService.configuracao();
        c.setPassoAtual(b.step() == null ? 0 : Math.max(0, Math.min(b.step(), JornadaService.TOTAL_PASSOS)));
        c.setProximaVigilia(b.nextReview() == null || b.nextReview().isBlank() ? null : LocalDate.parse(b.nextReview()));
        configuracaoRepository.save(c);
    }

    public void apagarTudo() {
        respostaRepository.deleteAllInBatch();
        notaRepository.deleteAllInBatch();
        metaRepository.deleteAllInBatch();
        pessoaRepository.deleteAllInBatch();
        habitoRepository.deleteAllInBatch();
        registroRepository.deleteAllInBatch();
        top3Repository.deleteAllInBatch();
        espacoRepository.deleteAllInBatch();
        revisaoRepository.deleteAllInBatch();
        configuracaoRepository.deleteAllInBatch();
    }
}
