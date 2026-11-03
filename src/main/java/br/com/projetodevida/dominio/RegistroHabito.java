package br.com.projetodevida.dominio;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/** Marca que o hábito X foi cumprido no dia Y. Se não existe registro, não foi cumprido. */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"nomeHabito", "dia"}))
public class RegistroHabito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nomeHabito;

    @Column(nullable = false)
    private LocalDate dia;

    protected RegistroHabito() { }

    public RegistroHabito(String nomeHabito, LocalDate dia) {
        this.nomeHabito = nomeHabito;
        this.dia = dia;
    }

    public Long getId() { return id; }
    public String getNomeHabito() { return nomeHabito; }
    public LocalDate getDia() { return dia; }
}
