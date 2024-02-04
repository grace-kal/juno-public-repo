package com.master.services.message;

import com.master.data.IChatGroupRepository;
import com.master.data.ICommentRepository;
import com.master.models.messaging.ChatGroup;
import com.master.models.post.Comment;
import com.master.services.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class ChatGroupService extends BaseService<ChatGroup> {
    @Autowired
    IChatGroupRepository _repository;

    @Override
    protected JpaRepository<ChatGroup, Long> getRepo() {
        return _repository;
    }

    public ChatGroup getChatGroupByName(String name){
        Optional<ChatGroup> maybe = _repository.findChatGroupByName(name);
        return maybe.orElse(null);
    }
}
