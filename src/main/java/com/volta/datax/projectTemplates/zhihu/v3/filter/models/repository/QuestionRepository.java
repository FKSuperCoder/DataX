package com.volta.datax.projectTemplates.zhihu.v3.filter.models.repository;

import com.volta.datax.projectTemplates.zhihu.v3.filter.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QuestionRepository extends JpaRepository<Question, Long> {
    Question findByTitle(String title);
}
