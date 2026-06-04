package br.com.walletfy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.walletfy.dto.CategoriaCadastroDTO;
import br.com.walletfy.dto.CategoriaResponseDTO;
import br.com.walletfy.entity.Categoria;
import br.com.walletfy.exception.RegraNegocioException;
import br.com.walletfy.mapper.CategoriaMapper;
import br.com.walletfy.repository.CategoriaRespository;
import br.com.walletfy.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaService {

	private final CategoriaRespository categoriaRepository;
	
	private final UsuarioRepository usuarioRepository;
	
	private final CategoriaMapper categoriaMapper;
	
	public CategoriaResponseDTO cadastrar(Long usuarioId, CategoriaCadastroDTO dto) {
		
		this.usuarioRepository.findById(usuarioId).orElseThrow(
				() -> new RegraNegocioException("Usuario não Cadastrado"));
		
		Categoria categoria = Categoria.builder()
				.usuarioId(usuarioId)
				.nome(dto.getNome())
				.descricao(dto.getDescricao())
				.cor(dto.getCor())
				.icone(dto.getIcone())
				.ativo("S")
				.build();

		this.categoriaRepository.save(categoria);
		return categoriaMapper.toResponseDTO(categoria);
	}
	
	public List<CategoriaResponseDTO> listar(Long usuarioId) {

	        return this.categoriaRepository.findByUsuarioId(usuarioId)
	                .stream()
	                .map(categoriaMapper::toResponseDTO)
	                .toList();
	}

    public List<CategoriaResponseDTO> listarAtivos(Long usuarioId) {

        return categoriaRepository
                .findByUsuarioIdAndAtivo(usuarioId, "S")
                .stream()
                .map(categoriaMapper::toResponseDTO)
                .toList();
    }

}
