package br.com.projetodevida.dominio;

/** As 6 áreas de desenvolvimento do PDP do Ramo Pioneiro. */
public enum Area {
    FISICO("fisico", "Físico", "#b4552d"),
    INTELECTUAL("intelectual", "Intelectual", "#3b6ea5"),
    CARATER("carater", "Caráter", "#4a6741"),
    AFETIVO("afetivo", "Afetivo", "#c04a6b"),
    SOCIAL("social", "Social", "#c9912c"),
    ESPIRITUAL("espiritual", "Espiritual", "#6b5b95");

    /** Identificador usado no JSON de backup (compatível com a versão web). */
    private final String codigo;
    private final String nome;
    private final String cor;

    Area(String codigo, String nome, String cor) {
        this.codigo = codigo;
        this.nome = nome;
        this.cor = cor;
    }

    public String getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public String getCor() { return cor; }

    public static Area porCodigo(String codigo) {
        for (Area a : values()) {
            if (a.codigo.equals(codigo)) return a;
        }
        throw new IllegalArgumentException("Área desconhecida: " + codigo);
    }
}
