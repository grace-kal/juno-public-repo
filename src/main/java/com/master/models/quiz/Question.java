package com.master.models.quiz;

import com.master.models.enums.Points;
import com.master.models.shared.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "QUESTIONS")
public class Question extends BaseModel {
    private String content;
    private Points points;
    @Column(name = "IS_DELETED")
    private boolean isDeleted;

    //Relations
    @OneToMany(mappedBy = "question")
    private Set<Answer> answers;

    @OneToMany(mappedBy = "question")
    private Set<QuestionInQuiz> questionsInQuiz;
}
