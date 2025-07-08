package com.project.backend.exception;

import com.project.backend.dto.exception.ExceptionResponse;
import com.project.backend.utils.JsonParserUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.security.auth.message.AuthException;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import javax.security.auth.login.LoginException;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleValidationError() throws NoSuchMethodException {
        Method method = this.getClass().getDeclaredMethod("dummyMethod", String.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);

        Object target = new Object();
        BindException bindException = new BindException(target, "target");
        bindException.addError(new FieldError("target", "field", "Must not be blank"));

        MethodArgumentNotValidException exception =
                new MethodArgumentNotValidException(methodParameter, bindException.getBindingResult());

        ExceptionResponse response = handler.handleValidationError(exception);

        assertNotNull(response);
        assertEquals(List.of("Must not be blank"), response.getMessages());
    }

    private void dummyMethod(String input) {}

    @Test
    void testHandleEntityNotFoundException() {
        ExceptionResponse response = handler.handleEntityNotFoundException(
                new EntityNotFoundException("Entity not found"));

        assertNotNull(response);
        assertEquals(List.of("Entity not found"), response.getMessages());
    }

    @Test
    void testHandleLoginException() {
        ExceptionResponse response = handler.handleLoginException(
                new LoginException("Login failed"));

        assertNotNull(response);
        assertEquals(List.of("Login failed"), response.getMessages());
    }

    @Test
    void testHandleBadRequestExceptions() {
        ExceptionResponse response = handler.handleBadRequestExceptions(
                new IllegalStateException("Illegal state"));

        assertNotNull(response);
        assertEquals(List.of("Illegal state"), response.getMessages());
    }

    @Test
    void testHandleAuthException() throws AuthException {
        ExceptionResponse response = handler.handleAuthException(new AuthException("Unauthorized"));

        assertNotNull(response);
        assertEquals(List.of("Unauthorized"), response.getMessages());
    }

    @Test
    void testHandleResponseStatusException() {
        // Mock static method
        try (var mocked = mockStatic(JsonParserUtil.class)) {
            mocked.when(() -> JsonParserUtil.extractErrorMessages("json"))
                    .thenReturn(List.of("Parsed error"));

            ResponseStatusException ex = new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "json");

            ResponseEntity<ExceptionResponse> response = handler.handleResponseStatusException(ex);

            assertNotNull(response);
            assertEquals(400, response.getStatusCode().value());
            assertEquals(List.of("Parsed error"), response.getBody().getMessages());
        }
    }
}
