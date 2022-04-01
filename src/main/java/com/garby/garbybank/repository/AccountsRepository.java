package com.garby.garbybank.repository;

import java.util.Optional;

import javax.persistence.LockModeType;

import com.garby.garbybank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AccountsRepository extends JpaRepository<Account, Long>{

	Optional<Account> findByAccountId(Long id);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Transactional
	@Query("SELECT a FROM Account a WHERE a.accountId = ?1")
	Optional<Account> getAccountForUpdate(Long id);
	
}
