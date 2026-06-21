package br.com.walletfy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.walletfy.entity.StatusGasto;
import br.com.walletfy.service.StatusGastoService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/status-gasto")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class StatusGastoController {

	private final StatusGastoService statusGastoService;
	
	@GetMapping
	public ResponseEntity<List<StatusGasto>> listar() {
		
		return ResponseEntity.ok(this.statusGastoService.listar());
	}
}
