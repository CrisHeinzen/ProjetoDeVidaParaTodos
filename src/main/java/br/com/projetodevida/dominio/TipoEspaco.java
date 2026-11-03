package br.com.projetodevida.dominio;

/** Categorias do "Abrir espaço livre" (Método Cronos). */
public enum TipoEspaco {
    PARAR("parar", "⛔ Parar"),
    DELEGAR("delegar", "🤝 Delegar"),
    COMECAR("comecar", "🌱 Começar");

    private final String codigo;
    private final String titulo;

    TipoEspaco(String codigo, String titulo) {
        this.codigo = codigo;
        this.titulo = titulo;
    }

    public String getCodigo() { return codigo; }
    public String getTitulo() { return titulo; }

    public static TipoEspaco porCodigo(String codigo) {
        for (TipoEspaco t : values()) {
            if (t.codigo.equals(codigo)) return t;
        }
        throw new IllegalArgumentException("Tipo desconhecido: " + codigo);
    }
}
