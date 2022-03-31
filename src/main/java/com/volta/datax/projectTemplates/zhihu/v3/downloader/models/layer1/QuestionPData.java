package com.volta.datax.projectTemplates.zhihu.v3.downloader.models.layer1;

import com.google.gson.annotations.Expose;
import com.volta.datax.projectTemplates.zhihu.v3.downloader.models.layer0.AnswerPData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionPData {

    @Expose
//    private ZhiHuUser quizzer; // todo 后期通过selenium获取
    private String questionHeader;
    @Expose
    private String questionContent;
    @Expose
    private Date questionDate;
    //    private ArrayList<Comment> comments;// todo 后期通过selenium获取
    @Expose
    private ArrayList<AnswerPData> answerPData;


    @Expose
    private String questionUrl;
    @Expose
    private Boolean isComplete;


}
