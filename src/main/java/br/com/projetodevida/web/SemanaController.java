package br.com.projetodevida.web;

import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.projetodevida.dominio.Habito;
import br.com.projetodevida.dominio.Meta;
import br.com.projetodevida.dominio.TipoEspaco;
import br.com.projetodevida.servico.MetaService;
import br.com.projetodevida.servico.SemanaService;

/** Aba 3 · Semana. */
@Controller
@RequestMapping("/semana")
public class SemanaController {

    private static final DateTimeFormatter DATA_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final SemanaService semanaService;
    private final MetaService metaService;

    public SemanaController(SemanaService semanaService, MetaService metaService) {
        this.semanaService = semanaService;
        this.metaService = metaService;
    }

    @GetMapping
    public String pagina(Model model) {
        Map<Long, String> titulosMetas = metaService.listar().stream()
                .collect(Collectors.toMap(Meta::getId, Meta::getTitulo));
        Map<String, Integer> sequencias = semanaService.habitos().stream()
                .collect(Collectors.toMap(Habito::getNome, h -> semanaService.sequencia(h.getNome())));

        model.addAttribute("semana", semanaService.semanaAtual());
        model.addAttribute("hoje", semanaService.hoje().format(DATA_BR));
        model.addAttribute("top3", semanaService.top3DaSemana());
        model.addAttribute("limiteTop3", SemanaService.LIMITE_TOP3);
        model.addAttribute("titulosMetas", titulosMetas);
        model.addAttribute("metasAbertas", metaService.listarAbertas());
        model.addAttribute("habitos", semanaService.habitos());
        model.addAttribute("cumpridosHoje", semanaService.cumpridosHoje());
        model.addAttribute("sequencias", sequencias);
        model.addAttribute("tiposEspaco", TipoEspaco.values());
        model.addAttribute("espaco", semanaService.espacoPorTipo());
        return "semana";
    }

    @PostMapping("/top3")
    public String adicionarTop3(@RequestParam String texto, @RequestParam(required = false) Long metaId,
                                RedirectAttributes ra) {
        return Acoes.executar(ra, "/semana", () -> semanaService.adicionarTop3(texto, metaId));
    }

    @PostMapping("/top3/{id}/alternar")
    public String alternarTop3(@PathVariable Long id) {
        semanaService.alternarTop3(id);
        return "redirect:/semana";
    }

    @PostMapping("/top3/{id}/excluir")
    public String excluirTop3(@PathVariable Long id) {
        semanaService.excluirTop3(id);
        return "redirect:/semana";
    }

    @PostMapping("/habitos")
    public String adicionarHabito(@RequestParam String nome, RedirectAttributes ra) {
        return Acoes.executar(ra, "/semana#habitos", () -> semanaService.adicionarHabito(nome));
    }

    @PostMapping("/habitos/{id}/alternar")
    public String alternarHabito(@PathVariable Long id) {
        semanaService.alternarHabitoHoje(id);
        return "redirect:/semana#habitos";
    }

    @PostMapping("/habitos/{id}/excluir")
    public String removerHabito(@PathVariable Long id) {
        semanaService.removerHabito(id);
        return "redirect:/semana#habitos";
    }

    @PostMapping("/espaco")
    public String adicionarEspaco(@RequestParam String texto, @RequestParam String tipo, RedirectAttributes ra) {
        return Acoes.executar(ra, "/semana#espaco",
                () -> semanaService.adicionarEspaco(texto, TipoEspaco.porCodigo(tipo)));
    }

    @PostMapping("/espaco/{id}/excluir")
    public String removerEspaco(@PathVariable Long id) {
        semanaService.removerEspaco(id);
        return "redirect:/semana#espaco";
    }
}
