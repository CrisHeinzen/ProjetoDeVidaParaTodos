package br.com.projetodevida.dominio;

/** Erro de validação que deve ser mostrado ao usuário (ex.: "Escreva a meta."). */
public class RegraDeNegocioException extends RuntimeException {
    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }
}
