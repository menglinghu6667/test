package com.suke.czx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.suke.czx.entity.PomEntity;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 业务描述：
 * Project Name: x_springboot-generator
 * Package Name: com.suke.czx
 * Author: zhaozhongfeng@kye-express.com
 * Date: 2018/10/11 17:44
 * Copyright (c) 2018 跨越新科技 版权所有
 */
public class PomTest {
    public static void main(String[] args) throws Exception{
        Resource resource = new ClassPathResource("pom\\maven.json");
        File file=resource.getFile();
        String content= FileUtils.readFileToString(file,"UTF-8");
        List<PomEntity> pomEntities = JSON.parseArray(content , PomEntity.class);
        for (PomEntity pomEntity:pomEntities) {
            System.out.println(pomEntity.getName());
        }
    }
}
