package com.master.models.messaging;

import com.master.models.shared.BaseModel;
import com.master.models.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "CHAT_GROUPS")
public class ChatGroup extends BaseModel {
    private String name;
    private String description;
    @Column(name = "IS_PRIVATE")
    private boolean isPrivate;
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    //Relationships
    @OneToMany(mappedBy = "receiverChatGroup")
    private Set<Message> messages;
    @OneToMany(mappedBy = "chatGroup")
    private Set<ChatGroupUserAdmin> usersInGroup;
    @OneToMany(mappedBy = "chatGroup")
    private Set<ChatGroupRequest> requests;
}
