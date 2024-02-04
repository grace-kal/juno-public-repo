package com.master.models.messaging;

import com.master.models.enums.Status;
import com.master.models.shared.BaseModel;
import com.master.models.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "CHAT_GROUP_REQUESTS")
public class ChatGroupRequest extends BaseModel {
    private Status status;

    //Relations
    @ManyToOne
    @JoinColumn(name = "SENDER_USER_ID")
    private User sender;
    @ManyToOne
    @JoinColumn(name = "APPROVED_BY_USER_ID")
    private User approvedBy;
    @ManyToOne
    @JoinColumn(name = "CHAT_GROUP_ID")
    private ChatGroup chatGroup;
}
