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
	
	@NotBlank
	private String descricao;
	
	private String observacao;
	
	@NotNull
	private BigDecimal valor;
	
	@NotNull
	private LocalDate dataGasto;
	
	private LocalDate dataVencimento;
	
	private String status;
}
