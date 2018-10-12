package com.suke.czx.utils;

import com.suke.czx.entity.ColumnEntity;
import com.suke.czx.entity.PomEntity;
import com.suke.czx.entity.TableEntity;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器   工具类
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2016年12月19日 下午11:40:24
 */
public class GenProjectUtils {

    public static List<String> getTemplates() {

        List<String> templates = new ArrayList<String>();

        templates.add("template/pom_parent.vm");
        templates.add("template/.gitignore.vm");
        templates.add("template/README.md.vm");

        //---------------------api----------------------------
        templates.add("template/api/package-info.java.vm");
        templates.add("template/api/pom_api.vm");

        //----------------------common------------------------------
        templates.add("template/common/BaseMapper.java.vm");
        templates.add("template/common/pom_common.vm");
        templates.add("template/common/IdentifiesUtils.java.vm");
        templates.add("template/common/DateUtils.java.vm");
        templates.add("template/common/ErrorCode.java.vm");

        //----------------------dao-------------------------------
        templates.add("template/dao/main/package-info.xml.vm");
        templates.add("template/dao/main/package-info.java.vm");
        templates.add("template/dao/pom_dao.vm");
        templates.add("template/dao/test/RunApplication.java.vm");
        templates.add("template/dao/test/Test.java.vm");
        templates.add("template/dao/main/MybatisConfig.vm");


        //----------------------server----------------------------------
        templates.add("template/server/main/package-info.java.vm");
        templates.add("template/server/main/RunApplication.java.vm");
        templates.add("template/server/main/application.yml.vm");
        templates.add("template/server/main/logback.xml.vm");
        templates.add("template/server/test/RunApplication.java.vm");
        templates.add("template/server/test/ControllerTest.java.vm");
        templates.add("template/server/pom_server.vm");
        templates.add("template/server/main/SwaggerConfig.java.vm");


        //------------------------Service------------------------------------------
        templates.add("template/service/main/package-info-service.java.vm");
        templates.add("template/service/main/package-info-serviceImpl.java.vm");
        templates.add("template/service/test/RunApplication.java.vm");
        templates.add("template/service/test/ServiceTest.java.vm");
        templates.add("template/service/pom_service.vm");

        return templates;
    }


