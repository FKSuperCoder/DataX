package com.volta.datax.projectTemplates.zhihu.v4.models;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZhiHuUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;

    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @OneToMany(mappedBy = "zhiHuUser", fetch = FetchType.EAGER) // 要和多的一方的这个变量名一致
    private List<Question> questionList;
}
