package br.com.projetodevida.web;

import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.projetodevida.dominio.TipoRevisao;
import br.com.projetodevida.servico.RevisaoService;

/** Aba 5 · Revisar. */
@Controller
@RequestMapping("/revisar")
public class RevisarController {

    static final DateTimeFormatter DATA_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /** Revisão já formatada para exibição. */
    public record ItemHistorico(String titulo, String data, String avancos, String ajustes, String metas, String rumo) { }

    private final RevisaoService revisaoService;
    private final Clock relogio;

    public RevisarController(RevisaoService revisaoService, Clock relogio) {
        this.revisaoService = revisaoService;
        this.relogio = relogio;
    }

    @GetMapping
    public String pagina(Model model) {
        RevisaoService.StatusVigilia status = revisaoService.statusVigilia();
        List<ItemHistorico> historico = revisaoService.historico().stream()
                .map(r -> new ItemHistorico(
                        r.getTipo() == TipoRevisao.VIGILIA ? "🕯️ Vigília" : "📆 Semana",
                        r.getRegistradaEm().atZone(relogio.getZone()).format(DATA_BR),
                        r.getAvancos(), r.getAjustes(), r.getMetas(), r.getRumo()))
                .toList();

        model.addAttribute("status", status);
        model.addAttribute("dataVigilia", status.agendada() ? status.data().format(DATA_BR) : null);
        model.addAttribute("historico", historico);
        return "revisar";
    }

    @PostMapping("/agendar")
    public String agendar(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                          RedirectAttributes ra) {
        return Acoes.executar(ra, "/revisar", () -> revisaoService.agendarVigilia(data));
    }

    @PostMapping("/semana")
    public String fecharSemana(@RequestParam(defaultValue = "") String avancos,
                               @RequestParam(defaultValue = "") String ajustes,
                               RedirectAttributes ra) {
        return Acoes.executar(ra, "/revisar", () -> revisaoService.fecharSemana(avancos, ajustes),
                "Semana fechada! Escolha o top 3 da próxima na aba Semana.");
    }

    @PostMapping("/vigilia")
    public String registrarVigilia(@RequestParam(defaultValue = "") String mudancas,
                                   @RequestParam(defaultValue = "") String metas,
                                   @RequestParam(defaultValue = "") String rumo,
                                   RedirectAttributes ra) {
        return Acoes.executar(ra, "/revisar", () -> revisaoService.registrarVigilia(mudancas, metas, rumo),
                "Vigília registrada! Próxima sugerida daqui a 3 meses — ajuste a data se quiser.");
    }
}
