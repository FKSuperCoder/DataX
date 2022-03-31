package com.volta.datax.projectTemplates.zhihu.v3.filter.models;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "text")
    private String content;
    private Date date;
    private Long weightScore;
    private String url;


    @ManyToOne
    @JoinColumn(name = "zhiHuUserId")
    private ZhiHuUser zhiHuUser; // 其中可以找到本回答的用户

    @OneToMany
    @JoinColumn(name = "questionId")
    private List<Answer> answerList;


}
