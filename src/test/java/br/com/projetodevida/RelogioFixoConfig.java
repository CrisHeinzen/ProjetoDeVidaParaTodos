package br.com.projetodevida;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/** Nos testes, "hoje" é sempre terça, 03/11/2026 (semana 2026-S45). */
@TestConfiguration
public class RelogioFixoConfig {

    public static final Instant AGORA = Instant.parse("2026-11-03T15:00:00Z");

    @Bean
    @Primary
    public Clock relogioFixo() {
        return Clock.fixed(AGORA, ZoneId.of("America/Sao_Paulo"));
    }
}
