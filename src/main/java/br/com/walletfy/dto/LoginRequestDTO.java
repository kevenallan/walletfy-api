package br.com.walletfy.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {

	@NotBlank(message = "Email é obrigatório")
	private String email;
	@NotBlank(message = "Senha é obrigatório")
	private String senha;
}
