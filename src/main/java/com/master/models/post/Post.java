package com.master.models.post;

import com.master.models.enums.Status;
import com.master.models.shared.BaseModel;
import com.master.models.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "POSTS")
public class Post extends BaseModel {
    private String title;
    private String content;
    private Status status;
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    //Relations
    @ManyToOne
    @JoinColumn(name = "AUTHOR_USER_ID")
    private User author;
    @OneToMany(mappedBy="post")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Comment> comments;
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy="post")
    private Set<Like> likes;
}
