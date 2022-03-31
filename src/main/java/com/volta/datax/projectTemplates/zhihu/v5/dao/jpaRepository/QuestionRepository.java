package com.volta.datax.projectTemplates.zhihu.v5.dao.jpaRepository;

import com.volta.datax.projectTemplates.zhihu.v5.dao.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Date;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    Question findByUrl(String questionUrl);

    ArrayList<Question> findAllByDateAfter(Date date);
}
