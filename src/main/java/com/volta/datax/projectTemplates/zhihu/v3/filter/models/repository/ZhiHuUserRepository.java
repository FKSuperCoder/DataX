package com.volta.datax.projectTemplates.zhihu.v3.filter.models.repository;

import com.volta.datax.projectTemplates.zhihu.v3.filter.models.ZhiHuUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZhiHuUserRepository extends JpaRepository<ZhiHuUser, Long> {
    ZhiHuUser findByName(String name);
}
