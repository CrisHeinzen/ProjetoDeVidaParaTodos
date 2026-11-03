package br.com.projetodevida.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.projetodevida.dominio.PessoaApoio;

public interface PessoaApoioRepository extends JpaRepository<PessoaApoio, Long> {

    List<PessoaApoio> findAllByOrderByIdAsc();
}
