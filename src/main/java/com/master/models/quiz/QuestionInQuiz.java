package com.master.models.quiz;

import com.master.models.shared.BaseModel;
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
@Table(name = "QUESTION_IN_QUIZ")
public class QuestionInQuiz extends BaseModel {
    private boolean answeredCorrectly;

    //Relations
    @ManyToOne
    @JoinColumn(name = "QUIZ_ID")
    private Quiz quiz;
    @ManyToOne
    @JoinColumn(name = "QUESTION_ID")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "GIVEN_ANSWER_ID")
    private Answer answerByUser;
}
