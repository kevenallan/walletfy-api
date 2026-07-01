package br.com.walletfy.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.walletfy.dto.ResumoAnualResponseDTO;
import br.com.walletfy.security.SecurityUtils;
import br.com.walletfy.service.DashboardService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

	private final DashboardService dashboardService;
	
	private Long usuarioId() {
	    return SecurityUtils.getUsuarioIdAutenticado();
	}

	@GetMapping
	public ResponseEntity<List<ResumoAnualResponseDTO>> gerarResumoAnual(@RequestParam int ano) {
		return ResponseEntity.ok(this.dashboardService.gerarResumoAnual(this.usuarioId(), ano));
	}
	
}
