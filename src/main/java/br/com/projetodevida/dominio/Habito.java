package br.com.projetodevida.dominio;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/** Hábito diário. O histórico (RegistroHabito) é ligado pelo nome e sobrevive à remoção do hábito. */
@Entity
public class Habito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 200)
    private String nome;

    protected Habito() { }

    public Habito(String nome) { this.nome = nome; }

    public Long getId() { return id; }
    public String getNome() { return nome; }
}
