package Jgit;

import org.kohsuke.github.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.io.FileWriter;
import java.io.IOException;
import static org.kohsuke.github.GHIssueState.CLOSED;

public class GitHubTest {
//   final static String login = "";
//   final static String passwd = "";
    final static String login = "lxm0";
    final static String passwd = "githublxm0";



    public static void main(String[] args){

        //获取GitHub的commit
       //getGitHubCommit();
        //获取仓库分支
        // getGitHubBranches();
        //获取GitHubGitHubPullRequestInfo
        getGitHubPullRequestInfo();
        //获取GitHub提交信息
        // getGitHubCommitInfo();
        //获取commit文件
        //getGitHubCommitFiles();
        //获取用户所有仓库
        //getGitHubUserRepo();
        //获取仓库fork用户
        //getGitHubRepoForkUser();
        //获取仓库贡献者
        //getGitHubRepoContributors();
    }
    public void writeArrayToExcel(String[][] data, String string) {
        int rowNum = data.length;
        int columnNum = data[0].length;
        try {
            FileWriter fw = new FileWriter(string);
            for (int i = 0; i < rowNum; i++) {
                for (int j = 0; j < columnNum; j++)
                    fw.write(data[i][j]+ "\t"); // tab 间隔
                fw.write("\n"); // 换行
            }
            fw.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    public static void getGitHub(){
        try {
            GitHub github = GitHub.connectUsingPassword(login,passwd);
            GHRepository repo = github.createRepository(
                    "new-repository", "this is my new repository",
                    "http://www.kohsuke.org/", true/*public*/);
            repo.addCollaborators(github.getUser("abayer"), github.getUser("rtyler"));
            repo.delete();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public static void getGitHubCommit(){
        try {
            GitHub github = GitHub.connectUsingPassword(login,passwd);
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            GHRepository ghRepository = github.getRepository("python/cpython");
            ghRepository.listCommits();
            int count= 0;

            for(GHCommit commit:ghRepository.listCommits()){
                count++;
                System.out.println( count +" **********************************");
                System.out.println( "提交人： "+commit.getCommitShortInfo().getAuthor().getName());
                System.out.println( "提交SHA1： "+commit.getSHA1());
                System.out.println( "提交信息： "+commit.getCommitShortInfo().getMessage());
                System.out.println( "提交时间： "+format.format(commit.getCommitShortInfo().getCommitDate()));
            }
            System.out.println(count);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public static void getGitHubBranches(){
        try{
            GitHub github = GitHub.connectUsingPassword(login,passwd);
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            GHRepository ghRepository = github.getRepository("kohsuke/github-api");
            Map<String, GHBranch> branches =  ghRepository.getBranches();
            System.out.println("BranchesCount: "+branches.size());
            for (Map.Entry<String, GHBranch> entry : branches.entrySet()) {
                System.out.println("key = " + entry.getKey() + ", Value = " + entry.getValue());
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void getGitHubPullRequestInfo(){
        try{
            GitHub github = GitHub.connectUsingPassword(login,passwd);
            GHRepository ghRepository = github.getRepository("activemerchant/active_merchant");
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            GHPullRequestQueryBuilder pullRequestQueryBuilder = ghRepository.queryPullRequests();
            GHIssueState ghIssueState=CLOSED;
            List<GHPullRequest> ghPullRequestList = ghRepository.getPullRequests(ghIssueState);
            PagedIterable<GHPullRequest> ghPullRequests =  pullRequestQueryBuilder.list();
            Date startTime = new Date(112,00,01);
            Date endTime = new Date(116,9,01);
            int count  = 0;
            int mergenum = 0;
            String [][] demands= new String[1650][9];
            demands[0][0] = "创建时间";
            demands[0][1] = "标题";
            demands[0][2] = "作者";
            demands[0][3] = "HtmlUrl";
            demands[0][4] = "merge状态";
            demands[0][5] = "merge数";
            demands[0][6] = "评审时间1";
            demands[0][7] = "评审时间2";
            demands[0][8] = "merge日期";
            for(GHPullRequest ghPullRequest:ghPullRequestList){
                if(ghPullRequest.getCreatedAt().getTime()<startTime.getTime()||ghPullRequest.getCreatedAt().getTime()>endTime.getTime()){
                   continue;
                }
                count++;
                ghPullRequest.getComments();
                System.out.println("************* "+count+" *************");
                System.out.println("创建时间 "+format.format(ghPullRequest.getCreatedAt()));
                System.out.println("标题:  "+ ghPullRequest.getTitle());
                System.out.println("作者:  "+ ghPullRequest.getUser().getName());
//                System.out.println("状态:  "+ ghPullRequest.getState());
//                System.out.println("提交数量:  "+ ghPullRequest.getCommits());
//                System.out.println("提交数量:  "+ ghPullRequest.listFiles());
                System.out.println("HtmlUrl:  "+ ghPullRequest.getHtmlUrl());
                System.out.println("merge日期:  "+ ghPullRequest.getMergedAt());
                System.out.println("merge状态:  "+ ghPullRequest.isMerged());
                System.out.println("评审时间:"+ (ghPullRequest.getClosedAt().getTime()-ghPullRequest.getCreatedAt().getTime())/(1000*24*60*60));
                if(ghPullRequest.isMerged()){
                    System.out.println("merge数：" + mergenum++);
                }
                demands[count][0] = format.format(ghPullRequest.getCreatedAt());
                demands[count][1] = ghPullRequest.getTitle();
                demands[count][2] = ghPullRequest.getUser().getName();
                demands[count][3] = ghPullRequest.getHtmlUrl().toString();
                demands[count][4] = ""+ghPullRequest.isMerged();
                demands[count][5] = ""+mergenum;
                demands[count][6] = ""+(ghPullRequest.getClosedAt().getTime()-ghPullRequest.getCreatedAt().getTime())/(1000*24*60*60);

                demands[count][8] = ""+ghPullRequest.getMergedAt();
//
//                System.out.println("评论数量:  "+ ghPullRequest.getCommentsCount());
//                System.out.println("审查数量:  "+ ghPullRequest.getReviewComments());
//                for(GHPullRequestFileDetail ghPullRequestFileDetail :ghPullRequest.listFiles()){
//                    System.out.println("Patch： "+ghPullRequestFileDetail.getPatch());
//                }
//                for(GHIssueComment comment:ghPullRequest.getComments()){
//                    System.out.println("评论者姓名： "+comment.getUser().getName());
//                    System.out.println("评论内容： "+comment.getBody());
//                }
                for(GHPullRequestReviewComment comment:ghPullRequest.listReviewComments()){
//                    System.out.println("审查者姓名： "+comment.getUser().getName());
//                    System.out.println("审查者id： "+comment.getUser().getId());
//                    System.out.println("审查内容： "+comment.getBody());
                    System.out.println("审查评论ID： "+comment.getId());
                    if(comment.getCreatedAt()!=null) {
                        demands[count][7] = "" + (comment.getCreatedAt().getTime() - ghPullRequest.getCreatedAt().getTime()) / (1000 * 24 * 60 * 60);
                    }
                }

//                if (ghPullRequest.isMerged()){break;}
            }
            GitHubTest wa = new GitHubTest();
            wa.writeArrayToExcel(demands, "test.xls");

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public static void getGitHubCommitInfo(){
        try{
            GitHub github = GitHub.connectUsingPassword(login,passwd);
            GHRepository ghRepository = github.getRepository("kohsuke/github-api");
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            GHCommitQueryBuilder ghCommitQueryBuilder = ghRepository.queryCommits();
            int count = 0;
            for(GHCommit commit:ghCommitQueryBuilder.list()){
                count++;
                System.out.println("******* "+count+" *******");
                System.out.println("创建时间: "+format.format(commit.getCommitDate()));
                System.out.println("作者: "+commit.getCommitShortInfo().getAuthor().getName());
                System.out.println("提交SHA1: "+commit.getSHA1());
                System.out.println("提交HtmlUrl: "+commit.getHtmlUrl());
                System.out.println("提交增加的行数: "+commit.getLinesAdded());
                System.out.println("提交删除的行数: "+commit.getLinesDeleted());
                System.out.println("提交改变的行数: "+commit.getLinesChanged());
                //System.out.println("提交信息: "+commit.getCommitShortInfo().getMessage());
                // System.out.println("提交评论数量: "+commit.getCommitShortInfo().getCommentCount());

            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public static void getGitHubCommitFiles(){
        try{
            GitHub github = GitHub.connectUsingPassword(login,passwd);
            GHRepository ghRepository = github.getRepository("python/cpython");
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            GHCommit commit =  ghRepository.getCommit("689d555ec135d4115574addd063c358ac4897cc4");
            System.out.println("提交时间： "+format.format(commit.getCommitDate()));
            System.out.println("提交作者： "+commit.getAuthor().getName());
            commit.getTree().getTree();
            for(GHCommit.File file:commit.getFiles()){
                System.out.println("文件名：  "+file.getFileName());
                System.out.println("BlobUrl： "+file.getBlobUrl());
                System.out.println("Patch:  "+file.getPatch());
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public static void getGitHubUserRepo(){
        try{
            GitHub github = GitHub.connectUsingPassword(login,passwd);
            GHRepository ghRepository = github.getRepository("kohsuke/github-api");
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int count =0;
            for(GHRepository repository:github.getUser("kohsuke").listStarredRepositories()){
                count  ++;
                System.out.println("StarredRepositories: "+repository.getName());
            }
            System.out.println("Star仓库数量 : "+count);

            count =0;
            for(Map.Entry<String, GHRepository> entry:github.getUser("kohsuke").getRepositories().entrySet()){
                count++;
                System.out.println(" 仓库名： " + entry.getKey() );
            }
            System.out.println("所有仓库数量 : "+count);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public static void getGitHubRepoForkUser(){
        try{
            GitHub github = GitHub.connectUsingPassword(login,passwd);
            GHRepository ghRepository = github.getRepository("kohsuke/github-api");
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int count =0;
            for(GHRepository repository:ghRepository.listForks()){
                count++;
                System.out.println("fork仓库名： "+repository.getFullName());
            }
           System.out.println("fork数量： "+count);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public static void getGitHubRepoContributors(){
        try{
            GitHub github = GitHub.connectUsingPassword(login,passwd);
            GHRepository ghRepository = github.getRepository("kohsuke/github-api");
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int count =0;
           for(GHRepository.Contributor contributor:ghRepository.listContributors()){
               count++;
               System.out.println("仓库贡献者姓名： "+contributor.getName());

           }
            System.out.println("仓库贡献者: "+count);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


}
