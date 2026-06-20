package br.com.walletfy.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.walletfy.dto.GastoResumoResponseDTO;
import br.com.walletfy.entity.Gasto;

@Repository
public interface GastoRepository extends JpaRepository<Gasto, Long>{

	List<Gasto> findByUsuarioId(Long usuarioId);
	
	Optional<Gasto> findByIdAndUsuarioId(Long gastoId, Long usuarioId);

	List<Gasto> findByUsuarioIdAndDataGastoBetween(Long usuarioId, LocalDate dataInicio, LocalDate dataFim);
	
	@Query(value = """
		    WITH resumo AS (
		        SELECT
		            TO_CHAR(data_gasto, 'YYYY-MM') AS mes,
		            1621.00 AS receita,
		            SUM(g.valor) AS despesas,
		            (1621.00 - SUM(g.valor)) AS saldo,
		            COUNT(CASE WHEN g.status_id != 1 THEN 1 END) AS pendentes
		        FROM gasto g
		        WHERE g.usuario_id = :usuarioId
		          AND TO_CHAR(g.data_gasto, 'YYYY-MM') >= TO_CHAR(CURRENT_DATE - INTERVAL '5 months', 'YYYY-MM')
		          AND TO_CHAR(g.data_gasto, 'YYYY-MM') <= TO_CHAR(CURRENT_DATE, 'YYYY-MM')
		        GROUP BY TO_CHAR(g.data_gasto, 'YYYY-MM')
		    )
		    SELECT
		        mes AS mes,
		        receita AS receita,
		        despesas AS despesas,
		        saldo AS saldo,
		        pendentes AS pendentes,
		        ROUND(
		            ((receita - LAG(receita) OVER (ORDER BY mes)) / NULLIF(LAG(receita) OVER (ORDER BY mes), 0)) * 100,
		        1) AS variacaoReceita,
		        ROUND(
		            ((despesas - LAG(despesas) OVER (ORDER BY mes)) / NULLIF(LAG(despesas) OVER (ORDER BY mes), 0)) * 100,
		        1) AS variacaoDespesas,
		        ROUND(
		            ((saldo - LAG(saldo) OVER (ORDER BY mes)) / NULLIF(LAG(saldo) OVER (ORDER BY mes), 0)) * 100,
		        1) AS variacaoSaldo
		    FROM resumo
		    ORDER BY mes
		    """, nativeQuery = true)
		Optional<List<GastoResumoResponseDTO>> getResumo(@Param("usuarioId") Long usuarioId);
}
