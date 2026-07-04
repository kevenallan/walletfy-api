package br.com.walletfy.dto;


import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioEdicaoRequestDTO {
	private String nome;
	@Email(message = "E-mail inválido")
	private String email;
	private String telefone;
	private LocalDate dataNascimento;
	private String senhaAntiga;
	private String senhaNova;
	private String foto;
}
