package br.com.walletfy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.walletfy.entity.Conta;


public interface ContaRepository extends JpaRepository<Conta, Long>{
	
}
