package br.com.walletfy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.walletfy.entity.FormaPagamento;
import br.com.walletfy.repository.FormaPagamentoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FormaPagamentoService {

	private final FormaPagamentoRepository formaPagamentoRepository;
	
	public List<FormaPagamento> listar(){
		return this.formaPagamentoRepository.findAll();
	}
}
