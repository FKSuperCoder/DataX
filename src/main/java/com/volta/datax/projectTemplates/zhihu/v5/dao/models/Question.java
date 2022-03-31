package com.volta.datax.projectTemplates.zhihu.v5.dao.models;

import com.google.gson.annotations.Expose;
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
    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Expose
    @Column(unique = true)
    private String title;
    @Expose
    @Column(columnDefinition = "text")
    private String content;
    @Expose
    private Date date;
    @Expose
    private Long weightScore;
    @Expose
    @Column(unique = true)
    private String url;
    @Expose
    @ManyToOne
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JoinColumn(name = "zhiHuUserId")
    private ZhiHuUser zhiHuUser;
}
