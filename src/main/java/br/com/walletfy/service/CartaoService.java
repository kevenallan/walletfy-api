package br.com.walletfy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.walletfy.dto.CartaoRequestDTO;
import br.com.walletfy.dto.CartaoResponseDTO;
import br.com.walletfy.entity.Cartao;
import br.com.walletfy.entity.Conta;
import br.com.walletfy.entity.Usuario;
import br.com.walletfy.exception.RegraNegocioException;
import br.com.walletfy.mapper.CartaoMapper;
import br.com.walletfy.repository.CartaoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartaoService {

	private final CartaoRepository cartaoRepository;
	private final CartaoMapper cartaoMapper;
	private final UsuarioService usuarioService;
	private final ContaService contaService;
	
	public CartaoResponseDTO cadastrar(Long usuarioId, CartaoRequestDTO dto){
		Usuario usuario = this.usuarioService.getReference(usuarioId);
		Conta conta = this.contaService.getReference(dto.getContaId()); 
		
		Cartao cartao = Cartao.builder()
						.usuario(usuario)
						.conta(conta)
						.nome(dto.getNome())
						.diaVencimento(dto.getDiaVencimento())
						.diaFechamento(dto.getDiaFechamento())
						.limite(dto.getLimite())
						.ativo(true)
						.build();
		
		cartao = this.cartaoRepository.save(cartao);
		return cartaoMapper.toResponseDTO(cartao);
	}
	
	public Cartao getReference(Long cartaoId) {
		return this.cartaoRepository.findById(cartaoId).orElseThrow(
				() -> new RegraNegocioException("Cartão não encontrado"));
	}
	
	public List<CartaoResponseDTO> listar(Long usuarioId) {
		Usuario usuario = this.usuarioService.getReference(usuarioId);
		
		return this.cartaoRepository.findByUsuarioAndAtivo(usuario, true).stream().map(cartaoMapper::toResponseDTO).toList();
	}
}
