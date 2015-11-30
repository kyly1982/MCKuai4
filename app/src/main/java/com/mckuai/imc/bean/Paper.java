package com.mckuai.imc.bean;

import com.mckuai.mcwa.bean.Question;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 考卷
 * Created by kyly on 2015/10/12.
 */
public class Paper implements Serializable{
//    protected int id;
//    protected String name;//名称
//    protected String cover;//封面
    protected ArrayList<Question> question;//习题列表
    protected ArrayList<MCUser> user; //贡献者

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getCover() {
//        return cover;
//    }
//
//    public void setCover(String cover) {
//        this.cover = cover;
//    }

    public ArrayList<Question> getQuestion() {
        return question;
    }

    public void setQuestion(ArrayList<Question> question) {
        this.question = question;
    }

    public ArrayList<MCUser> getUser() {
        return user;
    }

    public void setUser(ArrayList<MCUser> user) {
        this.user = user;
    }
}
