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
@Table(name = "FOLLOWS")
public class Follow extends BaseModel {

    @Column(name = "sent_AT")
    private LocalDateTime sentAt;

    //Relations
    @ManyToOne
    @JoinColumn(name = "FOLLOWING_USER_ID")
    private User followingUser;

    @ManyToOne
    @JoinColumn(name = "FOLLOWED_USER_ID")
    private User followedUser;
}
