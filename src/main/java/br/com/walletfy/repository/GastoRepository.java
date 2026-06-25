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
		    WITH RESUMO_RECEITA AS (
			    SELECT
			        TO_CHAR(R.DATA_RECEITA, 'YYYY-MM') AS MES,
			        SUM(R.VALOR) AS RECEITAS
			    FROM RECEITA R
			    WHERE
			        R.USUARIO_ID = 1
			        AND TO_CHAR(R.DATA_RECEITA, 'YYYY-MM') >= TO_CHAR(CURRENT_DATE - INTERVAL '5 MONTHS', 'YYYY-MM')
			        AND TO_CHAR(R.DATA_RECEITA, 'YYYY-MM') <= TO_CHAR(CURRENT_DATE, 'YYYY-MM')
			        AND R.ATIVO = TRUE
			    GROUP BY TO_CHAR(R.DATA_RECEITA, 'YYYY-MM')
			),
			RESUMO_GASTO AS (
			    SELECT
			        TO_CHAR(G.DATA_GASTO, 'YYYY-MM') AS MES,
			        SUM(G.VALOR) AS DESPESAS,
			        COUNT(CASE WHEN G.STATUS_ID != 1 THEN 1 END) AS PENDENTES,
			        SUM(CASE WHEN G.STATUS_ID != 1 THEN G.VALOR ELSE 0 END) AS VALOR_PENDENTE
			    FROM GASTO G
			    WHERE
			        G.USUARIO_ID = 1
			        AND TO_CHAR(G.DATA_GASTO, 'YYYY-MM') >= TO_CHAR(CURRENT_DATE - INTERVAL '5 MONTHS', 'YYYY-MM')
			        AND TO_CHAR(G.DATA_GASTO, 'YYYY-MM') <= TO_CHAR(CURRENT_DATE, 'YYYY-MM')
			        AND G.ATIVO = TRUE
			    GROUP BY TO_CHAR(G.DATA_GASTO, 'YYYY-MM')
			)
			SELECT
			    RG.MES,
			    COALESCE(RR.RECEITAS, 0) AS RECEITA,
			    RG.DESPESAS,
			    (COALESCE(RR.RECEITAS, 0) - RG.DESPESAS) AS SALDO,
			    RG.PENDENTES,
			    RG.VALOR_PENDENTE,
			    ROUND(
			        COALESCE(RR.RECEITAS, 0) - LAG(COALESCE(RR.RECEITAS, 0)) OVER (ORDER BY RG.MES),
			    1) AS VARIACAO_RECEITA,
			    ROUND(
			        RG.DESPESAS - LAG(RG.DESPESAS) OVER (ORDER BY RG.MES),
			    1) AS VARIACAO_DESPESAS,
			    ROUND(
			        (COALESCE(RR.RECEITAS, 0) - RG.DESPESAS) - LAG(COALESCE(RR.RECEITAS, 0) - RG.DESPESAS) OVER (ORDER BY RG.MES),
			    1) AS VARIACAO_SALDO
			FROM RESUMO_GASTO RG
			    LEFT JOIN RESUMO_RECEITA RR ON RR.MES = RG.MES
			ORDER BY RG.MES
		    """, nativeQuery = true)
		Optional<List<GastoResumoResponseDTO>> getResumo(@Param("usuarioId") Long usuarioId);
}
