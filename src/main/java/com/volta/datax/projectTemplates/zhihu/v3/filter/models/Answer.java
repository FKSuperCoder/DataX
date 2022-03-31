package com.volta.datax.projectTemplates.zhihu.v3.filter.models;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity // 不能使用lombok@Data中的toString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "text")
    private String answerContent;
    private Date answerDate;
    private Long weightScore;

    // 一对多之中，之所以只能由多端来维护的原因：一端维护的话，每次新增一个多，就得把所有多的集合查出来，再插入，再写
    @ManyToOne
    @JoinColumn(name = "zhiHuUserId")
    private ZhiHuUser zhiHuUser;

    @ManyToOne
    @JoinColumn(name = "questionId")
    private Question question;


}

