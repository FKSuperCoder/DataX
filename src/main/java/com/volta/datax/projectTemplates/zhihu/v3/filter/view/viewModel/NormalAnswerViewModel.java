package com.volta.datax.projectTemplates.zhihu.v3.filter.view.viewModel;

import com.volta.datax.projectTemplates.zhihu.v3.downloader.models.layer0.ZhiHuUserPData;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class NormalAnswerViewModel {
    String questionKeyword;
    ZhiHuUserPData answerer;
    String answerContent;
    Date answerDate;

}
