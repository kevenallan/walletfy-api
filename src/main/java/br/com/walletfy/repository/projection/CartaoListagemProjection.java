package br.com.walletfy.repository.projection;

import java.math.BigDecimal;

public interface CartaoListagemProjection {
	
    Long getId();
    String getNome();
    BigDecimal getLimite();
    Short getDiaFechamento();
    Short getDiaVencimento();
    BigDecimal getFaturaAtual();
    BigDecimal getLimiteDisponivel();
    BigDecimal getPercentualUsado();
    Long getBancoId();
    String getBancoNome();
    String getBancoIcone();
    String getBancoCor();

}
