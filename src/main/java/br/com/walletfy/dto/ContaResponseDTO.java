package br.com.walletfy.dto;

import java.math.BigDecimal;

import br.com.walletfy.entity.Banco;
import br.com.walletfy.enums.TipoConta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContaResponseDTO {
	private Long id;
	private String nome;
	private TipoConta tipo;
	private BigDecimal saldo;
	private boolean ativo;
	private Banco banco;
}
