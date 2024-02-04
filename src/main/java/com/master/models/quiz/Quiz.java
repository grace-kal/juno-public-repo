package com.master.models.quiz;

import com.master.models.shared.BaseModel;
import com.master.models.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "QUIZZES")
public class Quiz extends BaseModel {
    @Column(name = "TOTAL_POINTS")
    private  int totalPoints;

    //Relations
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "quiz")
    private Set<QuestionInQuiz> questionsInQuiz;
}
