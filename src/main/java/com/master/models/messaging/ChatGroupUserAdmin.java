package com.master.models.messaging;

import com.master.models.shared.BaseModel;
import com.master.models.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "CHAT_GROUP_USER_ADMINS")
public class ChatGroupUserAdmin extends BaseModel {
    @Column(name = "IS_CREATOR")
    private boolean isTheCreator;

    @Column(name = "IS_ADMIN")
    private boolean isAdmin;

    //Relations
    @ManyToOne
    @JoinColumn(name = "CHAT_GROUP_ID")
    private ChatGroup chatGroup;

    @ManyToOne
    @JoinColumn(name = "ADMIN_USER_ID")
    private User user;
}
