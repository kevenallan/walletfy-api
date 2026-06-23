package br.com.walletfy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.walletfy.entity.StatusReceita;

@Repository
public interface StatusReceitaRepository extends JpaRepository<StatusReceita, Long>{

}
