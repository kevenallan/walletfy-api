package br.com.walletfy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.walletfy.entity.Gasto;

@Repository
public interface GastoRepository extends JpaRepository<Gasto, Long>{

	List<Gasto> findByUsuarioId(Long usuarioId);
	
	Optional<Gasto> findByIdAndUsuarioId(Long gastoId, Long usuarioId);
}
