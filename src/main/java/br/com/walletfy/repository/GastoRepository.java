package br.com.walletfy.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.walletfy.dto.GastoResumoResponseDTO;
import br.com.walletfy.dto.ResumoAnualResponseDTO;
import br.com.walletfy.entity.Gasto;

@Repository
public interface GastoRepository extends JpaRepository<Gasto, Long>{

	List<Gasto> findByUsuarioId(Long usuarioId);
	
	Optional<Gasto> findByIdAndUsuarioId(Long gastoId, Long usuarioId);

	List<Gasto> findByUsuarioIdAndDataGastoBetween(Long usuarioId, LocalDate dataInicio, LocalDate dataFim);
	
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
			        SUM(R.VALOR) AS RECEITAS
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
			        SUM(G.VALOR) AS DESPESAS,
			        COUNT(CASE WHEN G.STATUS_ID != 1 THEN 1 END) AS PENDENTES,
			        SUM(CASE WHEN G.STATUS_ID != 1 THEN G.VALOR ELSE 0 END) AS VALOR_PENDENTE
			    FROM GASTO G
			    WHERE
			        G.USUARIO_ID = :usuarioId
			        AND TO_CHAR(G.DATA_GASTO, 'YYYY-MM') >= TO_CHAR(CURRENT_DATE - INTERVAL '5 MONTHS', 'YYYY-MM')
			        AND TO_CHAR(G.DATA_GASTO, 'YYYY-MM') <= TO_CHAR(CURRENT_DATE, 'YYYY-MM')
			        AND G.ATIVO = TRUE
			    GROUP BY TO_CHAR(G.DATA_GASTO, 'YYYY-MM')
			)
			SELECT
			    M.MES,
			    COALESCE(RR.RECEITAS, 0) AS RECEITA,
			    COALESCE(RG.DESPESAS, 0) AS DESPESAS,
			    (COALESCE(RR.RECEITAS, 0) - COALESCE(RG.DESPESAS, 0)) AS SALDO,
			    COALESCE(RG.PENDENTES, 0) AS PENDENTES,
			    COALESCE(RG.VALOR_PENDENTE, 0) AS VALOR_PENDENTE,
			    ROUND(
			        COALESCE(RR.RECEITAS, 0) - LAG(COALESCE(RR.RECEITAS, 0)) OVER (ORDER BY M.MES),
			    2) AS VARIACAO_RECEITA,
			    ROUND(
			        COALESCE(RG.DESPESAS, 0) - LAG(COALESCE(RG.DESPESAS, 0)) OVER (ORDER BY M.MES),
			    2) AS VARIACAO_DESPESAS,
			    ROUND(
			        (COALESCE(RR.RECEITAS, 0) - COALESCE(RG.DESPESAS, 0)) - LAG(COALESCE(RR.RECEITAS, 0) - COALESCE(RG.DESPESAS, 0)) OVER (ORDER BY M.MES),
			    2) AS VARIACAO_SALDO
			FROM MESES M
			    LEFT JOIN RESUMO_GASTO RG ON RG.MES = M.MES
			    LEFT JOIN RESUMO_RECEITA RR ON RR.MES = M.MES
			ORDER BY M.MES;
		    """, nativeQuery = true)
	Optional<List<GastoResumoResponseDTO>> getResumo(@Param("usuarioId") Long usuarioId);
	
	@Query(value = """
			WITH MESES AS (
				SELECT
					TO_CHAR(MAKE_DATE(:ano, GS, 1), 'YYYY-MM') AS MES,
					CASE
						GS
				            WHEN 1 THEN 'JANEIRO'
						WHEN 2 THEN 'FEVEREIRO'
						WHEN 3 THEN 'MARÇO'
						WHEN 4 THEN 'ABRIL'
						WHEN 5 THEN 'MAIO'
						WHEN 6 THEN 'JUNHO'
						WHEN 7 THEN 'JULHO'
						WHEN 8 THEN 'AGOSTO'
						WHEN 9 THEN 'SETEMBRO'
						WHEN 10 THEN 'OUTUBRO'
						WHEN 11 THEN 'NOVEMBRO'
						WHEN 12 THEN 'DEZEMBRO'
					END AS NOME_MES
				FROM
					GENERATE_SERIES(1, 12) GS
				),
				RESUMO_RECEITA AS (
				SELECT
					TO_CHAR(R.DATA_RECEITA, 'YYYY-MM') AS MES,
					SUM(R.VALOR) AS TOTAL_RECEITAS
				FROM
					RECEITA R
				WHERE
					R.USUARIO_ID = :usuarioId
					AND EXTRACT(YEAR FROM R.DATA_RECEITA) = :ano
					AND R.ATIVO = TRUE
				GROUP BY
					TO_CHAR(R.DATA_RECEITA, 'YYYY-MM')
				),
				RESUMO_GASTO AS (
				SELECT
					TO_CHAR(G.DATA_GASTO, 'YYYY-MM') AS MES,
					SUM(G.VALOR) AS TOTAL_GASTOS
				FROM
					GASTO G
				WHERE
					G.USUARIO_ID = :usuarioId
					AND EXTRACT(YEAR FROM G.DATA_GASTO) = :ano
					AND G.ATIVO = TRUE
				GROUP BY
					TO_CHAR(G.DATA_GASTO, 'YYYY-MM')
				),
				CATEGORIA_MAIS_UTILIZADA AS (
				SELECT
					TO_CHAR(G.DATA_GASTO, 'YYYY-MM') AS MES,
					C.NOME AS CATEGORIA,
					C.ICONE,
					C.COR,
					COUNT(*) AS QUANTIDADE_LANCAMENTOS,
					SUM(G.VALOR) AS TOTAL_GASTO_CATEGORIA,
					ROW_NUMBER() OVER (
				            PARTITION BY TO_CHAR(G.DATA_GASTO, 'YYYY-MM')
				ORDER BY
					COUNT(*) DESC,
					SUM(G.VALOR) DESC
				        ) AS RN
				FROM
					GASTO G
				INNER JOIN CATEGORIA C
				        ON
					C.ID = G.CATEGORIA_ID
				WHERE
					G.USUARIO_ID = :usuarioId
					AND EXTRACT(YEAR FROM G.DATA_GASTO) = :ano
					AND G.ATIVO = TRUE
				GROUP BY
					TO_CHAR(G.DATA_GASTO, 'YYYY-MM'),
					C.NOME,
					C.ICONE,
					C.COR
				),
				CATEGORIA_MAIOR_GASTO AS (
				SELECT
					TO_CHAR(G.DATA_GASTO, 'YYYY-MM') AS MES,
					C.NOME AS CATEGORIA,
					C.ICONE,
					C.COR,
					SUM(G.VALOR) AS TOTAL_GASTO,
					ROW_NUMBER() OVER (
				            PARTITION BY TO_CHAR(G.DATA_GASTO, 'YYYY-MM')
				ORDER BY
					SUM(G.VALOR) DESC,
					COUNT(*) DESC
				        ) AS RN
				FROM
					GASTO G
				INNER JOIN CATEGORIA C
				        ON
					C.ID = G.CATEGORIA_ID
				WHERE
					G.USUARIO_ID = :usuarioId
					AND EXTRACT(YEAR FROM G.DATA_GASTO) = :ano
					AND G.ATIVO = TRUE
				GROUP BY
					TO_CHAR(G.DATA_GASTO, 'YYYY-MM'),
					C.NOME,
					C.ICONE,
					C.COR
				)
				SELECT
					M.NOME_MES AS MES,
					COALESCE(RR.TOTAL_RECEITAS, 0) AS TOTAL_RECEITAS,
					COALESCE(RG.TOTAL_GASTOS, 0) AS TOTAL_GASTOS,
					COALESCE(RR.TOTAL_RECEITAS, 0) -
				    COALESCE(RG.TOTAL_GASTOS, 0) AS SALDO,
					CMU.CATEGORIA AS CATEGORIA_MAIS_UTILIZADA,
					CMU.ICONE AS ICONE_CATEGORIA_MAIS_UTILIZADA,
					CMU.COR AS COR_CATEGORIA_MAIS_UTILIZADA,
					COALESCE(CMU.QUANTIDADE_LANCAMENTOS, 0) AS QUANTIDADE_CATEGORIA,
					COALESCE(CMU.TOTAL_GASTO_CATEGORIA, 0) AS TOTAL_GASTO_CATEGORIA,
					CMG.CATEGORIA AS CATEGORIA_MAIOR_GASTO,
					CMG.ICONE AS ICONE_CATEGORIA_MAIOR_GASTO,
					CMG.COR AS COR_CATEGORIA_MAIOR_GASTO,
					COALESCE(CMG.TOTAL_GASTO, 0) AS TOTAL_CATEGORIA_MAIOR_GASTO
				FROM
					MESES M
				LEFT JOIN RESUMO_RECEITA RR
				    ON
					RR.MES = M.MES
				LEFT JOIN RESUMO_GASTO RG
				    ON
					RG.MES = M.MES
				LEFT JOIN CATEGORIA_MAIS_UTILIZADA CMU
				    ON
					CMU.MES = M.MES
					AND CMU.RN = 1
				LEFT JOIN CATEGORIA_MAIOR_GASTO CMG
				    ON
					CMG.MES = M.MES
					AND CMG.RN = 1
				ORDER BY
					M.MES;
			""", nativeQuery = true)
	List<ResumoAnualResponseDTO> getResumoAnual(@Param("usuarioId") Long usuarioId, @Param("ano") int ano);
}
