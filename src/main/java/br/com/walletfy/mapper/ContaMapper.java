package br.com.walletfy.mapper;

import org.mapstruct.Mapper;

import br.com.walletfy.dto.ContaResponseDTO;
import br.com.walletfy.entity.Conta;

@Mapper(componentModel = "spring")
public interface ContaMapper {

	ContaResponseDTO toResponseDTO(Conta conta);
}
