package br.com.projetodevida.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.projetodevida.dominio.Area;
import br.com.projetodevida.dominio.NotaRoda;

public interface NotaRodaRepository extends JpaRepository<NotaRoda, Area> {
}
