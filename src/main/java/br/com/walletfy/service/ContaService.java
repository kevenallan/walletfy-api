
package br.com.walletfy.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.walletfy.dto.ContaEdicaoRequestDTO;
import br.com.walletfy.dto.ContaRequestDTO;
import br.com.walletfy.dto.ContaResponseDTO;
import br.com.walletfy.entity.Banco;
import br.com.walletfy.entity.Conta;
import br.com.walletfy.entity.Usuario;
import br.com.walletfy.exception.RegraNegocioException;
import br.com.walletfy.mapper.ContaMapper;
import br.com.walletfy.repository.ContaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContaService {

	private final ContaRepository contaRepository;
	private final ContaMapper contaMapper;
	private final UsuarioService usuarioService;
	private final BancoService bancoService;
	
	public ContaResponseDTO cadastrar(Long usuarioId, ContaRequestDTO dto) {
		Usuario usuario = this.usuarioService.getReference(usuarioId);
		
		Banco banco = null;
		
		if(dto.getBancoId() != null) {
			banco = this.bancoService.getReference(dto.getBancoId());
			
		}
		
		Conta conta = Conta.builder() 
					  .usuario(usuario)
					  .banco(banco)
					  .nome(dto.getNome())
					  .tipo(dto.getTipo())
					  .saldoInicial(dto.getSaldoInicial() != null ? dto.getSaldoInicial() : new BigDecimal(0))
					  .ativo(true)
					  .build();
		
		conta = this.contaRepository.save(conta);
		
		return contaMapper.toResponseDTO(conta);
	}
	
	public Conta getReference(Long contaId) {
		return this.contaRepository.findById(contaId).orElseThrow(
				() -> new RegraNegocioException("Conta não encontrada"));
	}
	
   public List<ContaResponseDTO> listar(Long usuarioId) {
        return contaRepository.listarContasUsuario(usuarioId).stream()
            .map(contaMapper::toResponseDTO)
            .toList();
    }
   
   public ContaResponseDTO detalhar(Long contaId) {
	   Conta conta = this.getReference(contaId);
	   
	   conta.setSaldoInicial(null);
	   
	   return contaMapper.toResponseDTO(conta);
   }
   
   public void atualizar(Long usuarioId, ContaEdicaoRequestDTO dto) {
	   Conta conta = this.getReference(dto.getId());
	   if (dto.getBancoId() != null) {
		   Banco banco = this.bancoService.getReference(dto.getBancoId());
		   conta.setBanco(banco); 
	   }
	   
	   conta.setNome(dto.getNome());
	   conta.setTipo(dto.getTipo());
	   
	  this.contaRepository.save(conta);
   }
   
   public void deletar(Long contaId) {
	   Conta conta = this.getReference(contaId);
	   conta.setAtivo(false);
	   this.contaRepository.save(conta);
   }

}
