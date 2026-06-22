package br.com.walletfy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.walletfy.dto.CategoriaRequestDTO;
import br.com.walletfy.dto.CategoriaResponseDTO;
import br.com.walletfy.dto.UsuarioResponseDTO;
import br.com.walletfy.entity.Categoria;
import br.com.walletfy.enums.TipoCategoria;
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
    	UsuarioResponseDTO usuario = this.usuarioService.buscarPorId(usuarioId);
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

}
