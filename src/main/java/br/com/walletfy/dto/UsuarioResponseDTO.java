package br.com.walletfy.dto;

import java.sql.Blob;
import java.time.LocalDate;

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
public class UsuarioResponseDTO {
	private String nome;
	private String email;
	private String telefone;
	private LocalDate dataNascimento;
	private String fotoUrl;
}
