package br.com.walletfy.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GastoRequestDTO {

	@NotNull
	private Long categoriaId;
	
	@NotNull
	private Long formaPagamentoId;

	@NotNull
	private Long statusId;

	@NotBlank
	private String descricao;
	
	@NotNull
	private BigDecimal valor;
	
	@NotNull
	private LocalDate dataGasto;
	
	private LocalDate dataVencimento;
	
}
