package br.com.projetodevida.dominio;

public enum TipoRevisao {
    SEMANA("semana"),
    VIGILIA("vigilia");

    private final String codigo;

    TipoRevisao(String codigo) { this.codigo = codigo; }

    public String getCodigo() { return codigo; }

    public static TipoRevisao porCodigo(String codigo) {
        for (TipoRevisao t : values()) {
            if (t.codigo.equals(codigo)) return t;
        }
        throw new IllegalArgumentException("Tipo de revisão desconhecido: " + codigo);
    }
}
