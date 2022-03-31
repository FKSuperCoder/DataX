package com.volta.datax.projectTemplates.zhihu.v3.downloader.models.layer0;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerPData {

    private ZhiHuUserPData answerer;
    private String answerContent;
    private Date answerDate;
//    private ArrayList<Comment> comments;// todo 后期通过selenium获取

}
