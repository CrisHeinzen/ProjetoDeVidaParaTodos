package br.com.projetodevida;

import java.time.Clock;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Fornece um {@link Clock} para toda a aplicação. Os serviços nunca chamam
 * LocalDate.now() direto: usam este relógio, o que permite testar com datas fixas.
 */
@Configuration
public class ConfiguracaoRelogio {

    @Bean
    public Clock relogio(@Value("${app.fuso-horario:America/Sao_Paulo}") String fuso) {
        return Clock.system(ZoneId.of(fuso));
    }
}
