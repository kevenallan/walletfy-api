package br.com.walletfy.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.walletfy.entity.FormaPagamento;
import br.com.walletfy.entity.StatusReceita;
import lombok.Data;

@Data
public class ReceitaResponseDTO {

	private Long id;

	private CategoriaResponseDTO categoria;
	
	private FormaPagamento formaPagamento;
	
	private StatusReceita status;
	
	private String descricao;
	
	private BigDecimal valor;
	
	private LocalDate dataReceita;
	
	private Boolean recorrente;
	
	private Boolean ativo;
}
