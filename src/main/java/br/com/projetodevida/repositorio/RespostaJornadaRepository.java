package br.com.projetodevida.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.projetodevida.dominio.RespostaJornada;

public interface RespostaJornadaRepository extends JpaRepository<RespostaJornada, String> {
}
