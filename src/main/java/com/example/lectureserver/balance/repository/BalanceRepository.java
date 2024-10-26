package com.example.lectureserver.balance.repository;

import com.example.lectureserver.balance.domain.Balance;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<Balance, Long> {

    Optional<Balance> findByUserId(Long userId);

}
