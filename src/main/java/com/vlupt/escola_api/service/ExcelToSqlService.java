package com.vlupt.escola_api.service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class ExcelToSqlService {

    private final JdbcTemplate jdbcTemplate;

    public ExcelToSqlService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ======================================================
    // INSERE DIRETO NO BANCO (CLIENT + CLIENT_DATA)
    // ======================================================
    @Transactional
    public void saveToDatabase(InputStream is) throws Exception {

        try (Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            if (!rows.hasNext()) {
                throw new IllegalArgumentException("Planilha Excel vazia.");
            }

            Row headerRow = rows.next();
            List<String> columns = readAndNormalizeHeader(headerRow);
            validateHeader(columns);

            List<String> clientCols = List.of(
                    "school_name",
                    "cafeteria_name",
                    "location",
                    "student_count"
            );

            while (rows.hasNext()) {

                Row row = rows.next();
                if (isRowEmpty(row, columns.size())) continue;

                // ==================================================
                // 1️⃣ BUSCA OU CRIA CLIENT
                // ==================================================
                List<Object> clientValues = new ArrayList<>();

                for (String col : clientCols) {
                    int idx = columns.indexOf(col);
                    if (idx < 0) {
                        throw new IllegalArgumentException("Coluna obrigatória ausente: " + col);
                    }
                    clientValues.add(
                            toJdbcValue(row.getCell(idx, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK))
                    );
                }

                Long clientId = findClientId(
                        clientValues.get(0),
                        clientValues.get(1),
                        clientValues.get(2)
                );

                if (clientId == null) {
                    jdbcTemplate.update(
                            """
                            INSERT INTO client (school_name, cafeteria_name, location, student_count)
                            VALUES (?, ?, ?, ?)
                            """,
                            clientValues.toArray()
                    );
                    clientId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
                }

                // ==================================================
                // 2️⃣ UPSERT CLIENT_DATA
                // ==================================================
                List<String> dataCols = new ArrayList<>();
                List<Object> dataValues = new ArrayList<>();

                for (int i = 0; i < columns.size(); i++) {
                    String col = columns.get(i);
                    if (clientCols.contains(col)) continue;
                    if ("client_id".equals(col)) continue;

                    dataCols.add(col);
                    dataValues.add(
                            toJdbcValue(row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK))
                    );
                }

                dataCols.add("client_id");
                dataValues.add(clientId);

                String insertCols = String.join(",", dataCols);
                String placeholders = String.join(",", dataCols.stream().map(c -> "?").toList());

                String updateClause = dataCols.stream()
                        .filter(c -> !c.equals("client_id") && !c.equals("month_date"))
                        .map(c -> c + " = VALUES(" + c + ")")
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("");

                String sql = """
                    INSERT INTO client_data (%s)
                    VALUES (%s)
                    ON DUPLICATE KEY UPDATE %s
                    """.formatted(insertCols, placeholders, updateClause);

                jdbcTemplate.update(sql, dataValues.toArray());
            }
        }
    }

    // ======================================================
    // GERA SQL EM MEMÓRIA (NÃO EXECUTA)
    // ======================================================
    public List<String> convert(InputStream is, String tableName) throws Exception {

        try (Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            List<String> sqlLines = new ArrayList<>();

            if (!rows.hasNext()) return sqlLines;

            List<String> columns = readAndNormalizeHeader(rows.next());
            columns.removeIf(c -> c.equalsIgnoreCase("data_id"));

            while (rows.hasNext()) {

                Row row = rows.next();
                if (isRowEmpty(row, columns.size())) continue;

                List<String> values = new ArrayList<>();

                for (int i = 0; i < columns.size(); i++) {
                    values.add(toSqlLiteral(
                            row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
                    ));
                }

                sqlLines.add(
                        "INSERT INTO %s (%s) VALUES (%s);"
                                .formatted(tableName, String.join(",", columns), String.join(",", values))
                );
            }

            return sqlLines;
        }
    }

    // ======================================================
    // AUXILIARES
    // ======================================================

    private Long findClientId(Object school, Object cafeteria, Object location) {
        return jdbcTemplate.query(
                """
                SELECT client_id
                FROM client
                WHERE school_name = ?
                  AND cafeteria_name = ?
                  AND location = ?
                """,
                rs -> rs.next() ? rs.getLong("client_id") : null,
                school, cafeteria, location
        );
    }

    private void validateHeader(List<String> columns) {

        List<String> expected = List.of(
                "school_name",
                "cafeteria_name",
                "location",
                "student_count",
                "month_date",
                "cantina_percent",
                "registered_students",
                "average_cantina_per_student",
                "average_pedagogical_per_student",
                "order_count",
                "revenue",
                "profitability",
                "revenue_loss",
                "orders_outside_vpt",
                "average_ticket_app"
        );

        if (!columns.containsAll(expected)) {
            throw new IllegalArgumentException("Header do Excel não corresponde ao modelo esperado.");
        }
    }

    private List<String> readAndNormalizeHeader(Row headerRow) {
        List<String> columns = new ArrayList<>();

        for (Cell cell : headerRow) {
            String col = cell.getStringCellValue();
            if (!StringUtils.hasText(col)) {
                throw new IllegalArgumentException("Nome de coluna vazio no Excel.");
            }
            columns.add(col.trim().toLowerCase().replaceAll("\\s+", "_"));
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

        return switch (type) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> DateUtil.isCellDateFormatted(cell)
                    ? Date.valueOf(cell.getLocalDateTimeCellValue().toLocalDate())
                    : BigDecimal.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> cell.getBooleanCellValue();
            default -> null;
        };
    }

    private String toSqlLiteral(Cell cell) {
        CellType type = cell.getCellType();
        if (type == CellType.FORMULA) type = cell.getCachedFormulaResultType();

        return switch (type) {
            case STRING -> "'" + cell.getStringCellValue().replace("'", "''") + "'";
            case NUMERIC -> DateUtil.isCellDateFormatted(cell)
                    ? "'" + cell.getLocalDateTimeCellValue().toLocalDate() + "'"
                    : BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "NULL";
        };
    }
}
