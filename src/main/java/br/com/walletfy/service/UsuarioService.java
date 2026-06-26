package br.com.walletfy.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
	
	private final PasswordEncoder passwordEncoder;
	
	public Usuario cadastrar(UsuarioRequestDTO dto) {
		if (this.usuarioRepository.existsByEmail(dto.getEmail())) {
			throw new RegraNegocioException("E-mail já cadastrado");
		}
		
		Usuario usuario = Usuario.builder()
				.nome(dto.getNome())
				.email(dto.getEmail())
				.senha(passwordEncoder.encode(dto.getSenha()))
				.ativo(true)
				.build();
		
		return this.usuarioRepository.save(usuario);
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
	
	public Usuario buscarPorEmail(String email) {
		return this.usuarioRepository.findByEmail(email).orElseThrow(() -> new RegraNegocioException("Usuario não encontrado"));
	}
}
