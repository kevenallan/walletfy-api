package br.com.walletfy.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cartao")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Cartao {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "conta_id")
	private Conta conta;
	
	private String nome;
	private BigDecimal limite;
	private Long diaFechamento;
	private Long diaVencimento;
	private Boolean ativo;
	
	@CreationTimestamp
	private LocalDateTime dataCadastro;
	
	@CreationTimestamp
	private LocalDateTime dataAtualizacao;
}
