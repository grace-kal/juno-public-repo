package com.master.data;

import com.master.models.messaging.ChatGroup;
import com.master.models.user.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IChatGroupRepository<T extends ChatGroup> extends IBaseRepository<T> {
    Optional<ChatGroup> findChatGroupByName(String name);
}
