package com.ybyb.zzq.javamailapp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.pop3.POP3Message;
import com.ybyb.zzq.javamailapp.AppConstants;
import com.ybyb.zzq.javamailapp.R;
import com.ybyb.zzq.javamailapp.StringUtil;
import com.ybyb.zzq.javamailapp.manager.MailManager;
import com.ybyb.zzq.javamailapp.manager.MyAuthenticator;
import com.ybyb.zzq.javamailapp.model.Email;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
*主页面
*/
public class MainActivity extends AppCompatActivity {
    private EditText mEmailAccountEt;
    private EditText mEmailPasswordEt;
    private RadioButton mIMAPRb;
    private RadioButton mPOPRb;
    private RadioButton mSMTPRb;
    private Button mConfirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        setListener();
    }

    private void setListener() {
        this.mEmailAccountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtil.isEmailLegal(s.toString())) {
                    boolean isEmpty=TextUtils.isEmpty(mEmailPasswordEt.getText().toString());
                    mConfirmBtn.setEnabled(!isEmpty);
                }
            }
        });
        this.mEmailPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    boolean isRight=StringUtil.isEmailLegal(mEmailAccountEt.getText().toString());
                    mConfirmBtn.setEnabled(isRight);
                }
            }
        });

        this.mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToView();
            }
        });
    }

    private void jumpToView() {
        final String account = this.mEmailAccountEt.getText().toString();
        final String password = this.mEmailPasswordEt.getText().toString();
        int type = 0;
        if (this.mIMAPRb.isChecked()) {
            type = AppConstants.TYPE_IMAP;
        }
        else if (this.mPOPRb.isChecked()) {
            type = AppConstants.TYPE_POP;
        }
        else if (this.mSMTPRb.isChecked()) {
            type = AppConstants.TYPE_STMP;
        }
        final int finalType = type;
        if (finalType==AppConstants.TYPE_STMP){
                new Thread(){
                    @Override
                    public void run() {
                        try {
                        sendMessage("smtp.163.com", "zzqybyb1986@163.com",
                                    "ybyb19860112", "417986033@qq.com", "你好。。。。。",
                                    "---------------wrwe-----------",
                                    "text/html;charset=gb2312");
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                    }
                }.start();
                Toast.makeText(MainActivity.this, "发送成功", Toast.LENGTH_SHORT).show();

        }else {
            new Thread() {
                @Override
                public void run() {
                    Store store = MailManager.getStore(account, password, finalType);
                    if (store == null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "邮件服务器配置错误，请检查设置！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        try {
                            Folder inbox = store.getFolder("INBOX");
                            inbox.open(Folder.READ_ONLY);
                            javax.mail.Message[] messages = inbox.getMessages();
                            final List<Email> emails = new ArrayList<Email>();
                            for (javax.mail.Message msg : messages) {
                                Email email = new Email();
                                if (finalType == AppConstants.TYPE_IMAP) {
                                    IMAPMessage message = (IMAPMessage) msg;
                                    email.setSubject(message.getSubject());
                                    InternetAddress sender = (InternetAddress) message.getSender();
                                    email.setSenderAddress(sender.getAddress());
                                    email.setSenderName(sender.getPersonal());
                                    Date sentDate = message.getSentDate();
                                    email.setSendDate(sentDate);
                                    Date receiveDate = message.getReceivedDate();
                                    email.setReceiveDate(receiveDate);
                                    String text = message.toString();
                                    email.setText(text);
                                    Log.e("tag", "text---" + text);
                                }
                                else if (finalType == AppConstants.TYPE_POP) {
                                    POP3Message message = (POP3Message) msg;
                                    email.setSubject(message.getSubject());
                                    InternetAddress sender = (InternetAddress) message.getSender();
                                    email.setSenderAddress(sender.getAddress());
                                    email.setSenderName(sender.getPersonal());
                                    Date sentDate = message.getSentDate();
                                    email.setSendDate(sentDate);
                                    Date receiveDate = message.getReceivedDate();
                                    email.setReceiveDate(receiveDate);
                                    String text = message.toString();
                                    email.setText(text);
                                    Log.e("tag", "text---" + text);
                                }
                                emails.add(email);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "接受邮件成功，收件箱邮件数为："+emails.size(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }
    }

    private void bindViews() {
        this.mEmailAccountEt = (EditText) findViewById(R.id.email_account_et);
        this.mEmailPasswordEt = (EditText) findViewById(R.id.email_password_et);
        this.mIMAPRb = (RadioButton) findViewById(R.id.imap_rb);
        this.mPOPRb = (RadioButton) findViewById(R.id.pop_rb);
        this.mSMTPRb = (RadioButton) findViewById(R.id.smtp_rb);
        this.mConfirmBtn = (Button) findViewById(R.id.confirm_btn);
    }
    private  void sendMessage(String smtpHost, String from,
                                   String fromUserPassword, String to, String subject,
                                   String messageText, String messageType) throws MessagingException {
        // 第一步：配置javax.mail.Session对象
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.starttls.enable","true");//使用 STARTTLS安全连接
        //props.put("mail.smtp.port", "25");             //google使用465或587端口
        props.put("mail.smtp.auth", "true");        // 使用验证
        //props.put("mail.debug", "true");
        Session mailSession = Session.getInstance(props, new MyAuthenticator(from, fromUserPassword));

        // 第二步：编写消息

        InternetAddress fromAddress = new InternetAddress(from);
        InternetAddress toAddress = new InternetAddress(to);

        MimeMessage message = new MimeMessage(mailSession);

        message.setFrom(fromAddress);
        message.addRecipient(MimeMessage.RecipientType.TO, toAddress);

        message.setSentDate(Calendar.getInstance().getTime());
        message.setSubject(subject);
        //message.setContent(messageText, messageType);

        // 第三步：发送消息
        Transport transport = mailSession.getTransport("smtp");
        transport.connect(smtpHost,from, fromUserPassword);
        //transport.send(message,new Address[]{toAddress});//java5.0的api，高版本兼容有问题，会报错NoClassDefFoundError
        Log.e("tag","sent");
    }
}
