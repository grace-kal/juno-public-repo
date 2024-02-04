package com.master.models.messaging;

import com.master.models.shared.BaseModel;
import com.master.models.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "MESSAGES")
public class Message extends BaseModel {
    private String content;
    @Column(name = "SENT_AT")
    private LocalDateTime sentAt;

    //Relations

    @ManyToOne
    @JoinColumn(name = "SENDER_USER_ID")
    private User sender;
    @ManyToOne
    @JoinColumn(name = "RECEIVER_USER_ID")
    private User receiverUser;
    @ManyToOne
    @JoinColumn(name = "RECEIVER_CHAT_GROUP_ID")
    private ChatGroup receiverChatGroup;

}
