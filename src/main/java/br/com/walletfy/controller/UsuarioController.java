package br.com.walletfy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.walletfy.dto.LoginDTO;
import br.com.walletfy.dto.AuthResponseDTO;
import br.com.walletfy.dto.UsuarioRequestDTO;
import br.com.walletfy.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

	private final UsuarioService usuarioService;

	@PostMapping("/cadastrar")
	public ResponseEntity<AuthResponseDTO> cadastrar(@Valid @RequestBody UsuarioRequestDTO dto) {
		return ResponseEntity.ok(this.usuarioService.cadastrar(dto));
	}

	@PostMapping
	public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginDTO dto) {
		return ResponseEntity.ok(this.usuarioService.login(dto));
	}
}
