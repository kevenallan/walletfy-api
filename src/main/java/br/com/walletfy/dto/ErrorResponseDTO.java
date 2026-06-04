package br.com.walletfy.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorResponseDTO {

	private Integer status;
    private String mensagem;
    private LocalDateTime dataHora;

}
