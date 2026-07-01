package br.com.walletfy.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.walletfy.dto.ReceitaResumoResponseDTO;
import br.com.walletfy.entity.Receita;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long>{

	List<Receita> findByUsuarioId(Long usuarioId);
	Optional<Receita> findByIdAndUsuarioId(Long receitaId, Long usuarioId);
	List<Receita> findByUsuarioIdAndDataReceitaBetween(Long usuarioId, LocalDate dataInicio, LocalDate dataFim);
	Long countByUsuarioIdAndCategoriaId(Long usuarioId, Long gastoId);
	
	@Query(value = """
			WITH MESES AS (
			    SELECT TO_CHAR(GS, 'YYYY-MM') AS MES
			    FROM GENERATE_SERIES(
			        DATE_TRUNC('month', CURRENT_DATE - INTERVAL '5 months'),
			        DATE_TRUNC('month', CURRENT_DATE),
			        INTERVAL '1 month'
			    ) AS GS
			),
			RESUMO_RECEITA AS (
			    SELECT
			        TO_CHAR(R.DATA_RECEITA, 'YYYY-MM') AS MES,
			        SUM(R.VALOR) AS RECEITAS,
			        COUNT(CASE WHEN R.STATUS_ID = 2 THEN 1 END) AS PENDENTES,
			        SUM(CASE WHEN R.STATUS_ID = 2 THEN R.VALOR ELSE 0 END) AS VALOR_PENDENTE
			    FROM RECEITA R
			    WHERE
			        R.USUARIO_ID = :usuarioId
			        AND TO_CHAR(R.DATA_RECEITA, 'YYYY-MM') >= TO_CHAR(CURRENT_DATE - INTERVAL '5 MONTHS', 'YYYY-MM')
			        AND TO_CHAR(R.DATA_RECEITA, 'YYYY-MM') <= TO_CHAR(CURRENT_DATE, 'YYYY-MM')
			        AND R.ATIVO = TRUE
			    GROUP BY TO_CHAR(R.DATA_RECEITA, 'YYYY-MM')
			),
			RESUMO_GASTO AS (
			    SELECT
			        TO_CHAR(G.DATA_GASTO, 'YYYY-MM') AS MES,
			        SUM(G.VALOR) AS GASTOS
			    FROM GASTO G
			    WHERE
			        G.USUARIO_ID = :usuarioId
			        AND TO_CHAR(G.DATA_GASTO, 'YYYY-MM') >= TO_CHAR(CURRENT_DATE - INTERVAL '5 MONTHS', 'YYYY-MM')
			        AND TO_CHAR(G.DATA_GASTO, 'YYYY-MM') <= TO_CHAR(CURRENT_DATE, 'YYYY-MM')
			        AND G.ATIVO = TRUE
			    GROUP BY TO_CHAR(G.DATA_GASTO, 'YYYY-MM')
			),
			PRINCIPAL_CATEGORIA_MES AS (
			    SELECT
			        TO_CHAR(R.DATA_RECEITA, 'YYYY-MM') AS MES,
			        C.NOME AS PRINCIPAL_CATEGORIA,
			        SUM(R.VALOR) AS VALOR_PRINCIPAL_CATEGORIA,
			        RANK() OVER (PARTITION BY TO_CHAR(R.DATA_RECEITA, 'YYYY-MM') ORDER BY SUM(R.VALOR) DESC) AS RANK
			    FROM RECEITA R
			        INNER JOIN CATEGORIA C ON C.ID = R.CATEGORIA_ID
			    WHERE
			        R.USUARIO_ID = :usuarioId
			        AND TO_CHAR(R.DATA_RECEITA, 'YYYY-MM') >= TO_CHAR(CURRENT_DATE - INTERVAL '5 MONTHS', 'YYYY-MM')
			        AND TO_CHAR(R.DATA_RECEITA, 'YYYY-MM') <= TO_CHAR(CURRENT_DATE, 'YYYY-MM')
			        AND R.ATIVO = TRUE
			    GROUP BY TO_CHAR(R.DATA_RECEITA, 'YYYY-MM'), C.NOME
			)
			SELECT
			    M.MES,
			    COALESCE(RR.RECEITAS, 0) AS RECEITAS,
			    COALESCE(RG.GASTOS, 0) AS GASTOS,
			    (COALESCE(RR.RECEITAS, 0) - COALESCE(RG.GASTOS, 0)) AS SALDO,
			    COALESCE(RR.PENDENTES, 0) AS PENDENTES,
			    COALESCE(RR.VALOR_PENDENTE, 0) AS VALOR_PENDENTE,
			    P.PRINCIPAL_CATEGORIA,
			    P.VALOR_PRINCIPAL_CATEGORIA,
			    ROUND((P.VALOR_PRINCIPAL_CATEGORIA / NULLIF(RR.RECEITAS, 0)) * 100, 2) AS PERCENTUAL_PRINCIPAL_CATEGORIA,
			    ROUND(
			        COALESCE(RR.RECEITAS, 0) - LAG(COALESCE(RR.RECEITAS, 0)) OVER (ORDER BY M.MES),
			    2) AS VARIACAO_RECEITAS,
			    ROUND(
			        (COALESCE(RR.RECEITAS, 0) - COALESCE(RG.GASTOS, 0)) - LAG(COALESCE(RR.RECEITAS, 0) - COALESCE(RG.GASTOS, 0)) OVER (ORDER BY M.MES),
			    2) AS VARIACAO_SALDO
			FROM MESES M
			    LEFT JOIN RESUMO_RECEITA RR ON RR.MES = M.MES
			    LEFT JOIN RESUMO_GASTO RG ON RG.MES = M.MES
			    LEFT JOIN PRINCIPAL_CATEGORIA_MES P ON P.MES = M.MES AND P.RANK = 1
			ORDER BY M.MES;
		    """, nativeQuery = true)
		Optional<List<ReceitaResumoResponseDTO>> getResumo(@Param("usuarioId") Long usuarioId);
}
