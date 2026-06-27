package br.com.walletfy.dto;

import br.com.walletfy.enums.TipoCategoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaRequestDTO {
	
	private Long id;
	@NotBlank(message = "Nome é obrigatorio")
	private String nome;
	@NotNull(message = "Tipo da categoria é obrigatorio")
	private TipoCategoria tipo;
	private String icone;
	private String cor;
	private Boolean ativo;
}
