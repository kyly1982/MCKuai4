package com.mckuai.imc.bean;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by kyly on 2015/9/29.
 */
public class MCUser implements Serializable {
    private int id;// 用户id
    private int score;// 当前积分
    private int isServerActor;// 是否是腐竹或者名人 1:腐竹 2:名人 3:腐竹申请 4:名人申请
    private int talkNum;// 发帖数
    private int homeNum;// 小屋数
    private int dynamicNum;// 动态数
    private int messageNum;// 消息数
    private int workNum;// 作品数
    private int level;// 当前等级
    private float process; // 等级积分进度
    private String nike;// 昵称，显示用
    private String nickName;
    private String headImg;// 头像
    private String gender;// 性别
    private String userType;
    private String addr;// 定位
    private String name;// 登录后的openid
    private String token;//qqtoken
    //mc哇
    private int uploadNum;// 贡献题目数
    private int answerNum;//答题次数
    private int scoreRank;
    private long allScore;// 当前积分
    private long ranking;//排名
    //private String userName;//用户名，实为openId
    //private String nickName;// 昵称，显示用
//    private String sex;// 性别

    public MCUser(){

    }

    public MCUser clone(@NonNull MCUser user){
        this.id = user.getId();
        this.score = user.getScore();
        this.isServerActor = user.getIsServerActor();
        this.talkNum = user.getTalkNum();
        this.homeNum = user.getHomeNum();
        this.dynamicNum = user.getDynamicNum();
        this.messageNum = user.getMessageNum();
        this.workNum = user.getWorkNum();
        this.level = user.getLevel();
        this.process = user.getProcess();
        this.nickName = user.getNickName();
        this.nike = user.getNike();
        this.headImg = user.getHeadImg();
        this.gender = user.getGender();
        this.userType = user.getUserType();
        this.addr = user.getAddr();
        this.name = user.getName();
        this.token = user.getToken();

        this.uploadNum = user.getUploadNum();
        this.answerNum = user.getAnswerNum();
        this.scoreRank = user.getScoreRank();
        this.allScore = user.getAllScore();
        this.ranking = user.getRanking();
//        this.userName = user.getUserName();
//        this.nickName = user.getNickName();
//        this.sex = user.getSex();
        return this;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUploadNum() {
        return uploadNum;
    }

    public void setUploadNum(int uploadNum) {
        this.uploadNum = uploadNum;
    }

    public int getAnswerNum() {
        return answerNum;
    }

    public void setAnswerNum(int answerNum) {
        this.answerNum = answerNum;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getAllScore() {
        return allScore;
    }

    public void setAllScore(long allScore) {
        this.allScore = allScore;
    }

/*    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }*/


    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

/*    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }*/

    public long getRanking() {
        return ranking;
    }

    public void setRanking(long ranking) {
        this.ranking = ranking;
    }

    public int getScoreRank() {
        return scoreRank;
    }

    public void setScoreRank(int scoreRank) {
        this.scoreRank = scoreRank;
    }

    public void addScore(int score){
        this.allScore += score;
    }

    public void addAnswerNumber(){
        this.answerNum++;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getIsServerActor() {
        return isServerActor;
    }

    public void setIsServerActor(int isServerActor) {
        this.isServerActor = isServerActor;
    }

    public int getTalkNum() {
        return talkNum;
    }

    public void setTalkNum(int talkNum) {
        this.talkNum = talkNum;
    }

    public int getHomeNum() {
        return homeNum;
    }

    public void setHomeNum(int homeNum) {
        this.homeNum = homeNum;
    }

    public int getDynamicNum() {
        return dynamicNum;
    }

    public void setDynamicNum(int dynamicNum) {
        this.dynamicNum = dynamicNum;
    }

    public int getMessageNum() {
        return messageNum;
    }

    public void setMessageNum(int messageNum) {
        this.messageNum = messageNum;
    }

    public int getWorkNum() {
        return workNum;
    }

    public void setWorkNum(int workNum) {
        this.workNum = workNum;
    }

    public float getProcess() {
        return process;
    }

    public void setProcess(float process) {
        this.process = process;
    }

    public String getNike() {
        return nike;
    }

    public void setNike(String nike) {
        this.nike = nike;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public boolean isUserValid(){
        if (0 < id && null != name){
            return true;
        }
        return false;
    }


}
