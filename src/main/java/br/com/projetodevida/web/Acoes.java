package br.com.projetodevida.web;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.projetodevida.dominio.RegraDeNegocioException;

/**
 * Executa uma ação vinda de um formulário e redireciona (padrão Post/Redirect/Get).
 * Erros de regra de negócio viram uma mensagem "erro" exibida no topo da página.
 */
final class Acoes {

    private Acoes() { }

    static String executar(RedirectAttributes ra, String destino, Runnable acao) {
        return executar(ra, destino, acao, null);
    }

    static String executar(RedirectAttributes ra, String destino, Runnable acao, String mensagemSucesso) {
        try {
            acao.run();
            if (mensagemSucesso != null) ra.addFlashAttribute("mensagem", mensagemSucesso);
        } catch (RegraDeNegocioException e) {
            ra.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:" + destino;
    }
}
