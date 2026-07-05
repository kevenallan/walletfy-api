package br.com.walletfy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.walletfy.entity.Banco;
import br.com.walletfy.exception.RegraNegocioException;
import br.com.walletfy.repository.BancoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BancoService {
	
	private final BancoRepository bancoRepository;

	public Banco getReference(Long bancoId) {
		return this.bancoRepository.findById(bancoId).orElseThrow(
				() -> new RegraNegocioException("Banco não encontrado"));
	}
	
	public List<Banco> listar() {
		return this.bancoRepository.findAll();
	}
	
}
