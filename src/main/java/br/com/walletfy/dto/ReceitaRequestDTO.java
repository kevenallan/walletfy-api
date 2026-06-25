package br.com.walletfy.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReceitaRequestDTO {
	
	private Long id;

	@NotNull
	private Long categoriaId;
	
	@NotNull
	private Long formaPagamentoId;
	
	@NotNull
	private Long statusId;
	
	@NotNull
	private String descricao;
	
	@NotNull
	private BigDecimal valor;
	
	@NotNull
	private LocalDate dataReceita;
	
	private Boolean recorrente;
	
//	@NotNull
	private Boolean ativo;

}
