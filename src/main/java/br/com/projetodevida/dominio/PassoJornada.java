package br.com.projetodevida.dominio;

/** Os passos da jornada de reflexão (aba Descobrir). A ordem do enum é a ordem dos passos. */
public enum PassoJornada {
    HISTORIA("historia", "Minha história",
            "Quem é você? Escreva livremente: sua história até aqui, suas forças, suas limitações. Lembre: a narrativa que você conta sobre si pode ser reescrita — você é o autor.",
            "Minha história, minhas forças, minhas limitações..."),
    VALORES("valores", "Meus valores",
            "Quais valores você escolhe como bússola? Pense em 3 a 5 princípios dos quais não abre mão — e por quê.",
            "Ex.: honestidade, serviço, família, liberdade..."),
    MUNDO("mundo", "Meu mundo ao redor",
            "Análise prática do seu cenário: onde vive, com quem conta, quais oportunidades e obstáculos existem ao seu redor hoje?",
            "Contexto, pessoas, oportunidades, obstáculos..."),
    FUTURO("futuro", "O futuro desejado",
            "Daqui a 5, 10, até 20 anos, se tudo caminhar bem: onde você está? O que faz? Com quem? Escreva como se já estivesse vivendo esse futuro.",
            "Daqui a alguns anos, eu..."),
    ESCOLHAS("escolhas", "As escolhas",
            "Todas as possibilidades estão abertas, mas não dá para atingir todas — é preciso escolher. Do futuro que descreveu, o que é essencial? O que você aceita deixar para depois?",
            "O essencial para mim é... Abro mão, por ora, de..."),
    APOIO("apoio", "Minha rede de apoio",
            "Quem pode ser seu padrinho/madrinha — alguém maduro que faça boas perguntas sem decidir por você? Quais amigos ajudarão a manter o rumo? (Registre as pessoas na aba Planejar.)",
            "Pessoas com quem vou compartilhar este projeto...");

    private final String codigo;
    private final String titulo;
    private final String pergunta;
    private final String exemplo;

    PassoJornada(String codigo, String titulo, String pergunta, String exemplo) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.pergunta = pergunta;
        this.exemplo = exemplo;
    }

    public String getCodigo() { return codigo; }
    public String getTitulo() { return titulo; }
    public String getPergunta() { return pergunta; }
    public String getExemplo() { return exemplo; }

    public static PassoJornada porCodigo(String codigo) {
        for (PassoJornada p : values()) {
            if (p.codigo.equals(codigo)) return p;
        }
        return null;
    }
}
