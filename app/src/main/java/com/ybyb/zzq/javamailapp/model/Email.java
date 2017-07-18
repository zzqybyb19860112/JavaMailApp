package com.ybyb.zzq.javamailapp.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 作者：周正权 on 2017/7/18
 * 邮箱：zhouzhengquan@shixinyun.com
 */

public class Email implements Serializable {
    private String subject;//邮件主题
    private String senderName;//发件人名称
    private String senderAddress;//发件人邮箱地址
    private Date sendDate;
    private Date receiveDate;
    private String text;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
