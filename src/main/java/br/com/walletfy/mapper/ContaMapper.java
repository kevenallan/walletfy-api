package br.com.walletfy.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.walletfy.dto.ContaResponseDTO;
import br.com.walletfy.entity.Banco;
import br.com.walletfy.entity.Conta;
import br.com.walletfy.repository.projection.ContaListagemProjection;

@Mapper(componentModel = "spring")
public interface ContaMapper {

	@Mapping(target = "saldo", source = "saldoInicial")
    ContaResponseDTO toResponseDTO(Conta conta);
	
	@Mapping(target = "tipo", source = "tipoConta")
	@Mapping(target = "banco", expression = "java(toBanco(projection))")
	ContaResponseDTO toResponseDTO(ContaListagemProjection projection);

	    default Banco toBanco(ContaListagemProjection projection) {
	        if (projection.getBancoId() == null) {
	            return null;
	        }
	        return new Banco(
	            projection.getBancoId(),
	            projection.getBancoNome(),
	            projection.getBancoIcone(),
	            projection.getBancoCor()
	        );
	    }
}
