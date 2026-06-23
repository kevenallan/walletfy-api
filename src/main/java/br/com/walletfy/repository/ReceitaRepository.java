package br.com.walletfy.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.walletfy.entity.Receita;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long>{

	List<Receita> findByUsuarioId(Long usuarioId);
	Optional<Receita> findByIdAndUsuarioId(Long receitaId, Long usuarioId);
	List<Receita> findByUsuarioIdAndDataReceitaBetween(Long usuarioId, LocalDate dataInicio, LocalDate dataFim);
}
