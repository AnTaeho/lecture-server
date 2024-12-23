package com.example.lectureserver.ticket.repository;

import com.example.lectureserver.ticket.domain.Ticket;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("select t.amount from Ticket t where t.id = :ticketId")
    Integer getTicketAmount(@Param("ticketId") Long ticketId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from Ticket t where t.id = :ticketId")
    Optional<Ticket> findByIdWithLock(@Param("ticketId") Long ticketId);
}
