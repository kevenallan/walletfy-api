package br.com.walletfy.dto;

import java.math.BigDecimal;

import br.com.walletfy.entity.Conta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartaoResponseDTO {
	private Long id;
	private Conta conta;
	private String nome;
	private BigDecimal limite;
	private Long diaFechamento;
	private Long diaVencimento;
}
