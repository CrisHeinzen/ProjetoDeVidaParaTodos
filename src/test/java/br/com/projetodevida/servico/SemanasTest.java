package br.com.projetodevida.servico;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Test;

class SemanasTest {

    @Test
    void geraChaveNoFormatoDaVersaoWeb() {
        assertThat(Semanas.chave(LocalDate.of(2026, 11, 3))).isEqualTo("2026-S45");
    }

    @Test
    void usaAnoDaSemanaIsoNaViradaDeAno() {
        // 30/12/2024 já pertence à semana 1 de 2025; 01/01/2027 ainda é a semana 53 de 2026
        assertThat(Semanas.chave(LocalDate.of(2024, 12, 30))).isEqualTo("2025-S01");
        assertThat(Semanas.chave(LocalDate.of(2027, 1, 1))).isEqualTo("2026-S53");
    }

    @Test
    void sequenciaNaoQuebraSeHojeAindaNaoFoiCumprido() {
        LocalDate hoje = LocalDate.of(2026, 11, 3);
        Set<LocalDate> dias = Set.of(hoje.minusDays(1), hoje.minusDays(2), hoje.minusDays(4));
        assertThat(Sequencias.contar(hoje, dias::contains)).isEqualTo(2);
    }

    @Test
    void sequenciaContaHojeQuandoCumprido() {
        LocalDate hoje = LocalDate.of(2026, 11, 3);
        Set<LocalDate> dias = Set.of(hoje, hoje.minusDays(1));
        assertThat(Sequencias.contar(hoje, dias::contains)).isEqualTo(2);
        assertThat(Sequencias.contar(hoje, d -> false)).isZero();
    }
}
