package com.master.models.post;

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
@Table(name = "LIKES")
public class Like extends BaseModel {
    @Column(name = "IS_LIKE")
    private boolean isLike;

    //Relations
    @ManyToOne
    @JoinColumn(name = "GIVEN_BY_USER_ID")
    private User isGivenByUser;
    @ManyToOne
    @JoinColumn(name = "COMMENT_ID")
    private Comment comment;
    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;
}
