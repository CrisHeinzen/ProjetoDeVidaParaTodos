package br.com.projetodevida.servico;

import java.time.Clock;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.projetodevida.dominio.Habito;
import br.com.projetodevida.dominio.ItemTop3;
import br.com.projetodevida.dominio.Meta;
import br.com.projetodevida.dominio.RegistroHabito;
import br.com.projetodevida.dominio.TipoRevisao;
import br.com.projetodevida.repositorio.HabitoRepository;
import br.com.projetodevida.repositorio.ItemTop3Repository;
import br.com.projetodevida.repositorio.MetaRepository;
import br.com.projetodevida.repositorio.RegistroHabitoRepository;
import br.com.projetodevida.repositorio.RevisaoRepository;

/** Aba "Constância": placar da disciplina e indicadores do mês. */
@Service
@Transactional(readOnly = true)
public class ConstanciaService {

    static final List<String> MENSAGENS_PLATO = List.of(
            "Constância vale mais que intensidade. Apareça hoje.",
            "O platô não é falta de progresso — é onde o progresso se consolida.",
            "Pequenas repetições diárias constroem a vida épica que você desenhou.",
            "Revisar não é recomeçar do zero: é ajustar o leme.");

    public record Painel(int diasSeguidos, long semanasRevisadas, long vigilias,
                         long metasConcluidas, long totalMetas, String mensagemPlato,
                         int percentualHabitosMes, int percentualTop3Semana) { }

    private final HabitoRepository habitoRepository;
    private final RegistroHabitoRepository registroRepository;
    private final RevisaoRepository revisaoRepository;
    private final MetaRepository metaRepository;
    private final ItemTop3Repository top3Repository;
    private final Clock relogio;

    public ConstanciaService(HabitoRepository habitoRepository, RegistroHabitoRepository registroRepository,
                             RevisaoRepository revisaoRepository, MetaRepository metaRepository,
                             ItemTop3Repository top3Repository, Clock relogio) {
        this.habitoRepository = habitoRepository;
        this.registroRepository = registroRepository;
        this.revisaoRepository = revisaoRepository;
        this.metaRepository = metaRepository;
        this.top3Repository = top3Repository;
        this.relogio = relogio;
    }

    public Painel painel() {
        LocalDate hoje = LocalDate.now(relogio);
        Set<String> habitos = new HashSet<>();
        for (Habito h : habitoRepository.findAll()) habitos.add(h.getNome());

        // dia -> nomes dos hábitos cumpridos naquele dia
        Map<LocalDate, Set<String>> cumpridos = new HashMap<>();
        for (RegistroHabito r : registroRepository.findAll()) {
            cumpridos.computeIfAbsent(r.getDia(), d -> new HashSet<>()).add(r.getNomeHabito());
        }

        // Dias seguidos com TODOS os hábitos atuais cumpridos
        int diasSeguidos = Sequencias.contar(hoje, dia ->
                !habitos.isEmpty() && cumpridos.getOrDefault(dia, Set.of()).containsAll(habitos));

        // % de dias do mês (até hoje) com pelo menos um hábito cumprido
        int diasComHabito = 0;
        for (int d = 1; d <= hoje.getDayOfMonth(); d++) {
            Set<String> doDia = cumpridos.getOrDefault(hoje.withDayOfMonth(d), Set.of());
            if (habitos.stream().anyMatch(doDia::contains)) diasComHabito++;
        }
        int pctHabitos = Math.round(diasComHabito * 100f / hoje.getDayOfMonth());

        List<ItemTop3> top3 = top3Repository.findBySemanaOrderByIdAsc(Semanas.chave(hoje));
        long top3Feitos = top3.stream().filter(ItemTop3::isConcluido).count();
        int pctTop3 = top3.isEmpty() ? 0 : Math.round(top3Feitos * 100f / top3.size());

        List<Meta> metas = metaRepository.findAll();
        long metasConcluidas = metas.stream().filter(Meta::isConcluida).count();

        String mensagem = MENSAGENS_PLATO.get(hoje.getDayOfMonth() % MENSAGENS_PLATO.size());

        return new Painel(diasSeguidos,
                revisaoRepository.countByTipo(TipoRevisao.SEMANA),
                revisaoRepository.countByTipo(TipoRevisao.VIGILIA),
                metasConcluidas, metas.size(), mensagem, pctHabitos, pctTop3);
    }
}
