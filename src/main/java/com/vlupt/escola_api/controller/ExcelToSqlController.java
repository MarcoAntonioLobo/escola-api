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

    // ======================================================
    // GERA SQL (NÃO EXECUTA)
    // ======================================================
    @PostMapping(
        value = "/excel-to-sql",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> convertExcel(
            @RequestPart("file") MultipartFile file,
            @RequestParam("table") String tableName) {

        try {
            validateFile(file);

            List<String> sqlLines =
                    service.convert(file.getInputStream(), tableName);

            return ResponseEntity.ok(sqlLines);

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao processar o arquivo XLSX: " + e.getMessage());
        }
    }

    // ======================================================
    // INSERE DIRETO NO BANCO
    // ======================================================
    @PostMapping(
        value = "/excel-to-db",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> saveExcelToDatabase(
            @RequestPart("file") MultipartFile file) {

        try {
            validateFile(file);

            service.saveToDatabase(file.getInputStream());

            return ResponseEntity.ok(
                    "Dados inseridos com sucesso!"
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao processar o arquivo XLSX: " + e.getMessage());
        }
    }

    // ======================================================
    // VALIDAÇÃO BÁSICA
    // ======================================================
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Nenhum arquivo XLSX foi enviado."
            );
        }
    }
}
