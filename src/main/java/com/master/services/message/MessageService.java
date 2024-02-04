package com.master.services.message;

import com.master.data.ICommentRepository;
import com.master.data.IMessageRepository;
import com.master.models.messaging.Message;
import com.master.models.post.Comment;
import com.master.services.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class MessageService  extends BaseService<Message> {
    @Autowired
    IMessageRepository _repository;

    @Override
    protected JpaRepository<Message, Long> getRepo() {
        return _repository;
    }

    public List<Message> getAllMessagesByUser(Long userId){
        return _repository.getAllMessagesByUser(userId);
    }

    public List<Message> getAllMessagesToUser(Long userId){
        return _repository.getAllMessagesToUser(userId);
    }

    public List<Message> getAllMessagesFromChatGroup(Long chatGroupId){
        return _repository.getAllMessagesFromChatGroup(chatGroupId);
    }
}
