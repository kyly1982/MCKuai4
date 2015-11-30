package com.mckuai.imc.bean;

/**
 * Created by kyly on 2015/11/26.
 */
public class Token {
    private int type;
    private long birthday;
    private long expires;
    private String token;

    public Token() {
        this.expires = 7200;
    }

    public Token(int type) {
        this.expires = 7200;
        this.type = type;
    }

    public void clone(Token token){
        this.type = token.getType();
        this.birthday = token.getBirthday();
        this.expires = token.getExpires();
        this.token = token.getToken();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isTokenSurvival() {
        return (System.currentTimeMillis() - birthday) < expires * 1000;
    }
}
