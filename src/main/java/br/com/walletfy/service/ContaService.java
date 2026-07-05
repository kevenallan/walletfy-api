package br.com.walletfy.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.walletfy.dto.ContaRequestDTO;
import br.com.walletfy.dto.ContaResponseDTO;
import br.com.walletfy.entity.Banco;
import br.com.walletfy.entity.Conta;
import br.com.walletfy.entity.Usuario;
import br.com.walletfy.mapper.ContaMapper;
import br.com.walletfy.repository.ContaRepository;
import br.com.walletfy.repository.GastoRepository;
import br.com.walletfy.repository.ReceitaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContaService {

	private final ContaRepository contaRepository;
	private final ContaMapper contaMapper;
	private final UsuarioService usuarioService;
	private final BancoService bancoService;
	private final ReceitaRepository receitaRepository;
	private final GastoRepository gastoRepository;
	
	public ContaResponseDTO cadastrar(Long usuarioId, ContaRequestDTO dto) {
		Usuario usuario = this.usuarioService.getReference(usuarioId);
		Banco banco = this.bancoService.getReference(dto.getBancoId());
		
		Conta conta = Conta.builder() 
					  .usuario(usuario)
					  .banco(banco)
					  .nome(dto.getNome())
					  .tipo(dto.getTipo())
					  .icone(dto.getIcone())
					  .saldoInicial(dto.getSaldoInicial())
					  .ativo(true)
					  .build();
		
		conta = this.contaRepository.save(conta);
		
		return contaMapper.toResponseDTO(conta);
	}
	
	
}
