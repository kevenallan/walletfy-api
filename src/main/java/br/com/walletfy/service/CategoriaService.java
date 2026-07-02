package br.com.walletfy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.walletfy.dto.CategoriaRequestDTO;
import br.com.walletfy.dto.CategoriaResponseDTO;
import br.com.walletfy.dto.CategoriaResumoResponseDTO;
import br.com.walletfy.entity.Categoria;
import br.com.walletfy.entity.Usuario;
import br.com.walletfy.enums.TipoCategoria;
import br.com.walletfy.exception.RegraNegocioException;
import br.com.walletfy.mapper.CategoriaMapper;
import br.com.walletfy.repository.CategoriaRespository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaService {

	
	private final UsuarioService usuarioService;
	
	private final CategoriaMapper categoriaMapper;
	
	private final CategoriaRespository categoriaRepository;

	public CategoriaResponseDTO cadastrar(Long usuarioId, CategoriaRequestDTO dto) {
		
		this.usuarioService.buscarPorId(usuarioId);
		
		Categoria categoria = Categoria.builder()
				.usuarioId(usuarioId)
				.nome(dto.getNome())
				.tipo(dto.getTipo())
				.cor(dto.getCor())
				.icone(dto.getIcone())
				.ativo(dto.getAtivo())
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
    
    public CategoriaResponseDTO buscarPorId(Long usuarioId, Long categoriaId) {
    	return this.categoriaMapper.toResponseDTO(this.categoriaRepository.findByUsuarioIdAndId(usuarioId, categoriaId).orElseThrow(
    			() ->  new RegraNegocioException("Categoria não encontrada")));
    }
    
    public CategoriaResponseDTO atualizar(Long usuarioId, CategoriaRequestDTO categoria) {
    	Usuario usuario = this.usuarioService.getReference(usuarioId);
    	Categoria categoriaExistente = this.categoriaRepository.findByUsuarioIdAndId(usuario.getId(), categoria.getId())
    			.orElseThrow(() -> new RegraNegocioException("Categoria não encontrada"));

        categoriaExistente.setNome(categoria.getNome());
        categoriaExistente.setTipo(categoria.getTipo());
        categoriaExistente.setCor(categoria.getCor());
        categoriaExistente.setIcone(categoria.getIcone());
        categoriaExistente.setAtivo(categoria.getAtivo());

    	Categoria categoriaAtualizada = this.categoriaRepository.save(categoriaExistente);
    	return categoriaMapper.toResponseDTO(categoriaAtualizada);
    }
    
    public void deletar(Long usuarioId, Long categoriaId) {
        Categoria categoria = this.categoriaRepository.findByUsuarioIdAndId(usuarioId, categoriaId)
                .orElseThrow(() -> new RegraNegocioException("Categoria não encontrada"));
        this.categoriaRepository.delete(categoria);
    }
    
	public Categoria getReference(Long categoriaId) {
		return this.categoriaRepository.findById(categoriaId).orElseThrow(
				() -> new RegraNegocioException("Categoria não encontrada"));
	}
	
	public void cadastrarCategoriasPadrao(Long usuarioId) {
	    List<Categoria> categorias = List.of(

	        montarCategoria(usuarioId, "Alimentação", TipoCategoria.DESPESA, "pi-shopping-cart", "#f97316"),
	        montarCategoria(usuarioId, "Transporte",  TipoCategoria.DESPESA, "pi-car",           "#3b82f6"),
	        montarCategoria(usuarioId, "Moradia",     TipoCategoria.DESPESA, "pi-home",          "#f59e0b"),
	        montarCategoria(usuarioId, "Compras",     TipoCategoria.DESPESA, "pi-shopping-bag",  "#9333ea"),
	        montarCategoria(usuarioId, "Saúde",       TipoCategoria.DESPESA, "pi-heart",         "#22c55e"),
	        montarCategoria(usuarioId, "Lazer",       TipoCategoria.DESPESA, "pi-ticket",        "#ec4899"),
	        montarCategoria(usuarioId, "Outro",       TipoCategoria.DESPESA, "pi-th-large",      "#6b7280"),

	        montarCategoria(usuarioId, "Salário",       TipoCategoria.RECEITA, "pi-briefcase",  "#16a34a"),
	        montarCategoria(usuarioId, "Freelance",     TipoCategoria.RECEITA, "pi-file",       "#0ea5e9"),
	        montarCategoria(usuarioId, "Investimentos", TipoCategoria.RECEITA, "pi-chart-bar",  "#7c3aed"),
	        montarCategoria(usuarioId, "Aluguel",       TipoCategoria.RECEITA, "pi-home",       "#f59e0b"),
	        montarCategoria(usuarioId, "Outro",         TipoCategoria.RECEITA, "pi-th-large",   "#6b7280")
	    );

	    this.categoriaRepository.saveAll(categorias);
	}

	private Categoria montarCategoria(Long usuarioId, String nome, TipoCategoria tipo, String icone, String cor) {
	    return Categoria.builder()
	            .usuarioId(usuarioId)
	            .nome(nome)
	            .tipo(tipo)
	            .icone(icone)
	            .cor(cor)
	            .ativo(true)
	            .build();
	}
	
	public CategoriaResumoResponseDTO gerarResumo(Long usuarioId, TipoCategoria tipo) {
		this.usuarioService.buscarPorId(usuarioId);
		
		return this.categoriaRepository.getResumo(usuarioId, tipo.toString());
	}

}
