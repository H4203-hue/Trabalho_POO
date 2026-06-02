package com.tarefas.exception;

import com.tarefas.dto.RespostaDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * Tratamento global de exceções da API.
 * O @RestControllerAdvice intercepta exceções lançadas em qualquer controller.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata exceções de validação (Bean Validation).
     * Disparado quando os campos do DTO não passam nas validações (@NotBlank, @Size, etc.)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespostaDTO<Void>> handleValidationException(MethodArgumentNotValidException ex) {
        // Coleta todas as mensagens de erro de validação
        List<String> erros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .toList();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(RespostaDTO.erro(400, "Dados inválidos", erros));
    }

    /**
     * Trata o caso em que a tarefa não é encontrada no banco.
     */
    @ExceptionHandler(TarefaNotFoundException.class)
    public ResponseEntity<RespostaDTO<Void>> handleTarefaNotFound(TarefaNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(RespostaDTO.erro(404, ex.getMessage()));
    }

    /**
     * Trata erros gerais não previstos.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RespostaDTO<Void>> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(RespostaDTO.erro(500, "Erro interno no servidor: " + ex.getMessage()));
    }
}
