package br.com.walletfy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
			ORDER BY c.nome;
	""", nativeQuery = true)
	List<ContaListagemProjection> listarContasUsuario(@Param("usuarioId") Long usuarioId);
//	AND c.ativo = true
}
