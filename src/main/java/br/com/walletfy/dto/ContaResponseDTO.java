package br.com.walletfy.dto;

import java.math.BigDecimal;

import br.com.walletfy.enums.TipoConta;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContaResponseDTO {
	private Long id;
	private String nome;
	private String icone;
	private TipoConta tipo;
	private BigDecimal saldoInicial;
	private boolean ativo;
}
