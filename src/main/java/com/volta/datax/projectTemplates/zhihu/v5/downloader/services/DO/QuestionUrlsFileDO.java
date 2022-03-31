package com.volta.datax.projectTemplates.zhihu.v5.downloader.services.DO;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Builder
@Data
public class QuestionUrlsFileDO {
    private String keyword;
    private ArrayList<String> urls;
}
