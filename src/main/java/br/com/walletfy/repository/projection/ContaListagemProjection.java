package br.com.walletfy.repository.projection;

import java.math.BigDecimal;

public interface ContaListagemProjection {
    Long getId();
    String getNome();
    String getTipoConta();
    BigDecimal getSaldo();
    Boolean getAtivo();
    Long getBancoId();
    String getBancoNome();
    String getBancoIcone();
    String getBancoCor();
}
