package br.com.projetodevida.dominio;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/** Texto livre escrito pelo usuário em um passo da jornada. */
@Entity
public class RespostaJornada {

    /** Código do passo (ex.: "valores"). */
    @Id
    private String passo;

    @Column(length = 20000)
    private String texto;

    protected RespostaJornada() { }

    public RespostaJornada(String passo, String texto) {
        this.passo = passo;
        this.texto = texto;
    }

    public String getPasso() { return passo; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
}
