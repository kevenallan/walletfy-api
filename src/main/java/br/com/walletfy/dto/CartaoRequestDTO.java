package br.com.walletfy.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartaoRequestDTO {
	private Long contaId;
	private String nome;
	private BigDecimal limite;
	private Long diaFechamento;
	private Long diaVencimento;
}
