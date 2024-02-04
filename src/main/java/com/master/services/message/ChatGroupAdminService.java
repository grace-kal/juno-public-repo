package com.master.services.message;

import com.master.data.IChatGroupAdminRepository;
import com.master.models.messaging.ChatGroupUserAdmin;
import com.master.services.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ChatGroupAdminService extends BaseService<ChatGroupUserAdmin> {
    @Autowired
    IChatGroupAdminRepository _repository;

    @Override
    protected JpaRepository<ChatGroupUserAdmin, Long> getRepo() {
        return _repository;
    }

    public List<ChatGroupUserAdmin> getAllChatGroupsOfUser(Long userId) {
        return _repository.getAllChatGroupsOfUser(userId);
    }
}
