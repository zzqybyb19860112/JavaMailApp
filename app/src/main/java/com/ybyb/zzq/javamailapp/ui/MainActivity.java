package com.ybyb.zzq.javamailapp.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;
import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.imap.IMAPSSLStore;
import com.sun.mail.pop3.POP3Message;
import com.sun.mail.pop3.POP3SSLStore;
import com.ybyb.zzq.javamailapp.AppConstants;
import com.ybyb.zzq.javamailapp.R;
import com.ybyb.zzq.javamailapp.manager.MailManager;
import javax.mail.Folder;
import javax.mail.Store;
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
    private LinearLayout mIndexLl;
    private ProgressBar mIndexPb;
    private static final int MSG = 333;
    private int mProgress = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //处理消息
            switch (msg.what) {
                case MSG:
                    //设置滚动条和text的值
                    mIndexPb.setProgress(mProgress);
                    break;
            }
        }
    };
    private Thread mThread = new Thread(new Runnable() {
        @Override
        public void run() {
            int max = mIndexPb.getMax();
            try {
                //子线程循环间隔消息
                while (mProgress < max) {
                    mProgress += 10;
                    Message msg = new Message();
                    msg.what = MSG;
                    mHandler.sendMessage(msg);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });

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
                if (s.toString().contains("@")) {
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
                    boolean isRight=mEmailAccountEt.getText().toString().contains("@");
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
        this.mIndexLl.setVisibility(View.VISIBLE);
        this.mThread.start();
        String account = this.mEmailAccountEt.getText().toString();
        String password = this.mEmailPasswordEt.getText().toString();
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
        Store store = MailManager.getStore(account, password, type);
        if (store == null) {
            Toast.makeText(this, "邮件服务器配置错误，请检查设置！", Toast.LENGTH_SHORT).show();
            this.mIndexLl.setVisibility(View.GONE);
            this.mThread.interrupt();
        }
        else {
            this.mIndexLl.setVisibility(View.GONE);
            this.mThread.interrupt();
            Intent intent = new Intent(this, MailActivity.class);
            if (type == AppConstants.TYPE_IMAP) {
                IMAPSSLStore s = (IMAPSSLStore) store;
                try {
                    Folder inbox = s.getFolder("INBOX");
                    inbox.open(Folder.READ_ONLY);
                    IMAPMessage[] messages = (IMAPMessage[]) inbox.getMessages();
                    intent.putExtra("data", messages);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (type == AppConstants.TYPE_POP) {
                POP3SSLStore s = (POP3SSLStore) store;
                try {
                    Folder inbox = s.getFolder("INBOX");
                    inbox.open(Folder.READ_ONLY);
                    POP3Message[] messages = (POP3Message[]) inbox.getMessages();
                    intent.putExtra("data", messages);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            startActivity(intent);
        }
    }

    private void bindViews() {
        this.mEmailAccountEt = (EditText) findViewById(R.id.email_account_et);
        this.mEmailPasswordEt = (EditText) findViewById(R.id.email_password_et);
        this.mIMAPRb = (RadioButton) findViewById(R.id.imap_rb);
        this.mPOPRb = (RadioButton) findViewById(R.id.pop_rb);
        this.mSMTPRb = (RadioButton) findViewById(R.id.smtp_rb);
        this.mConfirmBtn = (Button) findViewById(R.id.confirm_btn);
        this.mIndexLl = (LinearLayout) findViewById(R.id.index_ll);
        this.mIndexPb = (ProgressBar) findViewById(R.id.index_pb);
        this.mIndexPb.setMax(100);
    }
}
