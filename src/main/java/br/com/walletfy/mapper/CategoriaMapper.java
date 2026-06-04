package br.com.walletfy.mapper;

import org.mapstruct.Mapper;

import br.com.walletfy.dto.CategoriaResponseDTO;
import br.com.walletfy.entity.Categoria;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

	CategoriaResponseDTO toResponseDTO(Categoria categoria);
}
