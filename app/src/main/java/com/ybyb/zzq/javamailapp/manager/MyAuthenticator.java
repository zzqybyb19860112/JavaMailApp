package com.ybyb.zzq.javamailapp.manager;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * 作者：周正权 on 2017/7/18
 * 邮箱：zhouzhengquan@shixinyun.com
 */

public class MyAuthenticator extends Authenticator {
    String userName="";
    String password="";
    public MyAuthenticator(){

    }
    public MyAuthenticator(String userName,String password){
        this.userName=userName;
        this.password=password;
    }
    protected PasswordAuthentication getPasswordAuthentication(){
        return new PasswordAuthentication(userName, password);
    }
}