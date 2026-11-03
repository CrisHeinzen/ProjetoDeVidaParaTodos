package br.com.projetodevida.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.projetodevida.dominio.Revisao;
import br.com.projetodevida.dominio.TipoRevisao;

public interface RevisaoRepository extends JpaRepository<Revisao, Long> {

    List<Revisao> findAllByOrderByRegistradaEmDesc();

    long countByTipo(TipoRevisao tipo);
}
