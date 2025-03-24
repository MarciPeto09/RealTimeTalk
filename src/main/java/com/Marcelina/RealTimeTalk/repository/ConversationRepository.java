package com.Marcelina.RealTimeTalk.repository;

import com.Marcelina.RealTimeTalk.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    @Query("SELECT c FROM Conversation c JOIN c.participants u WHERE u.id = :userId")
    List<Conversation> findByUserId(@Param("userId") Long userId);
}
