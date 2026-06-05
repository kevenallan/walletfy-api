package br.com.walletfy.mapper;

import org.mapstruct.Mapper;

import br.com.walletfy.dto.GastoResponseDTO;
import br.com.walletfy.entity.Gasto;

@Mapper(componentModel = "spring")
public interface GastoMapper {

	GastoResponseDTO toResponseDTO(Gasto gasto);
}
