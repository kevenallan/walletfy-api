package br.com.walletfy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaResumoResponseDTO {

	private Long totalCategorias;
	private Long categoriasAtivas;
	private String categoriaMaisUtilizada;
	private String iconeMaisUtilizada;
	private String corMaisUtilizada;
	private Long qtdMaisUtilizada;
	private String categoriaMenosUtilizada;
	private String iconeMenosUtilizada;
	private String corMenosUtilizada;
	private Long qtdMenosUtilizada;

}
