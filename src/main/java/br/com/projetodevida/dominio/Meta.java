package br.com.projetodevida.dominio;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Meta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Area area;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Horizonte horizonte;

    @Column(nullable = false, length = 500)
    private String titulo;

    /** Por que ela importa (liga ao valor/visão). */
    @Column(length = 2000)
    private String porque;

    @Column(length = 2000)
    private String primeiroPasso;

    /** Conhecimento · Habilidade · Atitude a desenvolver (opcional). */
    @Column(length = 2000)
    private String cha;

    private boolean concluida;

    protected Meta() { }

    public Meta(Area area, Horizonte horizonte, String titulo, String porque, String primeiroPasso, String cha) {
        this.area = area;
        this.horizonte = horizonte;
        this.titulo = titulo;
        this.porque = porque;
        this.primeiroPasso = primeiroPasso;
        this.cha = cha;
    }

    public void alternarConcluida() { this.concluida = !this.concluida; }

    public Long getId() { return id; }
    public Area getArea() { return area; }
    public Horizonte getHorizonte() { return horizonte; }
    public String getTitulo() { return titulo; }
    public String getPorque() { return porque; }
    public String getPrimeiroPasso() { return primeiroPasso; }
    public String getCha() { return cha; }
    public boolean isConcluida() { return concluida; }
    public void setConcluida(boolean concluida) { this.concluida = concluida; }
}
