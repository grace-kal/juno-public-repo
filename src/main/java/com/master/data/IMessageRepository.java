package com.master.data;

import com.master.models.messaging.Message;
import com.master.models.post.Post;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IMessageRepository <T extends Message> extends IBaseRepository<T> {
    @Query(value = "SELECT * FROM MESSAGES WHERE SENDER_USER_ID = ?1", nativeQuery = true)
    public List<Message> getAllMessagesByUser(Long userId);

    @Query(value = "SELECT * FROM MESSAGES WHERE RECEIVER_USER_ID = ?1", nativeQuery = true)
    public List<Message> getAllMessagesToUser(Long userId);

    @Query(value = "SELECT * FROM MESSAGES WHERE RECEIVER_CHAT_GROUP_ID = ?1", nativeQuery = true)
    public List<Message> getAllMessagesFromChatGroup(Long chatGroupId);
}
