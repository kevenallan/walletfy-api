package br.com.walletfy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.walletfy.dto.ContaResumoResponseDTO;
import br.com.walletfy.entity.Conta;
import br.com.walletfy.repository.projection.ContaListagemProjection;



public interface ContaRepository extends JpaRepository<Conta, Long>{
	
	@Query(value = """
			SELECT
			    c.id,
			    c.nome,
			    c.tipo                                          AS tipo_conta,
			    c.saldo_inicial
			        + COALESCE(r.total_receitas, 0)
			        - COALESCE(g.total_gastos, 0)               AS saldo,
			    c.ativo,
				b.id                                            AS banco_id,
                b.nome                                          AS banco_nome,
                b.icone                                         AS banco_icone,
                b.cor                                           AS banco_cor
			FROM conta c
			LEFT JOIN banco b
			    ON b.id = c.banco_id
			LEFT JOIN (
			    SELECT conta_id, SUM(valor) AS total_receitas
			    FROM receita
			    WHERE ativo = TRUE
			    GROUP BY conta_id
			) r ON r.conta_id = c.id
			LEFT JOIN (
			    SELECT conta_id, SUM(valor) AS total_gastos
			    FROM gasto
			    WHERE ativo = TRUE
			      AND conta_id IS NOT NULL   -- só gastos de débito/pix/dinheiro, não os de cartão
			    GROUP BY conta_id
			) g ON g.conta_id = c.id
			WHERE c.usuario_id = :usuarioId
			AND c.ativo = true
			ORDER BY c.nome;
	""", nativeQuery = true)
	List<ContaListagemProjection> listarContasUsuario(@Param("usuarioId") Long usuarioId);

	@Query(value=""" 
			WITH saldo_por_conta AS (
			    SELECT
			        c.id,
			        c.nome,
			        c.tipo,
			        c.saldo_inicial
			            + COALESCE(r.total, 0)
			            - COALESCE(g.total, 0) AS saldo
			    FROM conta c
			    LEFT JOIN (
			        SELECT conta_id, SUM(valor) AS total
			        FROM receita
			        WHERE ativo = TRUE
			        GROUP BY conta_id
			    ) r ON r.conta_id = c.id
			    LEFT JOIN (
			        SELECT conta_id, SUM(valor) AS total
			        FROM gasto
			        WHERE ativo = TRUE AND conta_id IS NOT NULL
			        GROUP BY conta_id
			    ) g ON g.conta_id = c.id
			    WHERE c.usuario_id = :usuarioId AND c.ativo = TRUE
			),
			movimentacao_por_conta AS (
			    SELECT c.id, c.nome, COUNT(*) AS quantidade
			    FROM conta c
			    JOIN (
			        SELECT conta_id FROM receita WHERE ativo = TRUE AND conta_id IS NOT NULL
			        UNION ALL
			        SELECT conta_id FROM gasto WHERE ativo = TRUE AND conta_id IS NOT NULL
			    ) mov ON mov.conta_id = c.id
			    WHERE c.usuario_id = :usuarioId AND c.ativo = TRUE
			    GROUP BY c.id, c.nome
			)
			SELECT
			    (SELECT COALESCE(SUM(saldo), 0) FROM saldo_por_conta)                          AS saldo_total,
			    (SELECT nome FROM movimentacao_por_conta ORDER BY quantidade DESC LIMIT 1)      AS conta_movimentacao_nome,
			    (SELECT quantidade FROM movimentacao_por_conta ORDER BY quantidade DESC LIMIT 1) AS conta_movimentacao_qtd,
			    (SELECT nome FROM saldo_por_conta ORDER BY saldo DESC LIMIT 1)                  AS conta_maior_saldo_nome,
			    (SELECT saldo FROM saldo_por_conta ORDER BY saldo DESC LIMIT 1)                 AS conta_maior_saldo_valor,
			    (SELECT COALESCE(SUM(saldo), 0) FROM saldo_por_conta WHERE tipo = 'POUPANCA')   AS total_poupanca;
			""", nativeQuery = true)
	ContaResumoResponseDTO getResumo(@Param("usuarioId") Long usuarioId);
}
