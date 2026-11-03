package br.com.projetodevida.servico;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.projetodevida.dominio.Configuracao;
import br.com.projetodevida.dominio.RegraDeNegocioException;
import br.com.projetodevida.dominio.Revisao;
import br.com.projetodevida.dominio.TipoRevisao;
import br.com.projetodevida.repositorio.ConfiguracaoRepository;
import br.com.projetodevida.repositorio.RevisaoRepository;

/** Aba "Revisar": revisão semanal, vigília trimestral e histórico. */
@Service
@Transactional
public class RevisaoService {

    public static final int MESES_ENTRE_VIGILIAS = 3;

    /** Situação da próxima vigília. {@code dias} &lt; 0 = atrasada; 0 = hoje; null = não agendada. */
    public record StatusVigilia(LocalDate data, Long dias) {
        public boolean agendada() { return data != null; }
    }

    private final RevisaoRepository revisaoRepository;
    private final ConfiguracaoRepository configuracaoRepository;
    private final JornadaService jornadaService;
    private final Clock relogio;

    public RevisaoService(RevisaoRepository revisaoRepository, ConfiguracaoRepository configuracaoRepository,
                          JornadaService jornadaService, Clock relogio) {
        this.revisaoRepository = revisaoRepository;
        this.configuracaoRepository = configuracaoRepository;
        this.jornadaService = jornadaService;
        this.relogio = relogio;
    }

    public StatusVigilia statusVigilia() {
        LocalDate data = jornadaService.configuracao().getProximaVigilia();
        if (data == null) return new StatusVigilia(null, null);
        return new StatusVigilia(data, ChronoUnit.DAYS.between(LocalDate.now(relogio), data));
    }

    public void agendarVigilia(LocalDate data) {
        if (data == null) throw new RegraDeNegocioException("Escolha uma data.");
        Configuracao c = jornadaService.configuracao();
        c.setProximaVigilia(data);
        configuracaoRepository.save(c);
    }

    public void fecharSemana(String avancos, String ajustes) {
        if (Textos.vazio(avancos) && Textos.vazio(ajustes)) {
            throw new RegraDeNegocioException("Escreva pelo menos um campo.");
        }
        revisaoRepository.save(new Revisao(TipoRevisao.SEMANA, Instant.now(relogio),
                Textos.limpar(avancos), Textos.limpar(ajustes), "", ""));
    }

    /** Registra a vigília e sugere a próxima para daqui a 3 meses. */
    public void registrarVigilia(String mudancas, String metas, String rumo) {
        if (Textos.vazio(mudancas) && Textos.vazio(metas) && Textos.vazio(rumo)) {
            throw new RegraDeNegocioException("Escreva pelo menos um campo.");
        }
        revisaoRepository.save(new Revisao(TipoRevisao.VIGILIA, Instant.now(relogio),
                "", Textos.limpar(mudancas), Textos.limpar(metas), Textos.limpar(rumo)));
        agendarVigilia(LocalDate.now(relogio).plusMonths(MESES_ENTRE_VIGILIAS));
    }

    @Transactional(readOnly = true)
    public List<Revisao> historico() {
        return revisaoRepository.findAllByOrderByRegistradaEmDesc();
    }
}
