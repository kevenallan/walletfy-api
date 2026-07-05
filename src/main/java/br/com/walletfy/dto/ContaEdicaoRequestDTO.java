package br.com.walletfy.dto;

import br.com.walletfy.enums.TipoConta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContaEdicaoRequestDTO {

	@NotNull
	private Long id;
	
	private Long bancoId;
	
	@NotBlank
	private String nome;
	
	@NotNull
	private TipoConta tipo;
	
	@NotNull
	private Boolean ativo;
}
