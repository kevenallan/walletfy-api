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
public class GastoResumoResponseDTO {
	String mes;
	BigDecimal receita;
	BigDecimal despesas;
	BigDecimal saldo;
	Long pendentes;
	BigDecimal variacaoReceita;
	BigDecimal variacaoDespesas;
	BigDecimal variacaoSaldo;
}
