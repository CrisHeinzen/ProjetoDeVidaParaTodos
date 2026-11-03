package br.com.projetodevida.web;

import java.util.EnumMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.projetodevida.dominio.Area;
import br.com.projetodevida.dominio.PassoJornada;
import br.com.projetodevida.servico.GraficoRoda;
import br.com.projetodevida.servico.JornadaService;

/** Aba 1 · Descobrir. */
@Controller
@RequestMapping("/descobrir")
public class DescobrirController {

    private final JornadaService jornadaService;

    public DescobrirController(JornadaService jornadaService) {
        this.jornadaService = jornadaService;
    }

    @GetMapping
    public String pagina(Model model) {
        int atual = jornadaService.passoAtual();
        boolean concluida = atual >= JornadaService.TOTAL_PASSOS;
        model.addAttribute("passos", PassoJornada.values());
        model.addAttribute("passoAtual", atual);
        model.addAttribute("concluida", concluida);
        if (!concluida) {
            PassoJornada passo = PassoJornada.values()[atual];
            model.addAttribute("passo", passo);
            model.addAttribute("resposta", jornadaService.resposta(passo));
        }
        Map<Area, Integer> notas = jornadaService.notasRoda();
        model.addAttribute("notas", notas);
        model.addAttribute("svgRoda", GraficoRoda.svg(notas));
        return "descobrir";
    }

    /** Salva o texto do passo atual (se houver) e navega para o passo "destino". */
    @PostMapping("/passo")
    public String navegar(@RequestParam(required = false) Integer passo,
                          @RequestParam(required = false) String texto,
                          @RequestParam int destino) {
        if (passo != null) jornadaService.salvarResposta(passo, texto);
        jornadaService.irParaPasso(destino);
        return "redirect:/descobrir";
    }

    /** Recebe campos "nota_fisico", "nota_intelectual"... com valores de 0 a 10. */
    @PostMapping("/roda")
    public String salvarRoda(@RequestParam Map<String, String> campos) {
        Map<Area, Integer> notas = new EnumMap<>(Area.class);
        for (Area a : Area.values()) {
            String valor = campos.get("nota_" + a.getCodigo());
            if (valor != null && valor.matches("\\d{1,2}")) notas.put(a, Integer.parseInt(valor));
        }
        jornadaService.salvarRoda(notas);
        return "redirect:/descobrir#roda";
    }
}
