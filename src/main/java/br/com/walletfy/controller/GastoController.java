package br.com.walletfy.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import br.com.walletfy.dto.GastoResumoResponseDTO;
import br.com.walletfy.security.SecurityUtils;
import br.com.walletfy.service.GastoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/gasto")
@RequiredArgsConstructor
public class GastoController {

	private final GastoService gastoService;
	
	private Long usuarioId() {
	    return SecurityUtils.getUsuarioIdAutenticado();
	}
	
	@PostMapping
	public ResponseEntity<GastoResponseDTO> cadastrar(@Valid @RequestBody GastoRequestDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(this.gastoService.cadastrar(this.usuarioId(), dto));
	}
	
	@GetMapping
	public ResponseEntity<List<GastoResponseDTO>> listar(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim) {
	    return ResponseEntity.ok(
	            this.gastoService.listar(this.usuarioId(), dataInicio, dataFim));
	}
	
	@GetMapping("/{gastoId}")
	public ResponseEntity<GastoResponseDTO> detalhar(@PathVariable Long gastoId) {
	    return ResponseEntity.ok(
	            this.gastoService.detalhar(gastoId, this.usuarioId()));
	}
	
	@GetMapping("/resumo")
    public ResponseEntity<List<GastoResumoResponseDTO>> resumo() {
        return ResponseEntity.ok(gastoService.gerarResumo(this.usuarioId()));
    }
	
	@PutMapping
	public ResponseEntity<GastoResponseDTO> atualizar(@Valid @RequestBody GastoRequestDTO dto) {
		return ResponseEntity.ok(
				this.gastoService.atualizar(this.usuarioId(), dto));
	}
	
	@DeleteMapping("/{gastoId}")
	public ResponseEntity<Void> deletar(@PathVariable Long gastoId) {
		this.gastoService.deletar(this.usuarioId(), gastoId);
		return ResponseEntity.noContent().build();
	}

}
