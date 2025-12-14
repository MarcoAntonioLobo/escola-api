package com.vlupt.escola_api.service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ExcelToSqlService {

    private final JdbcTemplate jdbcTemplate;

    public ExcelToSqlService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ======================================================
    // 1️⃣ INSERE DIRETO NO BANCO - SEPARANDO CLIENT E CLIENT_DATA
    // ======================================================
    public void saveToDatabase(InputStream is) throws Exception {
        try (Workbook workbook = WorkbookFactory.create(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            if (!rows.hasNext()) return;

            Row headerRow = rows.next();
            List<String> columns = readAndNormalizeHeader(headerRow);

            while (rows.hasNext()) {
                Row row = rows.next();
                if (isRowEmpty(row, row.getLastCellNum())) continue;

                // ===== 1. INSERIR CLIENT =====
                List<String> clientCols = List.of("school_name", "cafeteria_name", "location", "student_count");
                List<Object> clientValues = new ArrayList<>();
                for (String col : clientCols) {
                    int idx = columns.indexOf(col);
                    if (idx >= 0) clientValues.add(toJdbcValue(row.getCell(idx, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)));
                }

                String clientSql = String.format(
                        "INSERT INTO client (%s) VALUES (%s)",
                        String.join(",", clientCols),
                        String.join(",", clientCols.stream().map(c -> "?").toArray(String[]::new))
                );
                jdbcTemplate.update(clientSql, clientValues.toArray());

                Long clientId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

                // ===== 2. INSERIR CLIENT_DATA =====
                List<String> clientDataCols = new ArrayList<>();
                List<Object> clientDataValues = new ArrayList<>();
                for (int i = 0; i < columns.size(); i++) {
                    String col = columns.get(i);
                    if (clientCols.contains(col)) continue;            // Ignora colunas de client
                    if (col.equalsIgnoreCase("client_id")) continue;   // Ignora client_id do Excel
                    clientDataCols.add(col);
                    clientDataValues.add(toJdbcValue(row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)));
                }

                // Adiciona a FK client_id
                clientDataCols.add("client_id");
                clientDataValues.add(clientId);

                String clientDataSql = String.format(
                        "INSERT INTO client_data (%s) VALUES (%s)",
                        String.join(",", clientDataCols),
                        String.join(",", clientDataCols.stream().map(c -> "?").toArray(String[]::new))
                );
                jdbcTemplate.update(clientDataSql, clientDataValues.toArray());
            }
        }
    }

    // ======================================================
    // 2️⃣ GERA SQL EM MEMÓRIA (APENAS TEXTO, NÃO EXECUTA)
    // ======================================================
    public List<String> convert(InputStream is, String tableName) throws Exception {
        try (Workbook workbook = WorkbookFactory.create(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            List<String> sqlLines = new ArrayList<>();
            if (!rows.hasNext()) return sqlLines;

            List<String> columns = readAndNormalizeHeader(rows.next());
            columns.removeIf(c -> c.equalsIgnoreCase("dataId"));

            while (rows.hasNext()) {
                Row row = rows.next();
                if (isRowEmpty(row, row.getLastCellNum())) continue;

                List<String> values = new ArrayList<>();
                for (int i = 0; i < columns.size(); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    values.add(toSqlLiteral(cell));
                }

                String sql = String.format(
                        "INSERT INTO %s (%s) VALUES (%s);",
                        tableName,
                        String.join(",", columns),
                        String.join(",", values)
                );
                sqlLines.add(sql);
            }
            return sqlLines;
        }
    }

    // ======================================================
    // MÉTODOS AUXILIARES
    // ======================================================
    private List<String> readAndNormalizeHeader(Row headerRow) {
        List<String> columns = new ArrayList<>();
        for (Cell cell : headerRow) {
            String col = cell.getStringCellValue();
            if (!StringUtils.hasText(col)) throw new IllegalArgumentException("Nome de coluna vazio no Excel.");
            columns.add(col.trim().replaceAll("\\s+", "_"));
        }
        return columns;
    }

    private boolean isRowEmpty(Row row, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            CellType type = cell.getCellType();
            if (type == CellType.FORMULA) type = cell.getCachedFormulaResultType();
            if (type != CellType.BLANK) {
                if (type == CellType.STRING && !cell.getStringCellValue().trim().isEmpty()) return false;
                if (type != CellType.STRING) return false;
            }
        }
        return true;
    }

    private Object toJdbcValue(Cell cell) {
        CellType type = cell.getCellType();
        if (type == CellType.FORMULA) type = cell.getCachedFormulaResultType();

        switch (type) {
            case STRING: return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) return Date.valueOf(cell.getLocalDateTimeCellValue().toLocalDate());
                return BigDecimal.valueOf(cell.getNumericCellValue());
            case BOOLEAN: return cell.getBooleanCellValue();
            case BLANK:
            default: return null;
        }
    }

    private String toSqlLiteral(Cell cell) {
        CellType type = cell.getCellType();
        if (type == CellType.FORMULA) type = cell.getCachedFormulaResultType();

        switch (type) {
            case STRING: return "'" + cell.getStringCellValue().replace("'", "''") + "'";
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    LocalDate date = cell.getLocalDateTimeCellValue().toLocalDate();
                    return "'" + date + "'";
                }
                return BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            case BLANK:
            default: return "NULL";
        }
    }
}