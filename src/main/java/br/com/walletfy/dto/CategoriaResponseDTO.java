package br.com.walletfy.dto;

import br.com.walletfy.enums.TipoCategoria;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoriaResponseDTO {

	private Long id;
    private String nome;
    private TipoCategoria tipo;
    private String cor;
    private String icone;
    private Boolean ativo;

}
