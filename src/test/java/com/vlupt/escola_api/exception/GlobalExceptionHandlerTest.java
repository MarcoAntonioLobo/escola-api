package com.vlupt.escola_api.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleNotFound() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Não encontrado");

        ResponseEntity<Map<String, Object>> response = handler.handleNotFound(ex);
        Map<String, Object> body = response.getBody();

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Não encontrado", body.get("message"));
        assertEquals("Not Found", body.get("error"));
        assertEquals(404, body.get("status"));
    }

    @Test
    void testHandleConflict() {
        ConflictException ex = new ConflictException("Conflito");

        ResponseEntity<Map<String, Object>> response = handler.handleConflict(ex);
        Map<String, Object> body = response.getBody();

        assertEquals(409, response.getStatusCode().value());
        assertEquals("Conflito", body.get("message"));
        assertEquals("Conflict", body.get("error"));
        assertEquals(409, body.get("status"));
    }

    @Test
    void testHandleBadRequest() {
        BadRequestException ex = new BadRequestException("Requisição ruim");

        ResponseEntity<Map<String, Object>> response = handler.handleBadRequest(ex);
        Map<String, Object> body = response.getBody();

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Requisição ruim", body.get("message"));
        assertEquals("Bad Request", body.get("error"));
        assertEquals(400, body.get("status"));
    }

    @Test
    void testHandleGeneric() {
        Exception ex = new Exception("Erro genérico");

        ResponseEntity<Map<String, Object>> response = handler.handleAll(ex);
        Map<String, Object> body = response.getBody();

        assertEquals(500, response.getStatusCode().value());
        assertEquals("Erro genérico", body.get("message"));
        assertEquals("Internal Server Error", body.get("error"));
        assertEquals(500, body.get("status"));
    }
}
