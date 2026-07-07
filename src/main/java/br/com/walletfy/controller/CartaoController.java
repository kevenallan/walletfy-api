package br.com.walletfy.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.walletfy.dto.CartaoRequestDTO;
import br.com.walletfy.dto.CartaoResponseDTO;
import br.com.walletfy.security.SecurityUtils;
import br.com.walletfy.service.CartaoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cartao")
@RequiredArgsConstructor
public class CartaoController {
	
	private final CartaoService cartaoService;
	
	private Long usuarioId() {
	    return SecurityUtils.getUsuarioIdAutenticado();
	}
	
	@PostMapping
	public ResponseEntity<CartaoResponseDTO> cadastrar(CartaoRequestDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.cartaoService.cadastrar(this.usuarioId(), dto));
	}
	
	@GetMapping
	public ResponseEntity<List<CartaoResponseDTO>> listar() {
		return ResponseEntity.ok(this.cartaoService.listar(this.usuarioId()));
	}
}
