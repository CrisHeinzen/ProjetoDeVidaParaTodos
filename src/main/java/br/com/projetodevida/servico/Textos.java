package br.com.projetodevida.servico;

/** Pequenos utilitários de texto. */
final class Textos {

    private Textos() { }

    /** Remove espaços das pontas; null vira "". */
    static String limpar(String texto) {
        return texto == null ? "" : texto.strip();
    }

    static boolean vazio(String texto) {
        return limpar(texto).isEmpty();
    }
}
