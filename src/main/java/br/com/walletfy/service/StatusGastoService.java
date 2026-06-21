package br.com.walletfy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.walletfy.entity.StatusGasto;
import br.com.walletfy.exception.RegraNegocioException;
import br.com.walletfy.repository.StatusGastoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatusGastoService {

	private final StatusGastoRepository statusGastoRepository;
	
	public List<StatusGasto> listar() {
		return this.statusGastoRepository.findAll();
	}

	public StatusGasto getReference(Long statusGastoId) {
		return this.statusGastoRepository.findById(statusGastoId).orElseThrow(() -> new RegraNegocioException("Status não encontrado"));
	}
}
