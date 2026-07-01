package br.com.walletfy.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumoAnualResponseDTO {

	private String mes;
	private BigDecimal totalReceitas;
	private BigDecimal totalGastos;
	private BigDecimal saldo;
	private String categoriaMaisUtilizada;
	private String iconeCategoriaMaisUtilizada;
	private String corCategoriaMaisUtilizada;
	private Long quantidadeCategoria;
	private BigDecimal totalGastoGategoria;
	private String categoriaMaiorGasto;
	private String iconeCategoriaMaiorGasto;
	private String corCategoriaMaiorGasto;
	private BigDecimal totalCategoriaMaiorGasto;

}
