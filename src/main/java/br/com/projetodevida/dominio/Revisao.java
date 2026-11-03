package br.com.projetodevida.dominio;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/** Revisão semanal (leve) ou vigília (profunda, trimestral). */
@Entity
public class Revisao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoRevisao tipo;

    @Column(nullable = false)
    private Instant registradaEm;

    /** O que avançou (revisão semanal). */
    @Column(length = 5000)
    private String avancos;

    /** O que travou / o que mudou (semanal e vigília). */
    @Column(length = 5000)
    private String ajustes;

    /** Reflexão sobre as metas (vigília). */
    @Column(length = 5000)
    private String metas;

    /** Rumo para o próximo trimestre (vigília). */
    @Column(length = 5000)
    private String rumo;

    protected Revisao() { }

    public Revisao(TipoRevisao tipo, Instant registradaEm, String avancos, String ajustes, String metas, String rumo) {
        this.tipo = tipo;
        this.registradaEm = registradaEm;
        this.avancos = avancos;
        this.ajustes = ajustes;
        this.metas = metas;
        this.rumo = rumo;
    }

    public Long getId() { return id; }
    public TipoRevisao getTipo() { return tipo; }
    public Instant getRegistradaEm() { return registradaEm; }
    public String getAvancos() { return avancos; }
    public String getAjustes() { return ajustes; }
    public String getMetas() { return metas; }
    public String getRumo() { return rumo; }
}
