package com.suke.czx.utils;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 业务描述：
 * Project Name: x_springboot-generator
 * Package Name: com.suke.czx.utils
 * Author: menglinghu@kye-express.com
 * Date: 2018/10/12 14:34
 * Copyright (c) 2018 跨越新科技 版权所有
 */
public class GitUtilClass {
    public static String localRepoPath = "D:\\git\\clone";
    public static String localRepoGitConfig = "D:\\git\\clone\\.git\\";
    public static String remoteRepoURI = "https://github.com/menglinghu666/test.git";
    public static String localCodeDir = "D:/platplat";


    public static void main(String[] args) throws GitAPIException, IOException {

        newBranch("menglinghu");
    }
    /**
     * 新建一个分支并同步到远程仓库
     * @param branchName
     * @throws IOException
     * @throws GitAPIException
     */
    public static String newBranch(String branchName) throws GitAPIException {
        UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider =new
                UsernamePasswordCredentialsProvider("menglinghu666","118982mlh");

        String newBranchIndex = "refs/heads/"+branchName;
        String gitPathURI = "";
       // Git git= Git.cloneRepository().setURI(remoteRepoURI).setDirectory(new File(localRepoPath)) .setCredentialsProvider(usernamePasswordCredentialsProvider).call();

        Git git= Git.cloneRepository().setURI(remoteRepoURI) //设置远程URI
                .setBranch("master") //设置clone下来的分支
                .setDirectory(new File(localRepoPath)) //设置下载存放路径
                .setCredentialsProvider(usernamePasswordCredentialsProvider) //设置权限验证
                .call();

        try {

            //检查新建的分支是否已经存在，如果存在则将已存在的分支强制删除并新建一个分支
            List<Ref> refs = git.branchList().call();
            for (Ref ref : refs) {
                if (ref.getName().equals(newBranchIndex)) {
                    System.out.println("Removing branch before");
                    git.branchDelete().setBranchNames(branchName).setForce(true)
                            .call();
                    break;
                }
            }
            //新建分支
            Ref ref = git.branchCreate().setName(branchName).call();
            //推送到远程
            git.push().add(ref).setCredentialsProvider(usernamePasswordCredentialsProvider).call();
            gitPathURI = remoteRepoURI + " " + "feature/" + branchName;
        } catch (GitAPIException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return gitPathURI;
    }

    public static void commitFiles() throws IOException, GitAPIException{

        UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider =new
                UsernamePasswordCredentialsProvider("menglinghu666","118982mlh");


        String filePath = "";
        Git git = Git.open( new File(localRepoGitConfig) );
        //创建用户文件的过程
        File myfile = new File(filePath);
        myfile.createNewFile();
        git.add().addFilepattern("pets").call();

        //提交
        git.commit().setMessage("Added pets").call();
        //推送到远程
        git.push().setCredentialsProvider(usernamePasswordCredentialsProvider).call();
    }

    public static boolean pullBranchToLocal(String cloneURL){
        boolean resultFlag = false;
        String[] splitURL = cloneURL.split(" ");
        String branchName = splitURL[1];
        String fileDir = localCodeDir+"/"+branchName;
        //检查目标文件夹是否存在
        File file = new File(fileDir);
        if(file.exists()){
            deleteFolder(file);
        }
        Git git;
        try {
            git = Git.open( new File(localRepoGitConfig) );
            git.cloneRepository().setURI(cloneURL).setDirectory(file).call();
            resultFlag = true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (GitAPIException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resultFlag;
    }

    public static void deleteFolder(File file){
        if(file.isFile() || file.list().length==0){
            file.delete();
        }else{
            File[] files = file.listFiles();
            for(int i=0;i<files.length;i++){
                deleteFolder(files[i]);
                files[i].delete();
            }
        }
    }

    public static void setupRepo() throws GitAPIException{
        //建立与远程仓库的联系，仅需要执行一次
        Git git = Git.cloneRepository().setURI(remoteRepoURI).setDirectory(new File(localRepoPath)).call();
    }

}
