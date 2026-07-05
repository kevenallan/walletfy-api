package br.com.walletfy.dto;

import java.math.BigDecimal;

import br.com.walletfy.enums.TipoConta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContaRequestDTO {
	private Long id;
	
	@NotNull
	private Long bancoId;
	
	@NotBlank
	private String nome;
	
	@NotNull
	private TipoConta tipo;
	
	@NotNull
	private BigDecimal saldoInicial;
	
	@NotBlank
	private String icone;
}
