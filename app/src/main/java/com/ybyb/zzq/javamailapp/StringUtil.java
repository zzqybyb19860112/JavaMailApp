package com.ybyb.zzq.javamailapp;

import android.text.TextUtils;

/**
 * 作者：周正权 on 2017/7/17
 * 邮箱：zhouzhengquan@shixinyun.com
 */

public class StringUtil {
    /**
     * 判断是否是合法的邮箱
     *
     * @param email
     *
     * @return
     */
    public static boolean isEmailLegal(String email) {
        return !TextUtils.isEmpty(email) && email.matches(AppConstants.Validator.REGEX_EMAIL);
    }
}
