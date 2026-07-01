package br.com.walletfy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.walletfy.dto.ResumoAnualResponseDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

	private final GastoService gastoService;
	
	public List<ResumoAnualResponseDTO> gerarResumoAnual(Long usuarioId, int ano){
		return this.gastoService.gerarResumoAnual(usuarioId, ano);
	}
}
