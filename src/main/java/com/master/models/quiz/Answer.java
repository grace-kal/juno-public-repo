package com.master.models.quiz;

import com.master.models.shared.BaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ANSWERS")
public class Answer extends BaseModel {
    private String content;
    @Column(name = "IS_CORRECT")
    private boolean isCorrect;

    //Relations
    @ManyToOne
    @JoinColumn(name = "QUESTION_ID")
    private Question question;

    @OneToMany(mappedBy = "answerByUser")
    private Set<QuestionInQuiz> questionsInQuiz;
}
