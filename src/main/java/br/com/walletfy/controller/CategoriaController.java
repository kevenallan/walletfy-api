package br.com.walletfy.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.walletfy.dto.CategoriaRequestDTO;
import br.com.walletfy.dto.CategoriaResponseDTO;
import br.com.walletfy.enums.TipoCategoria;
import br.com.walletfy.security.SecurityUtils;
import br.com.walletfy.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/categoria")
@RequiredArgsConstructor
public class CategoriaController {

	private final CategoriaService categoriaService;

	private Long usuarioId() {
	    return SecurityUtils.getUsuarioIdAutenticado();
	}
	
	@PostMapping
    public ResponseEntity<CategoriaResponseDTO> cadastrar(@PathVariable Long usuarioId, @Valid @RequestBody CategoriaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoriaService.cadastrar(usuarioId, dto));
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listar(@RequestParam(required = true) TipoCategoria tipo) {
        return ResponseEntity.ok(
                categoriaService.listar(this.usuarioId(), tipo));
    }

    @GetMapping("/ativas")
    public ResponseEntity<List<CategoriaResponseDTO>> listarAtivos(@RequestParam(required = true) TipoCategoria tipo) {
        return ResponseEntity.ok(
                categoriaService.listarAtivos(this.usuarioId(), tipo));
    }
    
    @PutMapping
    public ResponseEntity<CategoriaResponseDTO> atualizar(@Valid @RequestBody CategoriaRequestDTO categoria) {
        return  ResponseEntity.status(HttpStatus.OK)
        		.body(this.categoriaService.atualizar(this.usuarioId(), categoria));
    }
}
