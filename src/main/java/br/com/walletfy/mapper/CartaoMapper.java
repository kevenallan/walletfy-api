package br.com.walletfy.mapper;

import br.com.walletfy.dto.CartaoResponseDTO;
import br.com.walletfy.entity.Cartao;

public interface CartaoMapper {
	
	CartaoResponseDTO toResponseDTO(Cartao cartao);
}
