package com.volta.datax.projectTemplates.zhihu.v3.downloader.models.layer0;

import java.util.Date;

/**
 * 知乎的评论
 * 评论的范围限定在某个问题的问题内容、回答内容之中
 */
public class CommentPData {
    private ZhiHuUserPData commenter;
    private String commentContent;
    private Date commentDate;
}