    /**
     * 生成代码
     */
    public static void generatorCode(ZipOutputStream zip, String servicePomString) {
        //配置信息
        Configuration config = getConfig();
        boolean hasBigDecimal = false;
        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
        String mainPath = config.getString("mainPath");
        mainPath = StringUtils.isBlank(mainPath) ? "x-springboot" : mainPath;
        //封装模板数据
        Map<String, Object> map = new HashMap<>();
        map.put("hasBigDecimal", hasBigDecimal);
        map.put("mainPath", mainPath);
        map.put("package", config.getString("package"));
        map.put("packageProject" , config.getString("package")+"."+config.getString("project").replace("-" , "."));
        map.put("project", config.getString("project"));
        map.put("email", config.getString("email"));
        map.put("datasourceUrl", config.getString("datasourceUrl"));
        map.put("datasourceUsername", config.getString("datasourceUsername"));
        map.put("datasourcePassword", config.getString("datasourcePassword"));
        map.put("port", config.getString("port"));
        map.put("contextPath", config.getString("contextPath"));
        map.put("baseResultMap", true);
        map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        map.put("servicePom" , servicePomString);
        VelocityContext context = new VelocityContext(map);

        //获取模板列表
        List<String> templates = getTemplates();
        for (String template : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);

            try {
                //添加到zip
                zip.putNextEntry(new ZipEntry(getFileName(template, config.getString("package"))));
                IOUtils.write(sw.toString(), zip, "UTF-8");
                IOUtils.closeQuietly(sw);
                zip.closeEntry();
            } catch (IOException e) {
                throw new RRException("渲染模板失败，表名：", e);
            }
        }
    }


    /**
     * 列名转换成Java属性名
     */
    public static String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 表名转换成Java类名
     */
    public static String tableToJava(String tableName, String tablePrefix) {
        if (StringUtils.isNotBlank(tablePrefix)) {
            tableName = tableName.replace(tablePrefix, "");
        }
        return columnToJava(tableName);
    }

    /**
     * 获取配置信息
     */
    public static Configuration getConfig() {
        try {
            return new PropertiesConfiguration("generator.properties");
        } catch (ConfigurationException e) {
            throw new RRException("获取配置文件失败，", e);
        }
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template, String packageName) {
        Configuration config = getConfig();
        String projectName = config.getString("project");
        String basepath = projectName + File.separator;
        String api = basepath + projectName + "-api" + File.separator;
        String common = basepath + projectName + "-common" + File.separator;
        String dao = basepath + projectName + "-dao" + File.separator;
        String server = basepath + projectName + "-server" + File.separator;
        String service = basepath + projectName + "-service" + File.separator;

        String directory = "src" + File.separator;

        String packagePath = "";
        String projectPath = "";

        if (StringUtils.isNotBlank(packageName)) {
            packagePath = packageName.replace(".", File.separator) + File.separator;
        }
        if (StringUtils.isNotBlank(projectName)){
            projectPath = projectName.replace("-" , File.separator) + File.separator;
        }
        if (template.equals("template/pom_parent.vm")) {
            return basepath + "pom.xml";
        }
        if (template.equals("template/.gitignore.vm")) {
            return basepath + ".gitignore";
        }
        if (template.equals("template/README.md.vm")) {
            return basepath + "README.md";
        }

        //----------------------------------------------------------API-----------------------------------------------------------------------------
        if (template.equals("template/api/package-info.java.vm")) {
            return api + directory + "main" + File.separator + "java" + File.separator + packagePath + projectPath + "domain" + File.separator + "entity" + File.separator + "package-info.java";
        }
        if (template.equals("template/api/pom_api.vm")) {
            return basepath + projectName + "-api" + File.separator + "pom.xml";
        }
        //----------------------------------------------------COMMON-----------------------------------------------------------------------------------
        if (template.equals("template/common/BaseMapper.java.vm")) {
            return common + directory + "main" + File.separator + "java" + File.separator + packagePath + "orm" + File.separator + "BaseMapper.java";
        }
        if (template.equals("template/common/IdentifiesUtils.java.vm")) {
            return common + directory + "main" + File.separator + "java" + File.separator + packagePath + "utils" + File.separator + "IdentifiesUtils.java";
        }
        if (template.equals("template/common/DateUtils.java.vm")) {
            return common + directory + "main" + File.separator + "java" + File.separator + packagePath + "utils" + File.separator + "DateUtils.java";
        }
        if (template.equals("template/common/ErrorCode.java.vm")) {
            return common + directory + "main" + File.separator + "java" + File.separator + packagePath + "exception" + File.separator + "ErrorCode.java.vm";
        }
        if (template.equals("template/common/pom_common.vm")) {
            return basepath + projectName + "-common" + File.separator + "pom.xml";
        }

        //-----------------------------------------------------DA0---------------------------------------------------------------------------------------
        if (template.equals("template/dao/main/package-info.xml.vm")) {
            return dao + directory + "main" + File.separator + "resources" + File.separator + "mappers" + File.separator + "package-info.xml";
        }
        if (template.equals("template/dao/main/MybatisConfig.vm")) {
            return dao + directory + "main" + File.separator + "java" + File.separator + packagePath + projectPath + "config" + File.separator + "MybatisConfig.java";
        }
        if (template.equals("template/dao/main/package-info.java.vm")) {
            return dao + directory + "main" + File.separator + "java" + File.separator + packagePath + projectPath + "domain" + File.separator + "mappers" + File.separator + "package-info.java";
        }

        if (template.equals("template/dao/pom_dao.vm")) {
            return basepath + projectName + "-dao" + File.separator + "pom.xml";
        }

        if (template.equals("template/dao/test/RunApplication.java.vm")) {
            return dao + directory + "test" + File.separator + "java" + File.separator + packagePath + projectPath + "RunApplication.java";
        }

        if (template.equals("template/dao/test/Test.java.vm")) {
            return dao + directory + "test" + File.separator + "java" + File.separator + packagePath + projectPath + "test" + File.separator + "package-info.java";
        }


        //------------------------------------------------------SERVER-----------------------------------------------------------------------------------------
        if (template.equals("template/server/main/RunApplication.java.vm")) {
            return server + directory + "main" + File.separator + "java" + File.separator + packagePath + projectPath + "RunApplication.java";
        }
        if (template.equals("template/server/main/package-info.java.vm")) {
            return server + directory + "main" + File.separator + "java" + File.separator + packagePath + projectPath + "controller" + File.separator + "HelloWorldController.java";
        }

        if (template.equals("template/server/main/SwaggerConfig.java.vm")) {
            return server + directory + "main" + File.separator + "java" + File.separator + packagePath + projectPath + "config" + File.separator + "SwaggerConfig.java";
        }

        if (template.equals("template/server/main/application.yml.vm")) {
            return server + directory + "main" + File.separator + "resources" + File.separator + "application.yml";
        }

        if (template.equals("template/server/main/logback.xml.vm")) {
            return server + directory + "main" + File.separator + "resources" + File.separator + "logback.xml";
        }

        if (template.equals("template/server/test/RunApplication.java.vm")) {
            return server + directory + "test" + File.separator + "java" + File.separator + packagePath + projectPath + "RunApplication.java";
        }

        if (template.equals("template/server/test/ControllerTest.java.vm")) {
            return server + directory + "test" + File.separator + "java" + File.separator + packagePath + projectPath + "controllerTest" + File.separator + "package-info.java";
        }

        if (template.equals("template/server/pom_server.vm")) {
            return basepath + projectName + "-server" + File.separator + "pom.xml";
        }


        //------------------------------------------------------SERVICE-----------------------------------------------------------------------------------------
        if (template.contains("template/service/main/package-info-service.java.vm")) {
            return service + directory + "main" + File.separator + "java" + File.separator + packagePath + projectPath + "service" + File.separator + "package-info.java";
        }
        if (template.contains("template/service/main/package-info-serviceImpl.java.vm")) {
            return service + directory + "main" + File.separator + "java" + File.separator + packagePath + projectPath + "service" + File.separator + "impl" + File.separator + "package-info.java";
        }

        if (template.contains("template/service/test/RunApplication.java.vm")) {
            return service + directory + "main" + File.separator + "java" + File.separator + packagePath + projectPath + "RunApplication.java";
        }
        if (template.contains("template/service/test/ServiceTest.java.vm")) {
            return service + directory + "test" + File.separator + "java" + File.separator + packagePath + projectPath + "test" + File.separator + "ServiceTest.java";
        }
        if (template.equals("template/service/pom_service.vm")) {
            return basepath + projectName + "-service" + File.separator + "pom.xml";
        }
        return null;
    }


    //首字母转小写
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }
}
