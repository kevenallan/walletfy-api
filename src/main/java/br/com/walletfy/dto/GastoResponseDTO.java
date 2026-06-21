package br.com.walletfy.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.walletfy.entity.Categoria;
import br.com.walletfy.entity.FormaPagamento;
import br.com.walletfy.entity.StatusGasto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GastoResponseDTO {

	private Long id;

	private Categoria categoria;
	
	private FormaPagamento formaPagamento;
	
	private StatusGasto status;

	private String descricao;
	
	private BigDecimal valor;
	
	private LocalDate dataGasto;
	
	private LocalDate dataVencimento;
	

}
