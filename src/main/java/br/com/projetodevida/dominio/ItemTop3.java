package br.com.projetodevida.dominio;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/** Uma das (no máximo) 3 ações escolhidas para a semana. */
@Entity
public class ItemTop3 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Semana ISO no formato "2026-S45". */
    @Column(nullable = false, length = 10)
    private String semana;

    @Column(nullable = false, length = 500)
    private String texto;

    /** Meta ligada (opcional). Guardamos só o id: se a meta for excluída, o item continua. */
    private Long metaId;

    private boolean concluido;

    protected ItemTop3() { }

    public ItemTop3(String semana, String texto, Long metaId) {
        this.semana = semana;
        this.texto = texto;
        this.metaId = metaId;
    }

    public void alternarConcluido() { this.concluido = !this.concluido; }

    public Long getId() { return id; }
    public String getSemana() { return semana; }
    public String getTexto() { return texto; }
    public Long getMetaId() { return metaId; }
    public boolean isConcluido() { return concluido; }
    public void setConcluido(boolean concluido) { this.concluido = concluido; }
}
