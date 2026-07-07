package br.com.walletfy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.walletfy.entity.Cartao;
import br.com.walletfy.entity.Usuario;

import java.util.List;


public interface CartaoRepository extends JpaRepository<Cartao, Long> {
	List<Cartao> findByUsuarioAndAtivo(Usuario usuario, Boolean ativo);
}

