package com.Marcelina.RealTimeTalk.repository;

import com.Marcelina.RealTimeTalk.model.Messages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessagesRepository extends JpaRepository<Messages, Long> {

    List<Messages> getMessagesByConversation_Id(Long conversationId);


}
