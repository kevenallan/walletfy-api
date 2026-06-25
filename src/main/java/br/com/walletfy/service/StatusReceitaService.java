package br.com.walletfy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.walletfy.entity.StatusReceita;
import br.com.walletfy.exception.RegraNegocioException;
import br.com.walletfy.repository.StatusReceitaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatusReceitaService {

	private final StatusReceitaRepository statusReceitaRepository;
	
	public List<StatusReceita> listar() {
		return this.statusReceitaRepository.findAll();
	}

	public StatusReceita getReference(Long statusReceitaId) {
		return this.statusReceitaRepository.findById(statusReceitaId).orElseThrow(() -> new RegraNegocioException("Status não encontrado"));
	}
}
