package br.com.walletfy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.walletfy.entity.StatusReceita;
import br.com.walletfy.service.StatusReceitaService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/status-receita")
@RequiredArgsConstructor
public class StatusReceitaController {

	private final StatusReceitaService statusReceitaService;
	
	@GetMapping
	public ResponseEntity<List<StatusReceita>> listar() {
		
		return ResponseEntity.ok(this.statusReceitaService.listar());
	}
}
