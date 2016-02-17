package com.fudan.android.mapchatting.net;

import com.fudan.android.mapchatting.config.Config;
import com.fudan.android.mapchatting.entity.Content;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 16/1/6.
 */
public class NetGetDiscussion {
    public NetGetDiscussion(String phoneNum, String token, String discussionId, int page, int perpage,
                            final SuccessCallback successCallback, final FailCallback failCallback) {
        new NetConnection(Config.SERVER_URL, HttpMethod.POST, new NetConnection.SuccessCallback() {

            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    switch (jsonObject.getInt(Config.KEY_RESULT_STATUS)) {
                        case Config.RESULT_STATUS_SUCCESS:
                            if (successCallback != null) {
                                List<Content> contents = new ArrayList<>();
                                JSONArray contentsJsonArray = jsonObject.getJSONArray(Config.KEY_CONTENTS);
                                JSONObject commentObj;
                                for (int i = 0; i < contentsJsonArray.length(); i++) {
                                    commentObj = contentsJsonArray.getJSONObject(i);
                                    contents.add(new Content(commentObj.getString(Config.KEY_SENTENCE),
                                            commentObj.getString(Config.KEY_PHONE_NUM)));
                                }

                                successCallback.onSuccess(jsonObject.getString(Config.KEY_DISCUSSIONID),
                                        jsonObject.getInt(Config.KEY_PAGE), jsonObject.getInt(Config.KEY_PERPAGE), contents);
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
        }, Config.KEY_ACTION, Config.ACTION_GET_DISCUSSION,
                Config.KEY_TOKEN, token,
                Config.KEY_DISCUSSIONID, discussionId,
                Config.KEY_PAGE, page + "",
                Config.KEY_PERPAGE, perpage + "");
    }


    public static interface SuccessCallback {
        void onSuccess(String discussionId, int page, int perpage, List<Content> contents);
    }

    public static interface FailCallback {
        void onFail(int errorCode);
    }
}
