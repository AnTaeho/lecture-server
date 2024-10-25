package com.example.lectureserver.ticket.repository;

import com.example.lectureserver.ticket.domain.UserTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTicketRepository extends JpaRepository<UserTicket, Long> {
}
