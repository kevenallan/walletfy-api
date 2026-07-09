package br.com.walletfy.dto;

import java.math.BigDecimal;

import br.com.walletfy.entity.Banco;
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
    private String nome;
    private BigDecimal limite;
    private Short diaFechamento;
    private Short diaVencimento;
    private BigDecimal faturaAtual;
    private BigDecimal limiteDisponivel;
    private BigDecimal percentualUsado;
    private Banco banco;
}
