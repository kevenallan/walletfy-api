package br.com.walletfy.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import br.com.walletfy.enums.TipoCategoria;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categoria")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Categoria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long usuarioId;
	private String nome;
	@Enumerated(EnumType.STRING)
	private TipoCategoria tipo;
	private String cor;
	private String icone;
	private Boolean ativo;
	@CreationTimestamp
	private LocalDateTime dataCadastro;
}
