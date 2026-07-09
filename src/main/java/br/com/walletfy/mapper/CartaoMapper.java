package br.com.walletfy.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.walletfy.dto.CartaoResponseDTO;
import br.com.walletfy.entity.Banco;
import br.com.walletfy.entity.Cartao;
import br.com.walletfy.repository.projection.CartaoListagemProjection;

@Mapper(componentModel = "spring")
public interface CartaoMapper {
	
    @Mapping(target = "banco", expression = "java(toBancoDaConta(cartao))")
    @Mapping(target = "faturaAtual", constant = "0")
    @Mapping(target = "limiteDisponivel", source = "limite")
    @Mapping(target = "percentualUsado", constant = "0")
    CartaoResponseDTO toResponseDTO(Cartao cartao);
	
	@Mapping(target = "banco", expression = "java(toBanco(projection))")
	CartaoResponseDTO toResponseDTO(CartaoListagemProjection projection);

	default Banco toBanco(CartaoListagemProjection projection) {
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
	
	default Banco toBancoDaConta(Cartao cartao) {
        Banco banco = cartao.getConta().getBanco();
        return banco != null ? banco : null;
    }
}
