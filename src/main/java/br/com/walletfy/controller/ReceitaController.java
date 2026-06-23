package br.com.walletfy.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.walletfy.dto.ReceitaRequestDTO;
import br.com.walletfy.dto.ReceitaResponseDTO;
import br.com.walletfy.service.ReceitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/receita")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ReceitaController {

	private final ReceitaService receitaService;
	
	@PostMapping("/{usuarioId}")
	public ResponseEntity<ReceitaResponseDTO> cadastrar(@PathVariable Long usuarioId, @Valid @RequestBody ReceitaRequestDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(this.receitaService.cadastrar(usuarioId, dto));
	}
	
	@GetMapping("/{usuarioId}")
	public ResponseEntity<List<ReceitaResponseDTO>> listar(@PathVariable Long usuarioId, @RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim) {
		return ResponseEntity.ok(this.receitaService.listar(usuarioId, dataInicio, dataFim));
	}
	
	@GetMapping
	public ResponseEntity<ReceitaResponseDTO> detalhar(@RequestParam Long receitaId, @RequestParam Long usuarioId) {
	    return ResponseEntity.ok(
	            this.receitaService.detalhar(receitaId, usuarioId));
	}

	@PutMapping("/{usuarioId}")
	public ResponseEntity<ReceitaResponseDTO> atualizar(@PathVariable Long usuarioId, @Valid @RequestBody ReceitaRequestDTO dto) {
		return ResponseEntity.ok(this.receitaService.atualizar(usuarioId, dto));
	}
	
	@DeleteMapping("/{usuarioId}/{receitaId}")
	public ResponseEntity<Void> deletar(@PathVariable Long usuarioId, @PathVariable Long receitaId){
		this.receitaService.deletar(usuarioId, receitaId);
		return ResponseEntity.noContent().build();
	}

}
