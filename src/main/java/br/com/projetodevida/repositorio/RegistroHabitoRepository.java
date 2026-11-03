package br.com.projetodevida.repositorio;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.projetodevida.dominio.RegistroHabito;

public interface RegistroHabitoRepository extends JpaRepository<RegistroHabito, Long> {

    Optional<RegistroHabito> findByNomeHabitoAndDia(String nomeHabito, LocalDate dia);

    List<RegistroHabito> findByNomeHabito(String nomeHabito);

    List<RegistroHabito> findByDiaBetween(LocalDate inicio, LocalDate fim);
}
