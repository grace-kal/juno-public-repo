package com.master.models.user;

import com.master.models.shared.BaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "FRIENDSHIPS")
public class Friendship extends BaseModel {
    private boolean accepted;
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    //Relations
    @ManyToOne
    @JoinColumn(name = "SENDER_USER_ID")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "RECEIVER_USER_ID")
    private User receiver;
}
