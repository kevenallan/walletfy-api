package br.com.walletfy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.walletfy.entity.Categoria;

@Repository
public interface CategoriaRespository extends JpaRepository<Categoria, Long> {

	List<Categoria> findByUsuarioId(Long usuarioId);
	List<Categoria> findByUsuarioIdAndAtivo(Long usuarioId, String ativo);
	boolean existsByUsuarioIdAndNome(Long usuarioId, String nome);
}
