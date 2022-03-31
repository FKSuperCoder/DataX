package com.volta.datax.projectTemplates.zhihu.v3.downloader.models.layer0;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 知乎用户类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZhiHuUserPData {

    private String userId; // 知乎用户id
    private String intro; // 用户简介

}
