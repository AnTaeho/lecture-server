package com.example.lectureserver.ticket.repository;

import com.example.lectureserver.ticket.domain.TicketOutbox;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketOutboxRepository extends JpaRepository<TicketOutbox, Long> {

    @Query("select tob from TicketOutbox tob where tob.ticketId = :ticketId and tob.email = :email")
    Optional<TicketOutbox> findByTicketIdAndEmail(
            @Param("ticketId") Long ticketId,
            @Param("email") String email
    );

    @Query("select tob from TicketOutbox tob where tob.status = 'CREATED' and tob.createdDate < :fiveMinuteAgo")
    List<TicketOutbox> findAllNotSendingMessage(@Param("fiveMinuteAgo") LocalDateTime fiveMinuteAgo);

    @Query("select tob.id from TicketOutbox tob where tob.createdDate < :oneHourAgo and tob.status = 'DONE'")
    List<Long> findOutboxOneHourAgo(@Param("oneHourAgo") LocalDateTime oneHourAgo);

    @Modifying
    @Query("delete TicketOutbox tob where tob.id in :outboxOneHourAgo")
    void deleteAllIn(@Param("outboxOneHourAgo") List<Long> outboxOneHourAgo);

    @Modifying
    @Query("update TicketOutbox tob set tob.status = 'PUBLISHED' where tob.ticketId = :ticketId and tob.email = :email")
    void updateToPublished(@Param("ticketId") Long ticketId, @Param("email") String email);

    @Query("select tob from TicketOutbox tob where tob.status = 'PUBLISHED'")
    List<TicketOutbox> findAllPublished();
}
