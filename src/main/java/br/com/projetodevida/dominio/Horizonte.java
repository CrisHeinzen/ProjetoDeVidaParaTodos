package br.com.projetodevida.dominio;

/** Horizonte de tempo de uma meta, usado na linha do tempo de 5 anos. */
public enum Horizonte {
    SEIS_MESES("6 meses", 0),
    UM_ANO("1 ano", 1),
    DOIS_ANOS("2 anos", 2),
    TRES_ANOS("3 anos", 3),
    CINCO_ANOS("5 anos", 5);

    private final String rotulo;
    private final int anosAFrente;

    Horizonte(String rotulo, int anosAFrente) {
        this.rotulo = rotulo;
        this.anosAFrente = anosAFrente;
    }

    public String getRotulo() { return rotulo; }
    public int getAnosAFrente() { return anosAFrente; }

    /** Texto exibido na linha do tempo, ex.: "2027" ou "2026 · em 6 meses". */
    public String rotuloLinhaDoTempo(int anoAtual) {
        if (this == SEIS_MESES) return anoAtual + " · em 6 meses";
        return String.valueOf(anoAtual + anosAFrente);
    }

    public static Horizonte porRotulo(String rotulo) {
        for (Horizonte h : values()) {
            if (h.rotulo.equals(rotulo)) return h;
        }
        throw new IllegalArgumentException("Horizonte desconhecido: " + rotulo);
    }
}
