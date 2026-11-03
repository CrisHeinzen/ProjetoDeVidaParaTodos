package br.com.projetodevida.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.projetodevida.dominio.ItemEspaco;

public interface ItemEspacoRepository extends JpaRepository<ItemEspaco, Long> {

    List<ItemEspaco> findAllByOrderByIdAsc();
}
