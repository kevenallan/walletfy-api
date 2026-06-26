package br.com.walletfy.service;

import org.springframework.stereotype.Service;

import br.com.walletfy.dto.LoginDTO;
import br.com.walletfy.dto.AuthResponseDTO;
import br.com.walletfy.dto.UsuarioRequestDTO;
import br.com.walletfy.entity.Usuario;
import br.com.walletfy.exception.RegraNegocioException;
import br.com.walletfy.mapper.UsuarioMapper;
import br.com.walletfy.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;
	
	public AuthResponseDTO cadastrar(UsuarioRequestDTO dto) {
		if (this.usuarioRepository.existsByEmail(dto.getEmail())) {
			throw new RegraNegocioException("E-mail já cadastrado");
		}
		
		Usuario usuario = Usuario.builder()
				.nome(dto.getNome())
				.email(dto.getEmail())
				.senha(dto.getSenha())
				.ativo("S")
				.build();
		
		Usuario usuarioCadastrado = this.usuarioRepository.save(usuario);

		return UsuarioMapper.toResponseDTO(usuarioCadastrado);
	}
	
	public AuthResponseDTO login(LoginDTO dto) {
		
		Usuario usuario = this.usuarioRepository.findByEmail(dto.getEmail())
				.orElseThrow(() -> new RegraNegocioException("Email não cadastrado"));
		
		if (!usuario.getSenha().equals(dto.getSenha())) {
			throw new RegraNegocioException("Senha incorreta");
		}

		return AuthResponseDTO.builder()
	            .id(usuario.getId())
	            .nome(usuario.getNome())
	            .email(usuario.getEmail())
	            .build();
	}

	public Usuario getReference(Long usuarioId) {
		return this.usuarioRepository.findById(usuarioId).orElseThrow(
				() -> new RegraNegocioException("Usuario não encontrado"));
	}
	
	public AuthResponseDTO buscarPorId(Long usuarioId) {
		Usuario usuario = this.usuarioRepository.findById(usuarioId).orElseThrow(
				() -> new RegraNegocioException("Usuario não encontrado"));
		return UsuarioMapper.toResponseDTO(usuario);
	}
}
