package br.com.walletfy.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {

	@NotBlank(message = "Email é obrigatório")
	private String email;
	@NotBlank(message = "Senha é obrigatório")
	private String senha;
}
