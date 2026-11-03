package br.com.projetodevida.web;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.projetodevida.backup.BackupJson;
import br.com.projetodevida.backup.BackupService;

/** Exportar / importar backup JSON e apagar tudo. */
@Controller
@RequestMapping("/dados")
public class DadosController {

    private final BackupService backupService;
    private final ObjectMapper objectMapper;

    public DadosController(BackupService backupService, ObjectMapper objectMapper) {
        this.backupService = backupService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/exportar")
    public ResponseEntity<byte[]> exportar() throws IOException {
        byte[] json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(backupService.exportar());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"projeto-de-vida-backup.json\"")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

    @PostMapping("/importar")
    public String importar(@RequestParam("arquivo") MultipartFile arquivo, RedirectAttributes ra) {
        if (arquivo.isEmpty()) {
            ra.addFlashAttribute("erro", "Escolha um arquivo .json.");
            return "redirect:/revisar#dados";
        }
        try {
            BackupJson backup = objectMapper.readValue(arquivo.getInputStream(), BackupJson.class);
            backupService.importar(backup);
            ra.addFlashAttribute("mensagem", "Dados importados!");
        } catch (IOException | RuntimeException e) {
            // Se algo falhar, a transação é desfeita e os dados atuais continuam como estavam.
            ra.addFlashAttribute("erro", "Arquivo inválido. Nenhum dado foi alterado.");
        }
        return "redirect:/revisar#dados";
    }

    @PostMapping("/apagar")
    public String apagar(RedirectAttributes ra) {
        backupService.apagarTudo();
        ra.addFlashAttribute("mensagem", "Todos os dados foram apagados.");
        return "redirect:/descobrir";
    }
}
