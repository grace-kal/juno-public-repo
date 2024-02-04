package com.master.models.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.master.models.post.*;
import com.master.models.messaging.*;
import com.master.models.quiz.*;
import com.master.models.enums.*;
import com.master.models.shared.BaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "USERS")
public class User extends BaseModel {

    @Column(nullable = false, unique = true, length = 255)
    private String username;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(length = 255)
    private String firstName;

    @Column(length = 255)
    private String lastName;

//    @Column(name = "POINTS_FROM_QUIZZES")
//    private int pointsFromQuizzes;

    @Column(nullable = false)
    private int age;

    private LocalDate dob;

    private boolean restricted;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @Column(name = "LAST_LOGGED_AT")
    private LocalDateTime loggedAt;

    private Roles role;

    //    color index for background color in prof pic if there is none
    @Column(name = "COLOR_INDEX")
    private int colorIndex;

    //    @Lob
    @Column(name = "PROFILE_PICTURE")
    private String profilePicture;

    //Relations
    //User relations
    @OneToMany(mappedBy = "sender")
    private Set<Friendship> sentRequestsFriendships;
    @OneToMany(mappedBy = "receiver")
    private Set<Friendship> receivedRequestsFriendships;
    @OneToMany(mappedBy = "followingUser")
    private Set<Follow> following;
    @OneToMany(mappedBy = "followedUser")
    private Set<Follow> followers;

    //Posts
    @OneToMany(mappedBy = "author")
    private Set<Post> posts;
    @OneToMany(mappedBy = "author")
    private Set<Comment> comments;
    @OneToMany(mappedBy = "isGivenByUser")
    private Set<Like> likes;

    //Messaging
    @OneToMany(mappedBy = "sender")
    private Set<Message> sendMessages;
    @OneToMany(mappedBy = "receiverUser")
    private Set<Message> receivedMessages;
    @OneToMany(mappedBy = "user")
    private Set<ChatGroupUserAdmin> userOfChatGroups;
    @OneToMany(mappedBy = "sender")
    private Set<ChatGroupRequest> chatGroupSentRequests;
    @OneToMany(mappedBy = "approvedBy")
    private Set<ChatGroupRequest> chatGroupApprovedRequests;

    //Quiz
    @OneToMany(mappedBy = "user")
    private Set<Quiz> quizzes;
}
