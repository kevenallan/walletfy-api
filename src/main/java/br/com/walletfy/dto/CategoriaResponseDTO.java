package br.com.walletfy.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoriaResponseDTO {

	private Long id;
    private String nome;
    private String descricao;
    private String cor;
    private String icone;
    private String ativo;

}
