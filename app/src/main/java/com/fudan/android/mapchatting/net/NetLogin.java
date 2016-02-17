package com.fudan.android.mapchatting.net;

import com.fudan.android.mapchatting.config.Config;

import org.json.JSONException;
import org.json.JSONObject;

public class NetLogin {

    public NetLogin(String phoneNum, String code, final SuccessCallback successCallback, final FailCallback failCallback) {
        new NetConnection(Config.SERVER_URL, HttpMethod.POST, new NetConnection.SuccessCallback() {

            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject obj = new JSONObject(result);

                    switch (obj.getInt(Config.KEY_RESULT_STATUS)) {
                        case Config.RESULT_STATUS_SUCCESS:
                            if (successCallback != null) {
                                successCallback.onSuccess(obj.getString(Config.KEY_TOKEN));
                            }
                            break;
                        default:
                            if (failCallback != null) {
                                failCallback.onFail();
                            }
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    if (failCallback != null) {
                        failCallback.onFail();
                    }
                }
            }
        }, new NetConnection.FailCallback() {

            @Override
            public void onFail() {
                if (failCallback != null) {
                    failCallback.onFail();
                }
            }
        }, Config.KEY_ACTION, Config.ACTION_LOGIN, Config.KEY_PHONE_NUM, phoneNum, Config.KEY_CODE, code);
    }

    public static interface SuccessCallback {
        void onSuccess(String token);
    }

    public static interface FailCallback {
        void onFail();
    }
}
