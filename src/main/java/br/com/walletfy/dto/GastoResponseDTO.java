package br.com.walletfy.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.walletfy.entity.Categoria;
import br.com.walletfy.entity.FormaPagamento;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GastoResponseDTO {

	private Long id;

	private Categoria categoria;
	
	private FormaPagamento formaPagamento;
	
	private String descricao;
	
	private String observacao;
	
	private BigDecimal valor;
	
	private LocalDate dataGasto;
	
	private LocalDate dataVencimento;
	
	private String status;

}
