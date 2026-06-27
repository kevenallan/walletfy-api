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

import br.com.walletfy.dto.ReceitaRequestDTO;
import br.com.walletfy.dto.ReceitaResponseDTO;
import br.com.walletfy.dto.ReceitaResumoResponseDTO;
import br.com.walletfy.security.SecurityUtils;
import br.com.walletfy.service.ReceitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/receita")
@RequiredArgsConstructor
public class ReceitaController {

	private final ReceitaService receitaService;

	private Long usuarioId() {
	    return SecurityUtils.getUsuarioIdAutenticado();
	}

	@PostMapping
	public ResponseEntity<ReceitaResponseDTO> cadastrar(@Valid @RequestBody ReceitaRequestDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(this.receitaService.cadastrar(this.usuarioId(), dto));
	}
	
	@GetMapping
	public ResponseEntity<List<ReceitaResponseDTO>> listar(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim) {
		return ResponseEntity.ok(this.receitaService.listar(this.usuarioId(), dataInicio, dataFim));
	}
	
	@GetMapping("/{receitaId}")
	public ResponseEntity<ReceitaResponseDTO> detalhar(@PathVariable Long receitaId) {
	    return ResponseEntity.ok(
	            this.receitaService.detalhar(receitaId, this.usuarioId()));
	}

	@PutMapping
	public ResponseEntity<ReceitaResponseDTO> atualizar (@Valid @RequestBody ReceitaRequestDTO dto) {
		return ResponseEntity.ok(this.receitaService.atualizar(this.usuarioId(), dto));
	}
	
	@DeleteMapping("/{receitaId}")
	public ResponseEntity<Void> deletar( @PathVariable Long receitaId){
		this.receitaService.deletar(this.usuarioId(), receitaId);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/resumo")
    public ResponseEntity<List<ReceitaResumoResponseDTO>> resumo() {
        return ResponseEntity.ok(this.receitaService.gerarResumo(this.usuarioId()));
    }

}
