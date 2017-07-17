package com.ybyb.zzq.javamailapp.manager;

import com.ybyb.zzq.javamailapp.AppConstants;
import java.util.Properties;
import javax.mail.Session;
import javax.mail.Store;

/**
 * 作者：周正权 on 2017/7/14
 * 邮箱：zhouzhengquan@shixinyun.com
 */

public class MailManager {
    public static Store getStore(String username,String password,int type){
        Properties props = System.getProperties();
        String provider="";
        String host="";
        String text[]=username.split("@");
        switch (type){
            case AppConstants.TYPE_IMAP:
                provider="imap";
                break;
            case AppConstants.TYPE_POP:
                provider="pop";
                break;
        }
        host=provider+"."+text[1];
        Session ss = Session.getDefaultInstance(props, null);
        try {
            Store store = ss.getStore(provider);
            // 连接存储库，从而可以打开存储库中的文件夹，此时是面向IMAP的
            store.connect(host, username, password);
            // 打开文件夹，此时是关闭的，只能对其进行删除或重命名，无法获取关闭文件夹的信息
            return store;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
