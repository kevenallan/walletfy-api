package br.com.walletfy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.walletfy.entity.Cartao;
import br.com.walletfy.entity.Usuario;
import br.com.walletfy.repository.projection.CartaoListagemProjection;


public interface CartaoRepository extends JpaRepository<Cartao, Long> {
	List<Cartao> findByUsuarioAndAtivo(Usuario usuario, Boolean ativo);
	
	@Query(value = """
			SELECT
			    ct.id,
			    ct.nome,
			    ct.limite,
			    ct.dia_fechamento,
			    ct.dia_vencimento,
			    COALESCE(g.fatura_atual, 0)                                      AS fatura_atual,
			    ct.limite - COALESCE(g.fatura_atual, 0)                          AS limite_disponivel,
			    CASE
			        WHEN ct.limite > 0
			        THEN ROUND((COALESCE(g.fatura_atual, 0) / ct.limite) * 100, 0)
			        ELSE 0
			    END                                                                AS percentual_usado,
			    b.id                                                               AS banco_id,
			    b.nome                                                             AS banco_nome,
			    b.icone                                                            AS banco_icone,
			    b.cor                                                              AS banco_cor
			FROM cartao ct
			JOIN conta c
			    ON c.id = ct.conta_id
			LEFT JOIN banco b
			    ON b.id = c.banco_id
			LEFT JOIN (
			    SELECT g.cartao_id, SUM(g.valor) AS fatura_atual
			    FROM gasto g
			    JOIN status_gasto sg ON sg.id = g.status_id
			    WHERE g.ativo = TRUE
			      AND g.cartao_id IS NOT NULL
			      AND sg.nome IN ('PENDENTE', 'ATRASADO')
			      AND TO_CHAR(g.data_gasto, 'YYYY-MM') = TO_CHAR(CURRENT_DATE, 'YYYY-MM')
			    GROUP BY g.cartao_id
			) g ON g.cartao_id = ct.id
			WHERE ct.usuario_id = :usuarioId
			  AND ct.ativo = TRUE
			ORDER BY ct.nome;
	""", nativeQuery = true)
	List<CartaoListagemProjection> listarCartoes(@Param("usuarioId") Long usuarioId);
}

