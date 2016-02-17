package com.fudan.android.mapchatting.config;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ethan on 15/12/24.
 */
public class Config {
    public static final String APP_ID = "com.fudan.android.activity.Config";

    public static final String CHARSET = "utf-8";

    public static final String SERVER_URL = "http://175.186.145.55:8080/index.jsp"; // IP address

    // keyn
    public static final String KEY_TOKEN = "key_token";
    public static final String KEY_ACTION = "key_action";
    public static final String KEY_PHONE_NUM = "key_phone";
    public static final String KEY_CODE = "key_code";
    public static final String KEY_RESULT_STATUS = "key_status";
    public static final String KEY_DIS_LIST = "key_dis_list";
    public static final String KEY_PAGE = "key_page";
    public static final String KEY_PERPAGE = "key_perpage";
    public static final String KEY_DISCUSSIONID = "key_discussionid";
    public static final String KEY_DISCUSSION = "key_discussion";
    public static final String KEY_STARTTIME = "key_starttime";
    public static final String KEY_CONTENTS = "key_contents";
    public static final String KEY_SENTENCE = "key_sentence";

    // value: action
    public static final String ACTION_GET_CODE = "action_get_code";
    public static final String ACTION_LOGIN = "action_login";
    public static final String ACTION_GET_DIS_LIST = "action_get_dis_list";
    public static final String ACTION_GET_DISCUSSION = "action_get_discussion";

    // value: result status
    public static final int RESULT_STATUS_SUCCESS = 1;
    public static final int RESULT_STATUS_FAIL = 0;
    public static final int RESULT_STATUS_INVALID_TOKEN = 2;

    public static String getCachedToken(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(KEY_TOKEN, null);
    }

    public static void setCacheToken(Context context, String token) {
        SharedPreferences.Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
        e.putString(KEY_TOKEN, token);
        e.commit();
    }

    public static String getCachedPhoneNum(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(KEY_PHONE_NUM, null);
    }

    public static void setCachePhoneNum(Context context, String phoneNum) {
        SharedPreferences.Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
        e.putString(KEY_PHONE_NUM, phoneNum);
        e.commit();
    }

}
