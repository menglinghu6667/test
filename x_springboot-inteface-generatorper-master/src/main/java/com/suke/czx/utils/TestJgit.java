package com.suke.czx.utils;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

/**
 * 业务描述：
 * Project Name: x_springboot-generator
 * Package Name: com.suke.czx.utils
 * Author: menglinghu@kye-express.com
 * Date: 2018/10/12 17:26
 * Copyright (c) 2018 跨越新科技 版权所有
 */
public class TestJgit {
    @Test
    public void test() throws IOException, GitAPIException {
        //在用户的账号配置了ssh，即可提交
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        String projectURL = System.getProperty("user.dir");
        Repository repository = builder.setGitDir(new File("D:\\git\\test"+"\\.git"))
                .readEnvironment() // scan environment GIT_* variables
                .findGitDir() // scan up the file system tree
                .build();
        Git git = new Git(repository);
        AddCommand add = git.add();
        add.addFilepattern(".").call();//git add .
        CommitCommand commit = git.commit();
        /**-Dusername=%teamcity.build.username%**/
        commit.setCommitter("menglinghu", "menglinghu666@163.com");
        commit.setAuthor("menglinghu","menglinghu666@163.com");
        commit.setAll(true);
        //commit.setCommitter(new PersonIdent(repository));
        RevCommit revCommit = commit.setMessage("use jgit").call();//git commit -m "use jgit"
        String commitId = revCommit.getId().name();
        System.out.println(commitId);
        PushCommand push = git.push();
        push.call();//git push
    }


    @Test
    public void testURL(){
        String url = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(url);
        String projectURL = System.getProperty("user.dir");
        //System.out.println(projectURL.lastIndexOf("\\"));
        System.out.println(projectURL.substring(0, projectURL.lastIndexOf("\\"))+"\\.git");
    }

}
