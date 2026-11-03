package br.com.projetodevida.backup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.projetodevida.RelogioFixoConfig;
import br.com.projetodevida.dominio.Area;
import br.com.projetodevida.dominio.Horizonte;
import br.com.projetodevida.dominio.PassoJornada;
import br.com.projetodevida.servico.JornadaService;
import br.com.projetodevida.servico.MetaService;
import br.com.projetodevida.servico.RevisaoService;
import br.com.projetodevida.servico.SemanaService;

@SpringBootTest
@Import(RelogioFixoConfig.class)
public class BackupServiceTest {

    /** Exemplo no formato exportado pela versão web (index.html, chave "pvpt2"). */
    public static final String BACKUP_VERSAO_WEB = """
            {
              "journey": {"historia": "Nasci em...", "valores": "honestidade, serviço"},
              "wheel": {"fisico": 7, "espiritual": 3},
              "goals": [
                {"id": 1730000000000, "area": "fisico", "horizon": "1 ano", "title": "Correr 10 km",
                 "why": "saúde", "step": "caminhar", "cha": "", "done": false},
                {"id": 1730000000001, "area": "social", "horizon": "5 anos", "title": "Voluntariado",
                 "why": "", "step": "", "cha": "", "done": true}
              ],
              "step": 6,
              "habits": ["Ler 10 páginas"],
              "habitLog": {"2026-11-02": {"Ler 10 páginas": true}, "2026-11-01": {"Ler 10 páginas": false}},
              "top3": {"2026-S45": [{"text": "Treinar 3x", "goalId": "1730000000000", "done": true}]},
              "space": [{"text": "rolagem infinita", "type": "parar"}],
              "support": [{"name": "Ana", "role": "Padrinho/Madrinha"}],
              "reviews": [{"type": "vigilia", "date": "2026-10-01T22:10:00.000Z", "changes": "mudei", "goals": "", "next": "foco"}],
              "nextReview": "2027-01-01",
              "campoDesconhecido": 123
            }
            """;

    @Autowired BackupService backupService;
    @Autowired ObjectMapper objectMapper;
    @Autowired JornadaService jornadaService;
    @Autowired MetaService metaService;
    @Autowired SemanaService semanaService;
    @Autowired RevisaoService revisaoService;

    @BeforeEach
    void limpar() {
        backupService.apagarTudo();
    }

    @Test
    void importaBackupDaVersaoWeb() throws Exception {
        backupService.importar(objectMapper.readValue(BACKUP_VERSAO_WEB, BackupJson.class));

        assertThat(jornadaService.resposta(PassoJornada.VALORES)).isEqualTo("honestidade, serviço");
        assertThat(jornadaService.passoAtual()).isEqualTo(6);
        assertThat(jornadaService.notasRoda()).containsEntry(Area.FISICO, 7).containsEntry(Area.SOCIAL, 5);
        assertThat(metaService.listar()).hasSize(2);
        assertThat(metaService.listar().get(0).getHorizonte()).isEqualTo(Horizonte.UM_ANO);
        assertThat(semanaService.sequencia("Ler 10 páginas")).isEqualTo(1);
        assertThat(revisaoService.statusVigilia().data()).isEqualTo(LocalDate.of(2027, 1, 1));

        // O item do top 3 continua ligado à meta, agora com o id novo
        var item = semanaService.top3DaSemana().get(0);
        assertThat(item.isConcluido()).isTrue();
        assertThat(item.getMetaId()).isEqualTo(metaService.listar().get(0).getId());
    }

    @Test
    void exportarEImportarDeNovoPreservaOsDados() throws Exception {
        backupService.importar(objectMapper.readValue(BACKUP_VERSAO_WEB, BackupJson.class));
        String exportado = objectMapper.writeValueAsString(backupService.exportar());

        backupService.importar(objectMapper.readValue(exportado, BackupJson.class));
        BackupJson denovo = backupService.exportar();

        assertThat(denovo.goals()).hasSize(2);
        assertThat(denovo.habitLog()).containsOnlyKeys("2026-11-02");
        assertThat(denovo.reviews()).hasSize(1);
        assertThat(denovo.reviews().get(0).date()).isEqualTo("2026-10-01T22:10:00Z");
        assertThat(denovo.top3().get("2026-S45").get(0).goalId())
                .isEqualTo(denovo.goals().get(0).id().toString());
    }

    @Test
    void arquivoInvalidoNaoApagaDadosAtuais() {
        metaService.criar(Area.FISICO, Horizonte.UM_ANO, "Meta atual", "", "", "");
        BackupJson ruim = new BackupJson(null, null,
                java.util.List.of(new BackupJson.Goal(1L, "area-que-nao-existe", "1 ano", "x", "", "", "", false)),
                null, null, null, null, null, null, null, null);

        assertThatThrownBy(() -> backupService.importar(ruim)).isInstanceOf(IllegalArgumentException.class);
        assertThat(metaService.listar()).extracting("titulo").containsExactly("Meta atual");
    }
}
