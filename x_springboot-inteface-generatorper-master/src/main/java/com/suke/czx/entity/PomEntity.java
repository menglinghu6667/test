package com.suke.czx.entity;

/**
 * 业务描述：存储pom的基本属性
 * Project Name: x_springboot-generator
 * Package Name: com.suke.czx.entity
 * Author: zhaozhongfeng@kye-express.com
 * Date: 2018/10/11 16:03
 * Copyright (c) 2018 跨越新科技 版权所有
 */
public class PomEntity {
    private String name;
    private String versionNumber;
    private String mavenSign;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getMavenSign() {
        return mavenSign;
    }

    public void setMavenSign(String mavenSign) {
        this.mavenSign = mavenSign;
    }
}
