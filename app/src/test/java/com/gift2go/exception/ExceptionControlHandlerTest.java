package com.gift2go.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExceptionControlHandlerTest {

    private final ExceptionControlHandler handler = new ExceptionControlHandler();

    @Test
    void testValidationException() {
        var ex = new ValidationException("Invalid UUID");
        var response = handler.handleValidationException(ex);

        assertEquals(400, response.getBody().get("status"));
        assertEquals("Bad Request", response.getBody().get("error"));
        assertEquals("Invalid UUID", response.getBody().get("message"));
    }

    @Test
    void testFileParseException() {
        var ex = new FileParserException("Parse failed");
        var response = handler.handleFileParserException(ex);

        assertEquals(422, response.getBody().get("status"));
        assertEquals("Unprocessable Entity", response.getBody().get("error"));
        assertEquals("Parse failed", response.getBody().get("message"));
    }

    @Test
    void testGenericException() {
        var ex = new RuntimeException("Something went wrong");
        var response = handler.handleAllUncaughtException(ex);

        assertEquals(500, response.getBody().get("status"));
        assertEquals("Internal Server Error", response.getBody().get("error"));
        assertEquals("An unexpected error occurred", response.getBody().get("message"));
    }
}
