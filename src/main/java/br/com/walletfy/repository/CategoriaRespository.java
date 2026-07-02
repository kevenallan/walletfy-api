package br.com.walletfy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.walletfy.dto.CategoriaResumoResponseDTO;
import br.com.walletfy.entity.Categoria;
import br.com.walletfy.enums.TipoCategoria;

@Repository
public interface CategoriaRespository extends JpaRepository<Categoria, Long> {

	List<Categoria> findByUsuarioIdAndTipoAndAtivo(Long usuarioId, TipoCategoria tipo, Boolean ativo);
	Optional<Categoria> findByUsuarioIdAndId(Long usuarioId, Long categoriaId);
	boolean existsByUsuarioIdAndNome(Long usuarioId, String nome);
	List<Categoria> findByUsuarioIdAndTipo(Long usuarioId, TipoCategoria tipo);
	
	@Query(value = """
			WITH CATEGORIAS AS (
			    SELECT
			        C.ID,
			        C.NOME,
			        C.ICONE,
			        C.COR,
			        C.ATIVO
			    FROM CATEGORIA C
			    WHERE
			        C.USUARIO_ID = :usuarioId
			        AND C.TIPO = :tipo
			),
			CONTAGEM_LANCAMENTOS AS (
			    SELECT CATEGORIA_ID, COUNT(*) AS QTD
			    FROM GASTO
			    WHERE USUARIO_ID = :usuarioId AND ATIVO = TRUE AND :tipo = 'DESPESA'
			    GROUP BY CATEGORIA_ID
			    UNION ALL
			    SELECT CATEGORIA_ID, COUNT(*) AS QTD
			    FROM RECEITA
			    WHERE USUARIO_ID = :usuarioId AND ATIVO = TRUE AND :tipo = 'RECEITA'
			    GROUP BY CATEGORIA_ID
			),
			CONTAGEM AS (
			    SELECT
			        C.ID,
			        C.NOME,
			        C.ICONE,
			        C.COR,
			        C.ATIVO,
			        COALESCE(L.QTD, 0) AS QTD_UTILIZACAO
			    FROM CATEGORIAS C
			        LEFT JOIN CONTAGEM_LANCAMENTOS L ON L.CATEGORIA_ID = C.ID
			),
			RANKED AS (
			    SELECT
			        *,
			        ROW_NUMBER() OVER (ORDER BY QTD_UTILIZACAO DESC, NOME ASC) AS RANK_MAIS,
			        ROW_NUMBER() OVER (ORDER BY QTD_UTILIZACAO ASC, NOME ASC) AS RANK_MENOS
			    FROM CONTAGEM
			)
			SELECT
			    (SELECT COUNT(*) FROM CONTAGEM) AS TOTAL_CATEGORIAS,
			    (SELECT COUNT(*) FROM CONTAGEM WHERE ATIVO = TRUE) AS CATEGORIAS_ATIVAS,
			    MAX(CASE WHEN RANK_MAIS = 1 THEN NOME END) AS CATEGORIA_MAIS_UTILIZADA,
			    MAX(CASE WHEN RANK_MAIS = 1 THEN ICONE END) AS ICONE_MAIS_UTILIZADA,
			    MAX(CASE WHEN RANK_MAIS = 1 THEN COR END) AS COR_MAIS_UTILIZADA,
			    MAX(CASE WHEN RANK_MAIS = 1 THEN QTD_UTILIZACAO END) AS QTD_MAIS_UTILIZADA,
			    MAX(CASE WHEN RANK_MENOS = 1 THEN NOME END) AS CATEGORIA_MENOS_UTILIZADA,
			    MAX(CASE WHEN RANK_MENOS = 1 THEN ICONE END) AS ICONE_MENOS_UTILIZADA,
			    MAX(CASE WHEN RANK_MENOS = 1 THEN COR END) AS COR_MENOS_UTILIZADA,
			    MAX(CASE WHEN RANK_MENOS = 1 THEN QTD_UTILIZACAO END) AS QTD_MENOS_UTILIZADA
			FROM RANKED;
		    """, nativeQuery = true)
		CategoriaResumoResponseDTO getResumo(@Param("usuarioId") Long usuarioId, @Param("tipo") String tipo);
}
