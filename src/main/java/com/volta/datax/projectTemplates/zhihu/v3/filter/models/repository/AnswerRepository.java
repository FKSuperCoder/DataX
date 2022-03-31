package com.volta.datax.projectTemplates.zhihu.v3.filter.models.repository;

import com.volta.datax.projectTemplates.zhihu.v3.filter.models.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Query(value = "select * from answer where zhi_hu_user_id=? and question_id=? ", nativeQuery = true)
    Answer getByZhiHuUserIdAndQuestionId(Long zhiHuUserId, Long questionId);

}
