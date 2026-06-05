package br.com.walletfy.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioRequestDTO {

	@NotBlank(message = "Nome é obrigatório")
	private String nome;
	
	@NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
	private String email;
	
	@NotBlank(message = "Senha é obrigatória")
	private String senha;
}
