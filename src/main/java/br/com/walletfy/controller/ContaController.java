package br.com.walletfy.controller;

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
import org.springframework.web.bind.annotation.RestController;

import br.com.walletfy.dto.ContaEdicaoRequestDTO;
import br.com.walletfy.dto.ContaRequestDTO;
import br.com.walletfy.dto.ContaResponseDTO;
import br.com.walletfy.security.SecurityUtils;
import br.com.walletfy.service.ContaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/conta")
@RequiredArgsConstructor
public class ContaController {

	private final ContaService contaService;
	
	private Long usuarioId() {
	    return SecurityUtils.getUsuarioIdAutenticado();
	}
	
	@PostMapping
    public ResponseEntity<ContaResponseDTO> cadastrar(@Valid @RequestBody ContaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.contaService.cadastrar(this.usuarioId(), dto));
    }

    @GetMapping
    public ResponseEntity<List<ContaResponseDTO>> listar() {
        return ResponseEntity.ok(
                this.contaService.listar(this.usuarioId()));
    }
    
    @GetMapping("/{contaId}")
    public ResponseEntity<ContaResponseDTO> detalhar(@PathVariable Long contaId) {
        return ResponseEntity.ok(
                this.contaService.detalhar(contaId));
    }
    
    @PutMapping
    public ResponseEntity<Void> atualizar(@Valid @RequestBody ContaEdicaoRequestDTO dto) {
        this.contaService.atualizar(this.usuarioId(), dto);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping
    public ResponseEntity<Void> deletar(@PathVariable Long contaId) {
        this.contaService.deletar(contaId);
        return ResponseEntity.noContent().build();
    }
}
