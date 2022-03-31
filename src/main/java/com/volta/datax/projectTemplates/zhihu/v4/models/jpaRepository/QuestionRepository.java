package com.volta.datax.projectTemplates.zhihu.v4.models.jpaRepository;

import com.volta.datax.projectTemplates.zhihu.v4.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

}
