package br.com.projetodevida.web;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.projetodevida.dominio.Area;
import br.com.projetodevida.dominio.Horizonte;
import br.com.projetodevida.dominio.PassoJornada;
import br.com.projetodevida.servico.JornadaService;
import br.com.projetodevida.servico.MetaService;

/** Aba 2 · Planejar. */
@Controller
@RequestMapping("/planejar")
public class PlanejarController {

    /** Uma linha do gráfico de equilíbrio entre áreas. */
    public record BarraArea(Area area, long quantidade, int percentual) { }

    private final MetaService metaService;
    private final JornadaService jornadaService;

    public PlanejarController(MetaService metaService, JornadaService jornadaService) {
        this.metaService = metaService;
        this.jornadaService = jornadaService;
    }

    @GetMapping
    public String pagina(Model model) {
        Map<Area, Long> contagem = metaService.contagemPorArea();
        long maior = Math.max(1, contagem.values().stream().mapToLong(Long::longValue).max().orElse(0));
        List<BarraArea> barras = contagem.entrySet().stream()
                .map(e -> new BarraArea(e.getKey(), e.getValue(), (int) (e.getValue() * 100 / maior)))
                .toList();

        model.addAttribute("valores", jornadaService.resposta(PassoJornada.VALORES));
        model.addAttribute("barras", barras);
        model.addAttribute("areas", Area.values());
        model.addAttribute("horizontes", Horizonte.values());
        model.addAttribute("metas", metaService.listar());
        model.addAttribute("linhaDoTempo", metaService.linhaDoTempo());
        model.addAttribute("pessoas", metaService.redeDeApoio());
        model.addAttribute("papeis", MetaService.PAPEIS_APOIO);
        return "planejar";
    }

    @PostMapping("/metas")
    public String criarMeta(@RequestParam Area area, @RequestParam Horizonte horizonte,
                            @RequestParam String titulo,
                            @RequestParam(defaultValue = "") String porque,
                            @RequestParam(defaultValue = "") String primeiroPasso,
                            @RequestParam(defaultValue = "") String cha,
                            RedirectAttributes ra) {
        return Acoes.executar(ra, "/planejar",
                () -> metaService.criar(area, horizonte, titulo, porque, primeiroPasso, cha));
    }

    @PostMapping("/metas/{id}/alternar")
    public String alternarMeta(@PathVariable Long id) {
        metaService.alternarConcluida(id);
        return "redirect:/planejar#metas";
    }

    @PostMapping("/metas/{id}/excluir")
    public String excluirMeta(@PathVariable Long id) {
        metaService.excluir(id);
        return "redirect:/planejar#metas";
    }

    @PostMapping("/apoio")
    public String adicionarPessoa(@RequestParam String nome, @RequestParam String papel, RedirectAttributes ra) {
        return Acoes.executar(ra, "/planejar#apoio", () -> metaService.adicionarPessoa(nome, papel));
    }

    @PostMapping("/apoio/{id}/excluir")
    public String removerPessoa(@PathVariable Long id) {
        metaService.removerPessoa(id);
        return "redirect:/planejar#apoio";
    }
}
