package br.com.walletfy.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.walletfy.dto.UsuarioEdicaoRequestDTO;
import br.com.walletfy.dto.UsuarioResponseDTO;
import br.com.walletfy.security.SecurityUtils;
import br.com.walletfy.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/usuario")
public class UsuarioController {
	
	private final UsuarioService usuarioService;

	private Long usuarioId() {
	    return SecurityUtils.getUsuarioIdAutenticado();
	}
	
	@PutMapping
    public ResponseEntity<UsuarioResponseDTO> atualizar(@Valid @RequestBody UsuarioEdicaoRequestDTO usuario) {
        return  ResponseEntity.status(HttpStatus.OK)
        		.body(this.usuarioService.atualizar(this.usuarioId(), usuario));
    }
}
