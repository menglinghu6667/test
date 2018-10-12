package com.suke.czx.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 业务描述：
 * Project Name: x_springboot-generator
 * Package Name: com.suke.czx.utils
 * Author: menglinghu@kye-express.com
 * Date: 2018/10/12 10:06
 * Copyright (c) 2018 跨越新科技 版权所有
 */
public class JGitUtil {
    public String remotePath = "git@github.com:menglinghu666/test.git";//远程库路径
    public String localPath = "D:\\git\\clone";//下载已有仓库到本地路径
    public String initPath = "D:\\git\\test\\";//本地路径新建

    /**
     * 克隆远程库
     *
     * @throws IOException
     * @throws GitAPIException
     */
    @Test
    public void testClone() throws IOException, GitAPIException {
        //设置远程服务器上的用户名和密码
        UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider = new
                UsernamePasswordCredentialsProvider("menglinghu666", "118982mlh");

        //克隆代码库命令
        CloneCommand cloneCommand = Git.cloneRepository();

        Git git = cloneCommand.setURI(remotePath) //设置远程URI
                .setBranch("master") //设置clone下来的分支
                .setDirectory(new File(localPath)) //设置下载存放路径
                .setCredentialsProvider(usernamePasswordCredentialsProvider) //设置权限验证
                .call();

        System.out.print(git.tag());
    }


    /**
     * 本地新建仓库
     */
    @Test
    public void testCreate() throws IOException {
        //本地新建仓库地址
        Repository newRepo = FileRepositoryBuilder.create(new File(localPath + "/.git"));
        newRepo.create();
    }

    /**
     * 本地仓库新增文件
     */
    @Test
    public void testAdd() throws IOException, GitAPIException {
        File myfile = new File(localPath + "/myfile.txt");
        myfile.createNewFile();
        //git仓库地址
        Git git = new Git(new FileRepository(localPath + "/.git"));

        //添加文件
        git.add().addFilepattern("myfile").call();
    }

    /**
     * 本地提交代码
     */
    @Test
    public void testCommit() throws IOException, GitAPIException,
            JGitInternalException {
        //git仓库地址
        Git git = new Git(new FileRepository(localPath + "/.git"));
        //提交代码
        git.commit().setMessage("test jGit").call();
    }


    /**
     * 拉取远程仓库内容到本地
     */
    @Test
    public void testPull() throws IOException, GitAPIException {

        UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider = new
                UsernamePasswordCredentialsProvider("menglinghu666", "118982mlh");
        //git仓库地址
        Git git = new Git(new FileRepository(localPath + "/.git"));
        git.pull().setRemoteBranchName("master").
                setCredentialsProvider(usernamePasswordCredentialsProvider).call();
    }

    /**
     * push本地代码到远程仓库地址
     */
    @Test
    public void testPush() throws IOException, JGitInternalException,
            GitAPIException {

        UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider = new
                UsernamePasswordCredentialsProvider("menglinghu666", "118982mlh");
        //git仓库地址
        Git git = new Git(new FileRepository(localPath + "/.git"));

        git.push().setRemote("origin").setCredentialsProvider(usernamePasswordCredentialsProvider).call();
    }


}
