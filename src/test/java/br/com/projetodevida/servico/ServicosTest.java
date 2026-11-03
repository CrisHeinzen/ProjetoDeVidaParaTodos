package br.com.projetodevida.servico;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import br.com.projetodevida.RelogioFixoConfig;
import br.com.projetodevida.backup.BackupService;
import br.com.projetodevida.dominio.Area;
import br.com.projetodevida.dominio.Horizonte;
import br.com.projetodevida.dominio.RegistroHabito;
import br.com.projetodevida.dominio.RegraDeNegocioException;
import br.com.projetodevida.repositorio.RegistroHabitoRepository;

@SpringBootTest
@Import(RelogioFixoConfig.class)
class ServicosTest {

    @Autowired SemanaService semanaService;
    @Autowired MetaService metaService;
    @Autowired RevisaoService revisaoService;
    @Autowired ConstanciaService constanciaService;
    @Autowired JornadaService jornadaService;
    @Autowired BackupService backupService;
    @Autowired RegistroHabitoRepository registroRepository;

    private final LocalDate hoje = LocalDate.of(2026, 11, 3);

    @BeforeEach
    void limpar() {
        backupService.apagarTudo();
    }

    @Test
    void top3AceitaNoMaximoTresItensPorSemana() {
        semanaService.adicionarTop3("a", null);
        semanaService.adicionarTop3("b", null);
        semanaService.adicionarTop3("c", null);
        assertThatThrownBy(() -> semanaService.adicionarTop3("d", null))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("Top 3 é top 3");
        assertThat(semanaService.top3DaSemana()).hasSize(3)
                .allMatch(i -> i.getSemana().equals("2026-S45"));
    }

    @Test
    void metaSemTituloEhRecusada() {
        assertThatThrownBy(() -> metaService.criar(Area.FISICO, Horizonte.UM_ANO, "  ", "", "", ""))
                .isInstanceOf(RegraDeNegocioException.class).hasMessage("Escreva a meta.");
    }

    @Test
    void linhaDoTempoAgrupaPorHorizonteEmOrdem() {
        metaService.criar(Area.FISICO, Horizonte.CINCO_ANOS, "Maratona", "", "", "");
        metaService.criar(Area.INTELECTUAL, Horizonte.SEIS_MESES, "Curso", "", "", "");
        assertThat(metaService.linhaDoTempo())
                .extracting(MetaService.GrupoLinhaDoTempo::rotulo)
                .containsExactly("2026 · em 6 meses", "2031");
        assertThat(metaService.contagemPorArea()).containsEntry(Area.FISICO, 1L).containsEntry(Area.SOCIAL, 0L);
    }

    @Test
    void alternarHabitoMarcaEDesmarcaHoje() {
        semanaService.adicionarHabito("Ler");
        semanaService.adicionarHabito("Ler"); // duplicado é ignorado
        assertThat(semanaService.habitos()).hasSize(1);
        Long id = semanaService.habitos().get(0).getId();

        semanaService.alternarHabitoHoje(id);
        assertThat(semanaService.cumpridosHoje()).containsExactly("Ler");
        semanaService.alternarHabitoHoje(id);
        assertThat(semanaService.cumpridosHoje()).isEmpty();
    }

    @Test
    void placarContaDiasSeguidosComTodosOsHabitos() {
        semanaService.adicionarHabito("Ler");
        semanaService.adicionarHabito("Correr");
        for (int i = 1; i <= 3; i++) {
            registroRepository.save(new RegistroHabito("Ler", hoje.minusDays(i)));
            registroRepository.save(new RegistroHabito("Correr", hoje.minusDays(i)));
        }
        registroRepository.save(new RegistroHabito("Ler", hoje.minusDays(4))); // só um dos dois: não conta

        assertThat(constanciaService.painel().diasSeguidos()).isEqualTo(3);
        assertThat(semanaService.sequencia("Ler")).isEqualTo(4);
        // 3 de novembro: dias 1, 2 (de 3) com hábito => 67%
        assertThat(constanciaService.painel().percentualHabitosMes()).isEqualTo(67);
    }

    @Test
    void vigiliaAgendaProximaParaDaquiTresMeses() {
        assertThatThrownBy(() -> revisaoService.registrarVigilia("", " ", null))
                .isInstanceOf(RegraDeNegocioException.class);
        revisaoService.registrarVigilia("mudei", "", "");
        RevisaoService.StatusVigilia status = revisaoService.statusVigilia();
        assertThat(status.data()).isEqualTo(LocalDate.of(2027, 2, 3));
        assertThat(status.dias()).isEqualTo(92);
        assertThat(constanciaService.painel().vigilias()).isEqualTo(1);
    }

    @Test
    void jornadaSalvaRespostaENavega() {
        jornadaService.salvarResposta(1, "honestidade");
        jornadaService.irParaPasso(99);
        assertThat(jornadaService.passoAtual()).isEqualTo(JornadaService.TOTAL_PASSOS);
        assertThat(jornadaService.resposta(br.com.projetodevida.dominio.PassoJornada.VALORES)).isEqualTo("honestidade");
    }
}
