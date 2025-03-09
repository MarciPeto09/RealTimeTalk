package com.Marcelina.RealTimeTalk.repository;

import com.Marcelina.RealTimeTalk.model.Messages;
import com.Marcelina.RealTimeTalk.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessagesRepository extends JpaRepository<Messages, Long> {

    List<Messages> findBySender(Users user);

    List<Messages> findByReceiver(Users user);

    @Query("SELECT m FROM Messages m WHERE (m.sender.id = :senderId AND m.receiver.id = :receiverId) " +
            "OR (m.sender.id = :receiverId AND m.receiver.id = :senderId) ORDER BY m.timestamp ASC")
    List<Messages> findMessagesBetweenUsers(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);
}
