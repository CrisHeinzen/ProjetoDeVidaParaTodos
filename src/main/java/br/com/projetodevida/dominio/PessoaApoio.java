package br.com.projetodevida.dominio;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/** Alguém da rede de apoio (padrinho/madrinha, par, mentor, família). */
@Entity
public class PessoaApoio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nome;

    @Column(length = 100)
    private String papel;

    protected PessoaApoio() { }

    public PessoaApoio(String nome, String papel) {
        this.nome = nome;
        this.papel = papel;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getPapel() { return papel; }
}
