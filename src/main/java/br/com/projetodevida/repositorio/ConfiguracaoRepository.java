package br.com.projetodevida.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.projetodevida.dominio.Configuracao;

public interface ConfiguracaoRepository extends JpaRepository<Configuracao, Long> {
}
