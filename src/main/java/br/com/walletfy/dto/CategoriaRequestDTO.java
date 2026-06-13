package br.com.walletfy.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaRequestDTO {

	private Long id;
	@NotBlank(message = "Nome é obrigatorio")
	private String nome;
	private String cor;
	private String icone;
	private Boolean ativo;
}
