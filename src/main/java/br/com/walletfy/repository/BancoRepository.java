package br.com.walletfy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.walletfy.entity.Banco;

public interface BancoRepository extends JpaRepository<Banco, Long> {

}
