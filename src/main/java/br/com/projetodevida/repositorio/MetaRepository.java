package br.com.projetodevida.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.projetodevida.dominio.Meta;

public interface MetaRepository extends JpaRepository<Meta, Long> {

    List<Meta> findAllByOrderByIdAsc();
}
