package br.com.walletfy.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.walletfy.dto.GastoRequestDTO;
import br.com.walletfy.dto.GastoResponseDTO;
import br.com.walletfy.service.GastoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/gasto")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class GastoController {

	private final GastoService gastoService;
	
	@PostMapping("/{usuarioId}")
	public ResponseEntity<GastoResponseDTO> cadastrar(@PathVariable Long usuarioId, @Valid @RequestBody GastoRequestDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(this.gastoService.cadastrar(usuarioId, dto));
	}
	
	@GetMapping("/{usuarioId}")
	public ResponseEntity<List<GastoResponseDTO>>
	listar(@PathVariable Long usuarioId) {

	    return ResponseEntity.ok(
	            this.gastoService.listar(usuarioId));
	}
	
	@GetMapping
	public ResponseEntity<GastoResponseDTO>
	detalhar(@RequestParam Long gastoId, @RequestParam Long usuarioId) {

	    return ResponseEntity.ok(
	            this.gastoService.detalhar(gastoId, usuarioId));
	}
	
	@PutMapping("/{usuarioId}")
	public ResponseEntity<GastoResponseDTO> atualizar(@PathVariable Long usuarioId, @Valid @RequestBody GastoRequestDTO dto) {
		return ResponseEntity.ok(
				this.gastoService.atualizar(usuarioId, dto));
	}

}
