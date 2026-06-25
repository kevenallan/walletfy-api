package br.com.walletfy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.walletfy.entity.Categoria;
import br.com.walletfy.enums.TipoCategoria;

@Repository
public interface CategoriaRespository extends JpaRepository<Categoria, Long> {

	List<Categoria> findByUsuarioIdAndTipoAndAtivo(Long usuarioId, TipoCategoria tipo, Boolean ativo);
	Optional<Categoria> findByUsuarioIdAndId(Long usuarioId, Long categoriaId);
	boolean existsByUsuarioIdAndNome(Long usuarioId, String nome);
	List<Categoria> findByUsuarioIdAndTipo(Long usuarioId, TipoCategoria tipo);
}
