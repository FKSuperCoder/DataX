package com.volta.datax.projectTemplates.zhihu.v5.dao.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
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
    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Expose
    String name;

    @Expose(serialize = false)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @OneToMany(mappedBy = "zhiHuUser", fetch = FetchType.LAZY) // 要和多的一方的这个变量名一致
    private List<Question> questionList;
}
