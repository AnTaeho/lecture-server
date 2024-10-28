package com.example.lectureserver.balance.manager;

import com.example.lectureserver.balance.domain.Balance;
import com.example.lectureserver.balance.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class BalanceManager {

    private final BalanceRepository balanceRepository;

    public void charge(Long id, int amount) {
        Balance balance = balanceRepository.findByUserId(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 계좌를 찾을 수 없습니다."));
        balance.charge(amount);
//        balanceRepository.saveAndFlush(balance);
    }
}
