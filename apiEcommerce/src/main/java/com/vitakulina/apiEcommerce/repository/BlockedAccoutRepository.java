package com.vitakulina.apiEcommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vitakulina.apiEcommerce.model.BlockedAccout;

public interface BlockedAccoutRepository extends JpaRepository <BlockedAccout, Long> {

	Optional<BlockedAccout> findByRecoveryKey(String key);
}
