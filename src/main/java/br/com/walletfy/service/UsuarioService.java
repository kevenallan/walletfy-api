package br.com.walletfy.service;

import org.springframework.stereotype.Service;

import br.com.walletfy.dto.LoginDTO;
import br.com.walletfy.dto.LoginResponseDTO;
import br.com.walletfy.dto.UsuarioRequestDTO;
import br.com.walletfy.dto.UsuarioResponseDTO;
import br.com.walletfy.entity.Usuario;
import br.com.walletfy.exception.RegraNegocioException;
import br.com.walletfy.mapper.UsuarioMapper;
import br.com.walletfy.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;
	
	public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto) {
		if (this.usuarioRepository.existsByEmail(dto.getEmail())) {
			throw new RegraNegocioException("E-mail já cadastrado");
		}
		
		Usuario usuario = Usuario.builder()
				.nome(dto.getNome())
				.email(dto.getEmail())
				.senha(dto.getSenha())
				.ativo("S")
				.build();

		return UsuarioMapper.toResponseDTO(this.usuarioRepository.save(usuario));
	}
	
	public LoginResponseDTO login(LoginDTO dto) {
		
		Usuario usuario = this.usuarioRepository.findByEmail(dto.getEmail())
				.orElseThrow(() -> new RegraNegocioException("Email não cadastrado"));
		
		if (!usuario.getSenha().equals(dto.getSenha())) {
			throw new RegraNegocioException("Senha incorreta");
		}

		return LoginResponseDTO.builder()
	            .id(usuario.getId())
	            .nome(usuario.getNome())
	            .email(usuario.getEmail())
	            .build();
	}

	public Usuario getReference(Long usuarioId) {
		return this.usuarioRepository.findById(usuarioId).orElseThrow(
				() -> new RegraNegocioException("Usuario não encontrado"));
	}
	
	public UsuarioResponseDTO buscarPorId(Long usuarioId) {
		Usuario usuario = this.usuarioRepository.findById(usuarioId).orElseThrow(
				() -> new RegraNegocioException("Usuario não encontrado"));
		return UsuarioMapper.toResponseDTO(usuario);
	}
}
