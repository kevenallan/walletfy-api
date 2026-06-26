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
	
	@Query(value = """
		    WITH RESUMO_RECEITA AS (
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
			    RR.MES,
			    RR.RECEITAS,
			    COALESCE(RG.GASTOS, 0) AS GASTOS,
			    (RR.RECEITAS - COALESCE(RG.GASTOS, 0)) AS SALDO,
			    RR.PENDENTES,
			    RR.VALOR_PENDENTE,
			    P.PRINCIPAL_CATEGORIA,
			    P.VALOR_PRINCIPAL_CATEGORIA,
			    ROUND((P.VALOR_PRINCIPAL_CATEGORIA / NULLIF(RR.RECEITAS, 0)) * 100, 1) AS PERCENTUAL_PRINCIPAL_CATEGORIA,
			    ROUND(
			        RR.RECEITAS - LAG(RR.RECEITAS) OVER (ORDER BY RR.MES),
			    1) AS VARIACAO_RECEITAS,
			    ROUND(
			        (RR.RECEITAS - COALESCE(RG.GASTOS, 0)) - LAG(RR.RECEITAS - COALESCE(RG.GASTOS, 0)) OVER (ORDER BY RR.MES),
			    1) AS VARIACAO_SALDO
			FROM RESUMO_RECEITA RR
			    LEFT JOIN RESUMO_GASTO RG ON RG.MES = RR.MES
			    LEFT JOIN PRINCIPAL_CATEGORIA_MES P ON P.MES = RR.MES AND P.RANK = 1
			ORDER BY RR.MES;
		    """, nativeQuery = true)
		Optional<List<ReceitaResumoResponseDTO>> getResumo(@Param("usuarioId") Long usuarioId);
}
