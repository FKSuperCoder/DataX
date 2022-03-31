package com.volta.datax.projectTemplates.zhihu.v4.models;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    private String title;
    @Column(columnDefinition = "text")
    private String content;
    private Date date;
    private Long weightScore;
    @Column(unique = true)
    private String url;

    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @ManyToOne
    @JoinColumn(name = "zhiHuUserId")
    private ZhiHuUser zhiHuUser;
}
