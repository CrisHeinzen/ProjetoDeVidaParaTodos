package br.com.projetodevida.web;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import br.com.projetodevida.RelogioFixoConfig;
import br.com.projetodevida.backup.BackupService;
import br.com.projetodevida.backup.BackupServiceTest;

@SpringBootTest
@AutoConfigureMockMvc
@Import(RelogioFixoConfig.class)
class PaginasTest {

    @Autowired MockMvc mvc;
    @Autowired BackupService backupService;

    @BeforeEach
    void limpar() {
        backupService.apagarTudo();
    }

    @ParameterizedTest
    @ValueSource(strings = {"/descobrir", "/planejar", "/semana", "/constancia", "/revisar"})
    void todasAsAbasAbrem(String url) throws Exception {
        mvc.perform(get(url)).andExpect(status().isOk())
                .andExpect(content().string(containsString("Projeto de Vida para Todos")));
    }

    @Test
    void raizRedirecionaParaDescobrir() throws Exception {
        mvc.perform(get("/")).andExpect(redirectedUrl("/descobrir"));
    }

    @Test
    void textoDoUsuarioEhEscapado() throws Exception {
        mvc.perform(post("/planejar/metas").param("area", "FISICO").param("horizonte", "UM_ANO")
                        .param("titulo", "<script>alert(1)</script>"))
                .andExpect(redirectedUrl("/planejar"));
        mvc.perform(get("/planejar"))
                .andExpect(content().string(containsString("&lt;script&gt;alert(1)&lt;/script&gt;")))
                .andExpect(content().string(not(containsString("<script>alert(1)</script>"))));
    }

    @Test
    void erroDeValidacaoViraMensagem() throws Exception {
        mvc.perform(post("/revisar/semana").param("avancos", " ").param("ajustes", ""))
                .andExpect(redirectedUrl("/revisar"))
                .andExpect(flash().attribute("erro", "Escreva pelo menos um campo."));
    }

    @Test
    void jornadaSalvaEAvanca() throws Exception {
        mvc.perform(post("/descobrir/passo").param("passo", "0").param("texto", "Minha história").param("destino", "1"))
                .andExpect(redirectedUrl("/descobrir"));
        mvc.perform(get("/descobrir")).andExpect(content().string(containsString("2. Meus valores")));
    }

    @Test
    void importaEExporta() throws Exception {
        MockMultipartFile arquivo = new MockMultipartFile("arquivo", "backup.json", "application/json",
                BackupServiceTest.BACKUP_VERSAO_WEB.getBytes(StandardCharsets.UTF_8));
        mvc.perform(multipart("/dados/importar").file(arquivo))
                .andExpect(flash().attribute("mensagem", "Dados importados!"));
        mvc.perform(get("/dados/exportar")).andExpect(status().isOk())
                .andExpect(content().string(containsString("Correr 10 km")));
        mvc.perform(get("/semana")).andExpect(content().string(containsString("Treinar 3x")));
    }

    @Test
    void importarArquivoQueNaoEhJsonMostraErro() throws Exception {
        MockMultipartFile arquivo = new MockMultipartFile("arquivo", "x.json", "application/json", "não é json".getBytes());
        mvc.perform(multipart("/dados/importar").file(arquivo))
                .andExpect(flash().attribute("erro", "Arquivo inválido. Nenhum dado foi alterado."));
    }
}
