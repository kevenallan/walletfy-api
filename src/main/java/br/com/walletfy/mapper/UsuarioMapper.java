package br.com.walletfy.mapper;

import br.com.walletfy.dto.AuthResponseDTO;
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
	 
}
