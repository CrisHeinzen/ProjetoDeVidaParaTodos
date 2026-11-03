package br.com.projetodevida.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.projetodevida.dominio.Habito;

public interface HabitoRepository extends JpaRepository<Habito, Long> {

    List<Habito> findAllByOrderByIdAsc();

    boolean existsByNome(String nome);
}
