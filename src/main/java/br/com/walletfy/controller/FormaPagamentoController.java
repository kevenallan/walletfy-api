package br.com.walletfy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.walletfy.entity.FormaPagamento;
import br.com.walletfy.service.FormaPagamentoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/formas-pagamento")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class FormaPagamentoController {

	private final FormaPagamentoService formaPagamentoService;
	
	@GetMapping
	public ResponseEntity<List<FormaPagamento>> listar(){
		return ResponseEntity.ok(this.formaPagamentoService.listar());
	}
}
