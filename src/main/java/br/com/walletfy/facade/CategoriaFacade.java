package br.com.walletfy.facade;

import org.springframework.stereotype.Service;

import br.com.walletfy.dto.CategoriaResponseDTO;
import br.com.walletfy.enums.TipoCategoria;
import br.com.walletfy.exception.RegraNegocioException;
import br.com.walletfy.service.CategoriaService;
import br.com.walletfy.service.GastoService;
import br.com.walletfy.service.ReceitaService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaFacade {

	private final CategoriaService categoriaService;
	private final GastoService gastoService;
	private final ReceitaService receitaService;
	
	public void deletar(Long usuarioId, Long categoriaId, TipoCategoria tipo) {
		CategoriaResponseDTO categoriaResponseDTO = this.categoriaService.buscarPorId(usuarioId, categoriaId);

        switch (tipo) {
	        case DESPESA -> {
	            Long totalGastos = this.gastoService.getQuantidadeCategoriaGasto(usuarioId, categoriaId);
	            if (totalGastos > 0) {
	                throw new RegraNegocioException(
	                    "Não é possível excluir. Existem " + totalGastos + " gasto(s) vinculado(s) a essa categoria.");
	            }
	        }
	        case RECEITA -> {
	            Long totalReceitas = this.receitaService.getQuantidadeCategoriaReceita(usuarioId, categoriaId);
	            if (totalReceitas > 0) {
	                throw new RegraNegocioException(
	                    "Não é possível excluir. Existem " + totalReceitas + " receita(s) vinculada(s) a essa categoria.");
	            }
	        }
	        default -> throw new RegraNegocioException("Tipo de categoria inválido");
	    }
        this.categoriaService.deletar(usuarioId, categoriaResponseDTO.getId());
	}
}
