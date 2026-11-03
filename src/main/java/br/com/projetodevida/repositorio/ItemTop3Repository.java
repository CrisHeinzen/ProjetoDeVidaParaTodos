package br.com.projetodevida.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.projetodevida.dominio.ItemTop3;

public interface ItemTop3Repository extends JpaRepository<ItemTop3, Long> {

    List<ItemTop3> findBySemanaOrderByIdAsc(String semana);

    long countBySemana(String semana);
}
