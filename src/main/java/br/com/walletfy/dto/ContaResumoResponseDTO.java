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
public class ContaResumoResponseDTO {
	private BigDecimal saldoTotal;
	private String contaMovimentacaoNome;
	private Long contaMovimentacaoQtd;
	private String contaMaiorSaldoNome;
	private BigDecimal contaMaiorSaldoValor;
	private BigDecimal totalPoupanca;
}
