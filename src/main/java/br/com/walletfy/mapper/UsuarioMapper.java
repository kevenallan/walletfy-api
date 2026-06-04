package br.com.walletfy.mapper;

import br.com.walletfy.dto.UsuarioResponseDTO;
import br.com.walletfy.entity.Usuario;

public class UsuarioMapper {

	 public static UsuarioResponseDTO toResponseDTO(Usuario usuario) {

	        if (usuario == null) {
	            return null;
	        }

	        return UsuarioResponseDTO.builder()
	                .id(usuario.getId())
	                .nome(usuario.getNome())
	                .email(usuario.getEmail())
	                .build();
	    }
	 
}
