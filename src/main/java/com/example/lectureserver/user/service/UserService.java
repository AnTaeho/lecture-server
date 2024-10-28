package com.example.lectureserver.user.service;

import com.example.lectureserver.balance.domain.Balance;
import com.example.lectureserver.balance.manager.BalanceManager;
import com.example.lectureserver.balance.repository.BalanceRepository;
import com.example.lectureserver.common.annotation.DistributedLock;
import com.example.lectureserver.ticket.redis.RedisTicketService;
import com.example.lectureserver.user.controller.dto.LoginRequest;
import com.example.lectureserver.user.controller.dto.UserResponse;
import com.example.lectureserver.user.controller.dto.JoinRequest;
import com.example.lectureserver.user.domain.User;
import com.example.lectureserver.user.manager.UserManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserManager userManager;
    private final BalanceRepository balanceRepository;
    private final RedisTicketService redisTicketService;
    private final BalanceManager balanceManager;

    public UserResponse join(JoinRequest joinRequest) {
        User joinedUser = userManager.join(joinRequest);
        Balance balance = new Balance(joinedUser);
        balanceRepository.save(balance);
        return new UserResponse(joinedUser.getId());
    }

    public UserResponse login(LoginRequest loginRequest) {
        return new UserResponse(userManager.login(loginRequest.email(), loginRequest.password()).getId());
    }

    @DistributedLock(key = "charge-balance")
    @Transactional
    public void chargeBalance(String email, int amount) {
//        boolean isDuplicate = redisTicketService.checkUserChargeTrial(email);
//        if (isDuplicate) {
//            return;
//        }

        User user = userManager.getUser(email);
        balanceManager.charge(user.getId(), amount);
    }

}
