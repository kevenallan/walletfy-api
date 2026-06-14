package br.com.walletfy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.walletfy.entity.StatusGasto;

@Repository
public interface StatusGastoRepository extends JpaRepository<StatusGasto, Long>{

}
