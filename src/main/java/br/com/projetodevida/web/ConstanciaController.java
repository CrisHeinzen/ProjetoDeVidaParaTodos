package br.com.projetodevida.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.projetodevida.servico.ConstanciaService;

/** Aba 4 · Constância. */
@Controller
public class ConstanciaController {

    private final ConstanciaService constanciaService;

    public ConstanciaController(ConstanciaService constanciaService) {
        this.constanciaService = constanciaService;
    }

    @GetMapping("/constancia")
    public String pagina(Model model) {
        model.addAttribute("painel", constanciaService.painel());
        return "constancia";
    }
}
