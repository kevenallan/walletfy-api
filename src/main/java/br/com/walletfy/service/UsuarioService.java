package br.com.walletfy.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.walletfy.dto.AuthResponseDTO;
import br.com.walletfy.dto.UsuarioEdicaoRequestDTO;
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
	
	public UsuarioResponseDTO atualizar(Long usuarioId, UsuarioEdicaoRequestDTO dto) {
		Usuario usuarioEncontrado = this.getReference(usuarioId);
		
		if(!this.verificarSenhaAtual(usuarioEncontrado, dto)) {
			throw new RegraNegocioException("Senha atual não é igual a informada");
		}
		
		usuarioEncontrado.setNome(dto.getNome());
		usuarioEncontrado.setEmail(dto.getEmail());
		usuarioEncontrado.setTelefone(dto.getTelefone());
		usuarioEncontrado.setDataNascimento(dto.getDataNascimento());
		usuarioEncontrado.setFotoUrl(dto.getFoto());
		
		Usuario usuarioAtualizado = this.usuarioRepository.save(usuarioEncontrado);
		return UsuarioMapper.toUsuarioResponseDTO(usuarioAtualizado);
	}
	
	private boolean verificarSenhaAtual(Usuario usuarioEncontrado, UsuarioEdicaoRequestDTO dto) {
		return passwordEncoder.matches(dto.getSenhaAntiga(), usuarioEncontrado.getSenha());
	}
}
