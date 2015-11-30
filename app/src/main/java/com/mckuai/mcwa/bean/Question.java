package com.mckuai.mcwa.bean;
import com.mckuai.imc.activity.MCKuai4;
import com.mckuai.imc.R;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 习题信息
 * Created by kyly on 2015/10/12.
 */
public class Question implements Serializable {
    private int id;
    private String questionType = "choice";//选择题(choice) 判断题(judge)
    private String title;//题目
    private int score;//分数
    private String icon;//封面
    private int authorId;//贡献者id
    private String authorName;//贡献者名字
    private String insertTime;
    private String answerOne;//答案1
    private String answerTwo;
    private String answerThree;
    private String answerFour;
    private String rightAnswer;//正确答案
    private String status;//未通过(nopass) 通过(pass)  审核中（passing）
    private int allCount;//答题总人数
    private int rightCount;//正确次数

    public Question() {

    }

    public String getInsertTimeEx(){
        if (null != insertTime && insertTime.length() > 10){
            return insertTime.substring(insertTime.indexOf("-")+1,insertTime.indexOf(" "));
        }
        return "未知";
    }

    //获取已经打乱顺序了的选项
    public ArrayList<String> getOptionsEx() {
        ArrayList<String> temp = new ArrayList<>(4);
        switch (questionType){
            case "choice":
                temp.add((int)(temp.size() * Math.random()),answerFour);
                temp.add((int)(temp.size() * Math.random()),answerOne);
                temp.add((int)(temp.size() * Math.random()),answerThree);
                temp.add((int)(temp.size() * Math.random()),answerTwo);
                break;
            default:
                temp.add(MCKuai4.getInstance().getString(R.string.correct));
                temp.add(MCKuai4.getInstance().getString(R.string.wrong));
                break;
        }
       ArrayList<String> options = new ArrayList<>(temp.size());
        for (int size = temp.size();size > 0;size--){
            int index = (int)(Math.random() * size);
            options.add(temp.get(index));
            temp.remove(index);
        }
        temp.clear();
        temp = null;
        return options;
    }

    //判断答案是否正确
    public boolean isRightOption(String optins) {
        return optins.equals(rightAnswer);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(String insertTime) {
        this.insertTime = insertTime;
    }

    public String getAnswerOne() {
        return answerOne;
    }

    public void setAnswerOne(String answerOne) {
        this.answerOne = answerOne;
    }

    public String getAnswerTwo() {
        return answerTwo;
    }

    public void setAnswerTwo(String answerTwo) {
        this.answerTwo = answerTwo;
    }

    public String getAnswerThree() {
        return answerThree;
    }

    public void setAnswerThree(String answerThree) {
        this.answerThree = answerThree;
    }

    public String getAnswerFour() {
        return answerFour;
    }

    public void setAnswerFour(String answerFour) {
        this.answerFour = answerFour;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAllCount() {
        return allCount;
    }

    public void setAllCount(int allCount) {
        this.allCount = allCount;
    }

    public int getRightCount() {
        return rightCount;
    }

    public void setRightCount(int rightCount) {
        this.rightCount = rightCount;
    }
}
