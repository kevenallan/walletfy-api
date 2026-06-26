package br.com.walletfy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.walletfy.dto.CategoriaRequestDTO;
import br.com.walletfy.dto.CategoriaResponseDTO;
import br.com.walletfy.entity.Categoria;
import br.com.walletfy.enums.TipoCategoria;
import br.com.walletfy.entity.Usuario;
import br.com.walletfy.exception.RegraNegocioException;
import br.com.walletfy.mapper.CategoriaMapper;
import br.com.walletfy.repository.CategoriaRespository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaService {

	private final CategoriaRespository categoriaRepository;
	
	private final UsuarioService usuarioService;
	
	private final CategoriaMapper categoriaMapper;
	
	public CategoriaResponseDTO cadastrar(Long usuarioId, CategoriaRequestDTO dto) {
		
		this.usuarioService.buscarPorId(usuarioId);
		
		Categoria categoria = Categoria.builder()
				.usuarioId(usuarioId)
				.nome(dto.getNome())
				.tipo(dto.getTipo())
				.cor(dto.getCor())
				.icone(dto.getIcone())
				.ativo(true)
				.build();

		this.categoriaRepository.save(categoria);
		return categoriaMapper.toResponseDTO(categoria);
	}
	
	public List<CategoriaResponseDTO> listar(Long usuarioId, TipoCategoria tipo) {
		this.usuarioService.buscarPorId(usuarioId);
		
        return this.categoriaRepository.findByUsuarioIdAndTipo(usuarioId, tipo)
                .stream()
                .map(categoriaMapper::toResponseDTO)
                .toList();
	}
	

    public List<CategoriaResponseDTO> listarAtivos(Long usuarioId, TipoCategoria tipo) {

        return categoriaRepository
                .findByUsuarioIdAndTipoAndAtivo(usuarioId, tipo, true)
                .stream()
                .map(categoriaMapper::toResponseDTO)
                .toList();
    }
    
    public CategoriaResponseDTO atualizar(Long usuarioId, CategoriaRequestDTO categoria) {
    	Usuario usuario = this.usuarioService.getReference(usuarioId);
    	Categoria categoriaExistente = this.categoriaRepository.findByUsuarioIdAndId(usuario.getId(), categoria.getId())
    			.orElseThrow(() -> new RegraNegocioException("Categoria não encontrada"));

        categoriaExistente.setNome(categoria.getNome());
        categoriaExistente.setCor(categoria.getCor());
        categoriaExistente.setIcone(categoria.getIcone());
        categoriaExistente.setAtivo(categoria.getAtivo());

    	Categoria categoriaAtualizada = this.categoriaRepository.save(categoriaExistente);
    	return categoriaMapper.toResponseDTO(categoriaAtualizada);
    }
    
	public Categoria getReference(Long categoriaId) {
		return this.categoriaRepository.findById(categoriaId).orElseThrow(
				() -> new RegraNegocioException("Categoria não encontrada"));
	}
	
	public void cadastrarCategoriasPadrao(Long usuarioId) {
		 List<CategoriaRequestDTO> categoriasPadrao = List.of(
	        // DESPESA
	        new CategoriaRequestDTO("Alimentação", TipoCategoria.DESPESA, "pi-shopping-cart", "#f97316", true),
	        new CategoriaRequestDTO("Transporte", TipoCategoria.DESPESA, "pi-car", "#3b82f6", true),
	        new CategoriaRequestDTO("Moradia", TipoCategoria.DESPESA, "pi-home", "#f59e0b", true),
	        new CategoriaRequestDTO("Compras", TipoCategoria.DESPESA, "pi-shopping-bag", "#9333ea", true),
	        new CategoriaRequestDTO("Saúde", TipoCategoria.DESPESA, "pi-heart", "#22c55e", true),
	        new CategoriaRequestDTO("Lazer", TipoCategoria.DESPESA, "pi-ticket", "#ec4899", true),
	        new CategoriaRequestDTO("Outro", TipoCategoria.DESPESA, "pi-th-large", "#6b7280", true),

	        // RECEITA
	        new CategoriaRequestDTO("Salário", TipoCategoria.RECEITA, "pi-briefcase", "#16a34a", true),
	        new CategoriaRequestDTO("Freelance", TipoCategoria.RECEITA, "pi-file", "#0ea5e9", true),
	        new CategoriaRequestDTO("Investimentos", TipoCategoria.RECEITA, "pi-chart-bar", "#7c3aed", true),
	        new CategoriaRequestDTO("Aluguel", TipoCategoria.RECEITA, "pi-home", "#f59e0b", true),
	        new CategoriaRequestDTO("Outro", TipoCategoria.RECEITA, "pi-th-large", "#6b7280", true)
	    );

	    categoriasPadrao.forEach(dto -> this.cadastrar(usuarioId, dto));
	}

}
