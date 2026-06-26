package br.com.walletfy.dto;

import br.com.walletfy.enums.TipoCategoria;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaRequestDTO {

	public CategoriaRequestDTO(@NotBlank(message = "Nome é obrigatorio") String nome, TipoCategoria tipo, String icone,
			String cor, Boolean ativo) {
		super();
		this.nome = nome;
		this.tipo = tipo;
		this.icone = icone;
		this.cor = cor;
		this.ativo = ativo;
	}
	
	private Long id;
	@NotBlank(message = "Nome é obrigatorio")
	private String nome;
	private TipoCategoria tipo;
	private String icone;
	private String cor;
	private Boolean ativo;
}
