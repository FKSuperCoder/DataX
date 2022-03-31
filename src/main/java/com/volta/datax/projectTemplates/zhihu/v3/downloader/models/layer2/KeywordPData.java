package com.volta.datax.projectTemplates.zhihu.v3.downloader.models.layer2;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.volta.datax.core.data.downloader.utils.JsonUtils;
import com.volta.datax.projectTemplates.zhihu.v3.downloader.models.layer1.QuestionPData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeywordPData {
    @Expose
    String questionKeyword; // 知乎问题的搜索关键字
    @Expose
    ArrayList<QuestionPData> questionPDataList;


    @Expose
    Boolean isComplete;


    public static KeywordPData readKeywordFromJsonDataFile(String fileFullPath) throws IOException {
        String jsonStr = JsonUtils.readJsonFileAsString(fileFullPath);
        return new Gson().fromJson(jsonStr, new TypeToken<KeywordPData>() {
        }.getType());
    }

    public static QuestionPData searchFromQuestionListByUrl(ArrayList<QuestionPData> questionPDataList, String url) {
        for (QuestionPData questionPData : questionPDataList) {
            if (questionPData.getQuestionUrl().equals(url)) return questionPData;
        }
        return null;
    }


}
