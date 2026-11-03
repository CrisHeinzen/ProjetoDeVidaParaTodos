package br.com.projetodevida.dominio;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/** Registro único (id = 1) com o estado geral do app. */
@Entity
public class Configuracao {

    public static final long ID_UNICO = 1L;

    @Id
    private Long id = ID_UNICO;

    /** Índice do passo atual da jornada. Igual ao total de passos = jornada concluída. */
    private int passoAtual;

    private LocalDate proximaVigilia;

    public Long getId() { return id; }
    public int getPassoAtual() { return passoAtual; }
    public void setPassoAtual(int passoAtual) { this.passoAtual = passoAtual; }
    public LocalDate getProximaVigilia() { return proximaVigilia; }
    public void setProximaVigilia(LocalDate proximaVigilia) { this.proximaVigilia = proximaVigilia; }
}
