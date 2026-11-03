package br.com.projetodevida.servico;

import java.time.Clock;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.projetodevida.dominio.Habito;
import br.com.projetodevida.dominio.ItemEspaco;
import br.com.projetodevida.dominio.ItemTop3;
import br.com.projetodevida.dominio.RegistroHabito;
import br.com.projetodevida.dominio.RegraDeNegocioException;
import br.com.projetodevida.dominio.TipoEspaco;
import br.com.projetodevida.repositorio.HabitoRepository;
import br.com.projetodevida.repositorio.ItemEspacoRepository;
import br.com.projetodevida.repositorio.ItemTop3Repository;
import br.com.projetodevida.repositorio.RegistroHabitoRepository;

/** Aba "Semana": top 3 da semana, hábitos diários e "abrir espaço livre". */
@Service
@Transactional
public class SemanaService {

    public static final int LIMITE_TOP3 = 3;

    private final ItemTop3Repository top3Repository;
    private final HabitoRepository habitoRepository;
    private final RegistroHabitoRepository registroRepository;
    private final ItemEspacoRepository espacoRepository;
    private final Clock relogio;

    public SemanaService(ItemTop3Repository top3Repository, HabitoRepository habitoRepository,
                         RegistroHabitoRepository registroRepository, ItemEspacoRepository espacoRepository,
                         Clock relogio) {
        this.top3Repository = top3Repository;
        this.habitoRepository = habitoRepository;
        this.registroRepository = registroRepository;
        this.espacoRepository = espacoRepository;
        this.relogio = relogio;
    }

    public LocalDate hoje() {
        return LocalDate.now(relogio);
    }

    public String semanaAtual() {
        return Semanas.chave(hoje());
    }

    // ---------------- Top 3 ----------------

    @Transactional(readOnly = true)
    public List<ItemTop3> top3DaSemana() {
        return top3Repository.findBySemanaOrderByIdAsc(semanaAtual());
    }

    public void adicionarTop3(String texto, Long metaId) {
        if (Textos.vazio(texto)) throw new RegraDeNegocioException("Escreva a ação da semana.");
        String semana = semanaAtual();
        if (top3Repository.countBySemana(semana) >= LIMITE_TOP3) {
            throw new RegraDeNegocioException(
                    "Top 3 é top 3: conclua ou remova um item antes de adicionar outro. Foco é força!");
        }
        top3Repository.save(new ItemTop3(semana, Textos.limpar(texto), metaId));
    }

    public void alternarTop3(Long id) {
        top3Repository.findById(id).ifPresent(ItemTop3::alternarConcluido);
    }

    public void excluirTop3(Long id) {
        top3Repository.deleteById(id);
    }

    // ---------------- Hábitos ----------------

    @Transactional(readOnly = true)
    public List<Habito> habitos() {
        return habitoRepository.findAllByOrderByIdAsc();
    }

    public void adicionarHabito(String nome) {
        if (Textos.vazio(nome)) throw new RegraDeNegocioException("Escreva o nome do hábito.");
        String limpo = Textos.limpar(nome);
        if (!habitoRepository.existsByNome(limpo)) habitoRepository.save(new Habito(limpo));
    }

    /** Remove o hábito da lista. O histórico (registros) é mantido de propósito. */
    public void removerHabito(Long id) {
        habitoRepository.deleteById(id);
    }

    /** Marca/desmarca o hábito como cumprido hoje. */
    public void alternarHabitoHoje(Long id) {
        habitoRepository.findById(id).ifPresent(h -> {
            LocalDate hoje = hoje();
            registroRepository.findByNomeHabitoAndDia(h.getNome(), hoje)
                    .ifPresentOrElse(registroRepository::delete,
                            () -> registroRepository.save(new RegistroHabito(h.getNome(), hoje)));
        });
    }

    /** Nomes dos hábitos cumpridos hoje. */
    @Transactional(readOnly = true)
    public Set<String> cumpridosHoje() {
        LocalDate hoje = hoje();
        return registroRepository.findByDiaBetween(hoje, hoje).stream()
                .map(RegistroHabito::getNomeHabito).collect(Collectors.toSet());
    }

    /**
     * Sequência de dias seguidos em que o hábito foi cumprido. Se hoje ainda não foi
     * cumprido, a contagem começa de ontem (o dia ainda não acabou).
     */
    @Transactional(readOnly = true)
    public int sequencia(String nomeHabito) {
        Set<LocalDate> dias = registroRepository.findByNomeHabito(nomeHabito).stream()
                .map(RegistroHabito::getDia).collect(Collectors.toSet());
        return Sequencias.contar(hoje(), dias::contains);
    }

    // ---------------- Abrir espaço ----------------

    @Transactional(readOnly = true)
    public Map<TipoEspaco, List<ItemEspaco>> espacoPorTipo() {
        Map<TipoEspaco, List<ItemEspaco>> mapa = new EnumMap<>(TipoEspaco.class);
        List<ItemEspaco> todos = espacoRepository.findAllByOrderByIdAsc();
        for (TipoEspaco t : TipoEspaco.values()) {
            mapa.put(t, todos.stream().filter(i -> i.getTipo() == t).toList());
        }
        return mapa;
    }

    public void adicionarEspaco(String texto, TipoEspaco tipo) {
        if (Textos.vazio(texto)) throw new RegraDeNegocioException("Escreva o que quer registrar.");
        if (tipo == null) throw new RegraDeNegocioException("Escolha parar, delegar ou começar.");
        espacoRepository.save(new ItemEspaco(tipo, Textos.limpar(texto)));
    }

    public void removerEspaco(Long id) {
        espacoRepository.deleteById(id);
    }
}
