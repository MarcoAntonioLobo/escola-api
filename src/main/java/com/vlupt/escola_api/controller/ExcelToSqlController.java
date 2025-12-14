package com.vlupt.escola_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vlupt.escola_api.service.ExcelToSqlService;

@RestController
@RequestMapping("/api/convert")
@CrossOrigin(origins = "*")
public class ExcelToSqlController {

    private final ExcelToSqlService service;

    public ExcelToSqlController(ExcelToSqlService service) {
        this.service = service;
    }

    // -------------------------------
    // Apenas gera SQL (não executa)
    // -------------------------------
    @PostMapping(value = "/excel-to-sql", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> convertExcel(
            @RequestPart("file") MultipartFile file,
            @RequestParam("table") String tableName) {
        try {
            validateRequest(file, tableName);
            List<String> sqlLines = service.convert(file.getInputStream(), tableName);
            return ResponseEntity.ok(sqlLines);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao processar o arquivo XLSX: " + e.getMessage());
        }
    }

    // -------------------------------
    // Envia direto para o banco
    // -------------------------------
    @PostMapping(value = "/excel-to-db", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> saveExcelToDatabase(@RequestPart("file") MultipartFile file) {
        try {
            validateRequest(file, "client_data");
            service.saveToDatabase(file.getInputStream());
            return ResponseEntity.ok("Dados inseridos com sucesso!");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao processar o arquivo XLSX: " + e.getMessage());
        }
    }

    // -------------------------------
    // Validação básica do request
    // -------------------------------
    private void validateRequest(MultipartFile file, String tableName) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Nenhum arquivo XLSX foi enviado.");
        }
        if (tableName == null || tableName.isBlank()) {
            throw new IllegalArgumentException("O nome da tabela é obrigatório para gerar SQL.");
        }
    }
}
