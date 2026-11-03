package br.com.projetodevida.servico;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import br.com.projetodevida.dominio.Area;

/**
 * Gera o SVG (gráfico radar) da Roda da Vida. Só usa números e nomes fixos das áreas,
 * nunca texto digitado pelo usuário, por isso pode ser inserido sem escape na página.
 */
public final class GraficoRoda {

    private static final double CX = 115, CY = 115, R = 95;

    private GraficoRoda() { }

    public static String svg(Map<Area, Integer> notas) {
        StringBuilder sb = new StringBuilder();
        sb.append("<svg width=\"230\" height=\"230\" viewBox=\"0 0 230 230\" role=\"img\" aria-label=\"Roda da Vida\">");
        for (int r = 1; r <= 5; r++) {
            sb.append(fmt("<circle cx=\"%.1f\" cy=\"%.1f\" r=\"%.1f\" fill=\"none\" stroke=\"#e6e1d6\"/>", CX, CY, R * r / 5));
        }
        Area[] areas = Area.values();
        List<String> pontos = new ArrayList<>();
        StringBuilder rotulos = new StringBuilder();
        for (int i = 0; i < areas.length; i++) {
            double ang = -Math.PI / 2 + i * Math.PI * 2 / areas.length;
            double cos = Math.cos(ang), sin = Math.sin(ang);
            sb.append(fmt("<line x1=\"%.1f\" y1=\"%.1f\" x2=\"%.1f\" y2=\"%.1f\" stroke=\"#e6e1d6\"/>",
                    CX, CY, CX + R * cos, CY + R * sin));
            double v = notas.getOrDefault(areas[i], JornadaService.NOTA_PADRAO) / 10.0;
            pontos.add(fmt("%.1f,%.1f", CX + R * v * cos, CY + R * v * sin));
            rotulos.append(fmt("<text x=\"%.1f\" y=\"%.1f\" font-size=\"9.5\" text-anchor=\"middle\" dominant-baseline=\"middle\" fill=\"%s\">%s</text>",
                    CX + (R + 13) * cos, CY + (R + 13) * sin, areas[i].getCor(), areas[i].getNome()));
        }
        sb.append("<polygon points=\"").append(String.join(" ", pontos))
          .append("\" fill=\"rgba(74,103,65,.25)\" stroke=\"#4a6741\" stroke-width=\"2\"/>");
        sb.append(rotulos).append("</svg>");
        return sb.toString();
    }

    /** Formata sempre com ponto decimal (Locale.ROOT), independente do idioma do sistema. */
    private static String fmt(String formato, Object... args) {
        return String.format(Locale.ROOT, formato, args);
    }
}
