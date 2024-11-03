package com.example.lectureserver.payment.service;

import com.example.lectureserver.balance.domain.Balance;
import com.example.lectureserver.balance.repository.BalanceRepository;
import com.example.lectureserver.lecture.domain.Lecture;
import com.example.lectureserver.payment.domain.Payment;
import com.example.lectureserver.payment.dto.PaymentResult;
import com.example.lectureserver.payment.repository.PaymentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BalanceRepository balanceRepository;

    @Transactional
    public PaymentResult payPrice(Long userId, Lecture lecture, List<Integer> seatNumbers) {
        Balance balance = balanceRepository.findByUserId(userId).orElseThrow();
        int totalPrice = lecture.getPrice() * seatNumbers.size();

        if (balance.getAmount() < totalPrice) {
            throw new IllegalArgumentException("");
        }
        balance.use(totalPrice);
        paymentRepository.save(new Payment(totalPrice, userId, lecture.getId()));

        return new PaymentResult(totalPrice);
    }

}
