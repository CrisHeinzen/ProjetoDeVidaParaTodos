package br.com.projetodevida.servico;

import java.time.LocalDate;
import java.time.temporal.IsoFields;

/** Utilitário para a chave da semana ISO-8601, no formato usado desde a versão web: "2026-S45". */
public final class Semanas {

    private Semanas() { }

    public static String chave(LocalDate dia) {
        int ano = dia.get(IsoFields.WEEK_BASED_YEAR);
        int semana = dia.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        return String.format("%d-S%02d", ano, semana);
    }
}
