package br.com.walletfy.mapper;

import org.mapstruct.Mapper;

import br.com.walletfy.dto.ReceitaResponseDTO;
import br.com.walletfy.entity.Receita;

@Mapper(componentModel = "spring")
public interface ReceitaMapper {

	ReceitaResponseDTO toResponseDTO(Receita receita);
}
