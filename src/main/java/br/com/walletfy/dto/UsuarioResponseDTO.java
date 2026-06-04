package br.com.walletfy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UsuarioResponseDTO {

	private Long id;
	private String nome;
	private String email;
	
}
