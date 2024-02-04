package com.master.data;

import com.master.models.messaging.ChatGroup;
import com.master.models.messaging.ChatGroupUserAdmin;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IChatGroupAdminRepository <T extends ChatGroupUserAdmin> extends IBaseRepository<T> {

    @Query(value = "SELECT * FROM CHAT_GROUP_USER_ADMINS WHERE ADMIN_USER_ID = ?1", nativeQuery = true)
    public List<ChatGroupUserAdmin> getAllChatGroupsOfUser(Long userId);
}
