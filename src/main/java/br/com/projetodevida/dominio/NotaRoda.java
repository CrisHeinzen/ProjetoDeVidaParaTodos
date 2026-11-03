package br.com.projetodevida.dominio;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

/** Nota de 0 a 10 de uma área na Roda da Vida. */
@Entity
public class NotaRoda {

    @Id
    @Enumerated(EnumType.STRING)
    private Area area;

    private int nota;

    protected NotaRoda() { }

    public NotaRoda(Area area, int nota) {
        this.area = area;
        setNota(nota);
    }

    public Area getArea() { return area; }
    public int getNota() { return nota; }

    public void setNota(int nota) {
        this.nota = Math.max(0, Math.min(10, nota));
    }
}
