package br.com.walletfy.mapper;

import br.com.walletfy.dto.AuthResponseDTO;
import br.com.walletfy.dto.UsuarioResponseDTO;
import br.com.walletfy.entity.Usuario;

public class UsuarioMapper {

	 public static AuthResponseDTO toResponseDTO(Usuario usuario) {

	        if (usuario == null) {
	            return null;
	        }

	        return AuthResponseDTO.builder()
	                .nome(usuario.getNome())
	                .build();
	    }
	 
	 public static UsuarioResponseDTO toUsuarioResponseDTO(Usuario usuario) {
		 
			 if (usuario == null) {
		            return null;
		        }
	
	        return UsuarioResponseDTO.builder()
	                .nome(usuario.getNome())
	                .email(usuario.getEmail())
	                .telefone(usuario.getTelefone())
	                .dataNascimento(usuario.getDataNascimento())
	                .fotoUrl(usuario.getFotoUrl())	          
	                .build();
	 }
	 
}
