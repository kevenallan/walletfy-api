package br.com.walletfy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.walletfy.entity.Banco;
import br.com.walletfy.service.BancoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/banco")
@RequiredArgsConstructor
public class BancoController {

	private final BancoService bancoService;
	
	@GetMapping
	public ResponseEntity<List<Banco>> listar() {
		return ResponseEntity.ok(this.bancoService.listar());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Banco> buscarPorId(@PathVariable Long bancoId) {
		return ResponseEntity.ok(this.bancoService.buscarPorId(bancoId));
	}
}
