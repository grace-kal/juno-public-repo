package com.master.models.post;

import com.master.models.enums.Status;
import com.master.models.shared.BaseModel;
import com.master.models.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "COMMENTS")
public class Comment extends BaseModel {
    private String content;
    private Status status;
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    //Relations
    @ManyToOne
    @JoinColumn(name = "AUTHOR_USER_ID")
    private User author;
    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;
    @OneToMany(mappedBy="comment")
    private Set<Like> likes;
}
