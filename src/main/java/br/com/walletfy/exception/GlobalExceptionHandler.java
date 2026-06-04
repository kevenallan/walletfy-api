package br.com.walletfy.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.walletfy.dto.ErrorResponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponseDTO> tratarErroGenerico(
	        Exception ex) {

	    ErrorResponseDTO erro = ErrorResponseDTO.builder()
	            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
	            .mensagem("Erro inesperado. Tente novamente mais tarde.")
	            .dataHora(LocalDateTime.now())
	            .build();
	    ex.printStackTrace();
	    return ResponseEntity
	            .status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body(erro);
	}

	//EXCEPTION DOS @VALIDATION @Valid @NotEmpty
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponseDTO> tratarValidacao(
	        MethodArgumentNotValidException ex) {

	    String mensagem = ex.getBindingResult()
	            .getFieldError()
	            .getDefaultMessage();

	    ErrorResponseDTO erro = ErrorResponseDTO.builder()
	            .status(HttpStatus.BAD_REQUEST.value())
	            .mensagem(mensagem)
	            .dataHora(LocalDateTime.now())
	            .build();

	    return ResponseEntity.badRequest().body(erro);
	}

	 @ExceptionHandler(RegraNegocioException.class)
	    public ResponseEntity<ErrorResponseDTO> tratarRegraNegocio(
	            RegraNegocioException ex) {

		 ErrorResponseDTO erro = ErrorResponseDTO.builder()
		            .status(HttpStatus.BAD_REQUEST.value())
		            .mensagem(ex.getMessage())
		            .dataHora(LocalDateTime.now())
		            .build();
		 return ResponseEntity.badRequest().body(erro);
	    }
}
