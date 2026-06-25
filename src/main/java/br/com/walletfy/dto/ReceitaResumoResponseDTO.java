package br.com.walletfy.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceitaResumoResponseDTO {
    String mes;
    BigDecimal receitas;
    BigDecimal gastos;
    BigDecimal saldo;
    Long pendentes;
    BigDecimal valorPendente;
    String principalCategoria;
    BigDecimal valorPrincipalCategoria;
    BigDecimal percentualPrincipalCategoria;
    BigDecimal variacaoReceitas;
    BigDecimal variacaoSaldo;
}