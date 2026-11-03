package br.com.projetodevida.servico;

import java.util.EnumMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.projetodevida.dominio.Area;
import br.com.projetodevida.dominio.Configuracao;
import br.com.projetodevida.dominio.NotaRoda;
import br.com.projetodevida.dominio.PassoJornada;
import br.com.projetodevida.dominio.RespostaJornada;
import br.com.projetodevida.repositorio.ConfiguracaoRepository;
import br.com.projetodevida.repositorio.NotaRodaRepository;
import br.com.projetodevida.repositorio.RespostaJornadaRepository;

/** Aba "Descobrir": jornada de reflexão e Roda da Vida. */
@Service
@Transactional
public class JornadaService {

    public static final int NOTA_PADRAO = 5;
    public static final int TOTAL_PASSOS = PassoJornada.values().length;

    private final ConfiguracaoRepository configuracaoRepository;
    private final RespostaJornadaRepository respostaRepository;
    private final NotaRodaRepository notaRodaRepository;

    public JornadaService(ConfiguracaoRepository configuracaoRepository,
                          RespostaJornadaRepository respostaRepository,
                          NotaRodaRepository notaRodaRepository) {
        this.configuracaoRepository = configuracaoRepository;
        this.respostaRepository = respostaRepository;
        this.notaRodaRepository = notaRodaRepository;
    }

    /** Busca o registro único de configuração, criando-o na primeira vez. */
    public Configuracao configuracao() {
        return configuracaoRepository.findById(Configuracao.ID_UNICO)
                .orElseGet(() -> configuracaoRepository.save(new Configuracao()));
    }

    @Transactional(readOnly = true)
    public int passoAtual() {
        return configuracaoRepository.findById(Configuracao.ID_UNICO)
                .map(Configuracao::getPassoAtual).orElse(0);
    }

    public boolean jornadaConcluida() {
        return passoAtual() >= TOTAL_PASSOS;
    }

    @Transactional(readOnly = true)
    public String resposta(PassoJornada passo) {
        return respostaRepository.findById(passo.getCodigo()).map(RespostaJornada::getTexto).orElse("");
    }

    /** Salva o texto do passo de índice {@code indice}. Índices fora da faixa são ignorados. */
    public void salvarResposta(int indice, String texto) {
        if (indice < 0 || indice >= TOTAL_PASSOS || texto == null) return;
        String codigo = PassoJornada.values()[indice].getCodigo();
        RespostaJornada resposta = respostaRepository.findById(codigo)
                .orElseGet(() -> new RespostaJornada(codigo, ""));
        resposta.setTexto(texto);
        respostaRepository.save(resposta);
    }

    /** Vai para o passo indicado (0..TOTAL_PASSOS; TOTAL_PASSOS = "esboço concluído"). */
    public void irParaPasso(int indice) {
        Configuracao c = configuracao();
        c.setPassoAtual(Math.max(0, Math.min(indice, TOTAL_PASSOS)));
        configuracaoRepository.save(c);
    }

    /** Notas da roda em ordem das áreas; áreas sem nota valem {@value #NOTA_PADRAO}. */
    @Transactional(readOnly = true)
    public Map<Area, Integer> notasRoda() {
        Map<Area, Integer> notas = new EnumMap<>(Area.class);
        for (Area a : Area.values()) notas.put(a, NOTA_PADRAO);
        notaRodaRepository.findAll().forEach(n -> notas.put(n.getArea(), n.getNota()));
        return notas;
    }

    public void salvarRoda(Map<Area, Integer> notas) {
        notas.forEach((area, nota) -> notaRodaRepository.save(new NotaRoda(area, nota)));
    }
}
