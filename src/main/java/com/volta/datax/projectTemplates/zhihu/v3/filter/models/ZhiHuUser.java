package com.volta.datax.projectTemplates.zhihu.v3.filter.models;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZhiHuUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    // fetch 是用来定义级联查询的方式，cascade 用来管理级联插入和修改。

    // 一对多中，一方有多个一对多关系，List需要写成Set
    @OneToMany(mappedBy = "zhiHuUser", fetch = FetchType.EAGER) // mappedBy声明一不是一对多关系的维护方，同时避免生成的多余的表
    private Set<Answer> answerList;

    @OneToMany(mappedBy = "zhiHuUser", fetch = FetchType.EAGER)
    private Set<Question> questionList;


}
