package com.aluguelcarros.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class TratadorGlobalExcecoes {

    public record RespostaErro(LocalDateTime momento, int status, String mensagem, Map<String, String> campos) {
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<RespostaErro> tratarRegraNegocio(RegraNegocioException e) {
        return responder(HttpStatus.BAD_REQUEST, e.getMessage(), null);
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<RespostaErro> tratarNaoEncontrado(RecursoNaoEncontradoException e) {
        return responder(HttpStatus.NOT_FOUND, e.getMessage(), null);
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<RespostaErro> tratarCredenciais(CredenciaisInvalidasException e) {
        return responder(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
    }

    @ExceptionHandler({AcessoNegadoException.class, AuthorizationDeniedException.class})
    public ResponseEntity<RespostaErro> tratarAcessoNegado(RuntimeException e) {
        return responder(HttpStatus.FORBIDDEN, "Acesso negado", null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<RespostaErro> tratarIntegridade(DataIntegrityViolationException e) {
        return responder(HttpStatus.CONFLICT, "Operação viola restrições de dados (registro duplicado ou em uso)", null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespostaErro> tratarValidacao(MethodArgumentNotValidException e) {
        Map<String, String> campos = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(erro -> campos.putIfAbsent(erro.getField(), erro.getDefaultMessage()));
        return responder(HttpStatus.BAD_REQUEST, "Dados inválidos", campos);
    }

    private ResponseEntity<RespostaErro> responder(HttpStatus status, String mensagem, Map<String, String> campos) {
        return ResponseEntity.status(status)
                .body(new RespostaErro(LocalDateTime.now(), status.value(), mensagem, campos));
    }
}
