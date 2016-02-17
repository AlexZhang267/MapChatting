package com.fudan.android.mapchatting.net;

import com.fudan.android.mapchatting.config.Config;
import com.fudan.android.mapchatting.entity.Discussion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NetGetDisList {

    public NetGetDisList(String phoneNum, String token, int page, int perpage,
                         final SuccessCallback successCallback, final FailCallback failCallback) {
        new NetConnection(Config.SERVER_URL, HttpMethod.POST, new NetConnection.SuccessCallback() {

            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject obj = new JSONObject(result);

                    switch (obj.getInt(Config.KEY_RESULT_STATUS)) {
                        case Config.RESULT_STATUS_SUCCESS:
                            if (successCallback != null) {
                                List<Discussion> discussionList = new ArrayList<>();
                                JSONArray disJsonArray = obj.getJSONArray(Config.KEY_DIS_LIST);
                                JSONObject disObj;
                                for (int i = 0; i < disJsonArray.length(); i++) {
                                    disObj = disJsonArray.getJSONObject(i);
                                    discussionList.add(new Discussion(
                                            disObj.getString(Config.KEY_DISCUSSIONID),
                                            disObj.getString(Config.KEY_DISCUSSION),
                                            disObj.getString(Config.KEY_PHONE_NUM),
                                            disObj.getString(Config.KEY_STARTTIME)
                                    ));
                                }

                                successCallback.onSuccess(obj.getInt(Config.KEY_PAGE),
                                        obj.getInt(Config.KEY_PERPAGE), discussionList);
                            }
                            break;
                        case Config.RESULT_STATUS_INVALID_TOKEN:
                            if (failCallback != null) {
                                failCallback.onFail(Config.RESULT_STATUS_INVALID_TOKEN);
                            }
                            break;
                        default:
                            if (failCallback != null) {
                                failCallback.onFail(Config.RESULT_STATUS_FAIL);
                            }
                            break;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    if (failCallback != null) {
                        failCallback.onFail(Config.RESULT_STATUS_FAIL);
                    }
                }
            }
        }, new NetConnection.FailCallback() {

            @Override
            public void onFail() {
                if (failCallback != null) {
                    failCallback.onFail(Config.RESULT_STATUS_FAIL);
                }
            }
        }, Config.KEY_ACTION, Config.ACTION_GET_DIS_LIST,
                Config.KEY_PHONE_NUM, phoneNum,
                Config.KEY_TOKEN, token,
                Config.KEY_PAGE, page + "",
                Config.KEY_PERPAGE, perpage + "");
    }

    public static interface SuccessCallback {
        void onSuccess(int page, int perpage, List<Discussion> discussionList);
    }

    public static interface FailCallback {
        void onFail(int errorCode);
    }
}
