package br.com.projetodevida.servico;

import java.time.LocalDate;
import java.util.function.Predicate;

/** Cálculo de sequências ("streaks") de dias. */
public final class Sequencias {

    private Sequencias() { }

    /**
     * Conta quantos dias seguidos, de trás para frente a partir de hoje, satisfazem a condição.
     * Se hoje ainda não satisfaz, começa a contar de ontem — o dia ainda não terminou,
     * então não "quebra" a sequência.
     */
    public static int contar(LocalDate hoje, Predicate<LocalDate> cumprido) {
        LocalDate dia = cumprido.test(hoje) ? hoje : hoje.minusDays(1);
        int n = 0;
        while (cumprido.test(dia)) {
            n++;
            dia = dia.minusDays(1);
        }
        return n;
    }
}
