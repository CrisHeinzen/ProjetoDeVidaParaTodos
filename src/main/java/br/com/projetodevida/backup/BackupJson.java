package br.com.projetodevida.backup;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Formato do arquivo de backup. Os nomes dos campos (em inglês) são os mesmos da versão
 * web anterior (chave "pvpt2" do localStorage), para que backups antigos possam ser importados.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record BackupJson(
        Map<String, String> journey,
        Map<String, Integer> wheel,
        List<Goal> goals,
        Integer step,
        List<String> habits,
        /** dia (aaaa-mm-dd) -> (nome do hábito -> cumprido?) */
        Map<String, Map<String, Boolean>> habitLog,
        /** semana ("2026-S45") -> itens */
        Map<String, List<Top3>> top3,
        List<Space> space,
        List<Support> support,
        List<Review> reviews,
        String nextReview) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Goal(Long id, String area, String horizon, String title, String why,
                       String step, String cha, boolean done) { }

    /** goalId é String porque a versão web gravava o valor de um &lt;select&gt; (texto). */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Top3(String text, String goalId, boolean done) { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Space(String text, String type) { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Support(String name, String role) { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Review(String type, String date, String wins, String changes, String goals, String next) { }
}
