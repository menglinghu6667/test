package com.suke.czx.controller;

import com.alibaba.fastjson.JSON;
import com.suke.czx.entity.PomEntity;
import com.suke.czx.utils.GenProjectUtils;
import com.suke.czx.utils.R;
import com.suke.czx.service.SysGeneratorService;
import com.suke.czx.utils.PageUtils;
import com.suke.czx.utils.Query;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2016年12月19日 下午9:12:58
 */
@Controller
@RequestMapping("/sys/generator" )
public class SysGeneratorController {
    @Autowired
    private SysGeneratorService sysGeneratorService;

    /**
     * 列表
     */
    @ResponseBody
    @RequestMapping("/list" )
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<Map<String, Object>> list = sysGeneratorService.queryList(query);
        int total = sysGeneratorService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(list, total, query.getLimit(), query.getPage());

        return R.ok().put("page" , pageUtil);
    }

    /**
     * 生成代码
     */
    @RequestMapping("/code" )
    public void code(HttpServletRequest request, HttpServletResponse response, ArrayList<PomEntity> servicePom) throws IOException {
        PomEntity pomEntity1 = new PomEntity();
        pomEntity1.setName("shiro-core" );
        pomEntity1.setVersionNumber("1.4.0" );
        pomEntity1.setMavenSign("\t\t<dependency>\n" +
                "\t\t\t<groupId>org.apache.shiro</groupId>\n" +
                "\t\t\t<artifactId>shiro-core</artifactId>\n" +
                "\t\t\t<version>1.4.0</version>\n" +
                "\t\t</dependency>" );
        PomEntity pomEntity2 = new PomEntity();
        pomEntity2.setName("junit" );
        pomEntity2.setVersionNumber("4.12" );
        pomEntity2.setMavenSign("\t\t<dependency>\n" +
                "\t\t\t<groupId>junit</groupId>\n" +
                "\t\t\t<artifactId>junit</artifactId>\n" +
                "\t\t\t<version>4.12</version>\n" +
                "\t\t\t<scope>test</scope>\n" +
                "\t\t</dependency>" );
        servicePom.add(pomEntity1);
        servicePom.add(pomEntity2);

        PomEntity pomEntity3 = new PomEntity();
        pomEntity3.setName("jackson-core" );
        pomEntity3.setVersionNumber("2.9.7" );
        pomEntity3.setMavenSign("\t\t<dependency>\n" +
                "\t\t\t<groupId>com.fasterxml.jackson.core</groupId>\n" +
                "\t\t\t<artifactId>jackson-core</artifactId>\n" +
                "\t\t\t<version>2.9.7</version>\n" +
                "\t\t</dependency>" );
        PomEntity pomEntity4 = new PomEntity();
        pomEntity4.setName("commons-lang" );
        pomEntity4.setVersionNumber("2.6" );
        pomEntity4.setMavenSign("\t\t<dependency>\n" +
                "\t\t\t<groupId>commons-lang</groupId>\n" +
                "\t\t\t<artifactId>commons-lang</artifactId>\n" +
                "\t\t\t<version>2.6</version>\n" +
                "\t\t</dependency>" );
        servicePom.add(pomEntity3);
        servicePom.add(pomEntity4);
        //初始化数据完毕
        //-------------------------------------------------------------------------------------------------------------
        //处理数据
        String servicePomString = "";
        for (PomEntity pom : servicePom) {
            servicePomString += pom.getMavenSign();
            servicePomString += "\n";
        }

        String[] tableNames = new String[]{};
        String tables = request.getParameter("tables" );
        tableNames = JSON.parseArray(tables).toArray(tableNames);

        byte[] data = sysGeneratorService.generatorCode(tableNames , servicePomString);

        response.reset();
        response.setHeader("Content-Disposition" , "attachment; filename=\"x-springboot.zip\"" );
        response.addHeader("Content-Length" , "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8" );

        IOUtils.write(data, response.getOutputStream());
    }

    /**
     * 生成代码
     */
    @RequestMapping("/project" )
    public void project(HttpServletRequest request, HttpServletResponse response, ArrayList<PomEntity> servicePom) throws IOException {
        PomEntity pomEntity1 = new PomEntity();
        pomEntity1.setName("shiro-core" );
        pomEntity1.setVersionNumber("1.4.0" );
        pomEntity1.setMavenSign("\t\t<dependency>\n" +
                "\t\t\t<groupId>org.apache.shiro</groupId>\n" +
                "\t\t\t<artifactId>shiro-core</artifactId>\n" +
                "\t\t\t<version>1.4.0</version>\n" +
                "\t\t</dependency>" );
        PomEntity pomEntity2 = new PomEntity();
        pomEntity2.setName("junit" );
        pomEntity2.setVersionNumber("4.12" );
        pomEntity2.setMavenSign("\t\t<dependency>\n" +
                "\t\t\t<groupId>junit</groupId>\n" +
                "\t\t\t<artifactId>junit</artifactId>\n" +
                "\t\t\t<version>4.12</version>\n" +
                "\t\t\t<scope>test</scope>\n" +
                "\t\t</dependency>" );
        servicePom.add(pomEntity1);
        servicePom.add(pomEntity2);

        PomEntity pomEntity3 = new PomEntity();
        pomEntity3.setName("jackson-core" );
        pomEntity3.setVersionNumber("2.9.7" );
        pomEntity3.setMavenSign("\t\t<dependency>\n" +
                "\t\t\t<groupId>com.fasterxml.jackson.core</groupId>\n" +
                "\t\t\t<artifactId>jackson-core</artifactId>\n" +
                "\t\t\t<version>2.9.7</version>\n" +
                "\t\t</dependency>" );
        PomEntity pomEntity4 = new PomEntity();
        pomEntity4.setName("commons-lang" );
        pomEntity4.setVersionNumber("2.6" );
        pomEntity4.setMavenSign("\t\t<dependency>\n" +
                "\t\t\t<groupId>commons-lang</groupId>\n" +
                "\t\t\t<artifactId>commons-lang</artifactId>\n" +
                "\t\t\t<version>2.6</version>\n" +
                "\t\t</dependency>" );
        servicePom.add(pomEntity3);
        servicePom.add(pomEntity4);
        //初始化数据完毕
        //-------------------------------------------------------------------------------------------------------------
        //处理数据
        String servicePomString = "";
        for (PomEntity pom : servicePom) {
            servicePomString += pom.getMavenSign();
            servicePomString += "\n";
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        GenProjectUtils.generatorCode(zip, servicePomString);
        IOUtils.closeQuietly(zip);
        byte[] data = outputStream.toByteArray();
        response.reset();
        response.setHeader("Content-Disposition" , "attachment; filename=\"x-springboot.zip\"" );
        response.addHeader("Content-Length" , "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8" );

        IOUtils.write(data, response.getOutputStream());
    }

    @ResponseBody
    @RequestMapping("/findAllMaven" )
    public List<PomEntity> findAllMaven() throws IOException {
        Resource resource = new ClassPathResource("pom\\maven.json");
        File file=resource.getFile();
        String content= FileUtils.readFileToString(file,"UTF-8");
        List<PomEntity> pomEntities = JSON.parseArray(content , PomEntity.class);
        return pomEntities;
    }
}
