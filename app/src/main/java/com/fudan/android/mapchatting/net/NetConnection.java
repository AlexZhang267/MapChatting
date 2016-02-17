package com.fudan.android.mapchatting.net;

import android.os.AsyncTask;

import com.fudan.android.mapchatting.config.Config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Ethan on 15/12/24.
 */
public class NetConnection {

    /**
     * Communicate with the server
     *
     * @param url             address
     * @param method          http method
     * @param successCallback success state
     * @param failCallback    fail state
     * @param kvs             key value
     */
    public NetConnection(final String url, final HttpMethod method, final SuccessCallback successCallback,
                         final FailCallback failCallback, final String... kvs) {

        new AsyncTask<Void, Void, String>() { // No parameter, no progress

            @Override
            protected String doInBackground(Void... arg0) {
                // key value
                StringBuffer paramsStr = new StringBuffer(); // Thread-safe
                for (int i = 0; i < kvs.length; i += 2) {
                    paramsStr.append(kvs[i]).append("=").append(kvs[i + 1]).append("&"); // key=value&
                }

                try {
                    URLConnection uc;

                    switch (method) {
                        case POST:
                            uc = new URL(url).openConnection();
                            uc.setDoOutput(true); // output to the server
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(uc.getOutputStream(),
                                    Config.CHARSET));
                            bw.write(paramsStr.toString());
                            bw.flush();
                            break;
                        default: // GET
                            uc = new URL(url + "?" + paramsStr.toString()).openConnection();
                            break;
                    }

                    System.out.println("Request url:" + uc.getURL());
                    System.out.println("Request data:" + paramsStr);

                    // Read return data from the server
                    BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream(), Config.CHARSET));
                    String line;
                    StringBuffer result = new StringBuffer();
                    while ((line = br.readLine()) != null) {
                        result.append(line);
                    }

                    System.out.println("Result:" + result);
                    return result.toString();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String result) {

                if (result != null) { // success
                    if (successCallback != null) {
                        successCallback.onSuccess(result);
                    }
                } else { // fail
                    if (failCallback != null) {
                        failCallback.onFail();
                    }
                }

                super.onPostExecute(result);
            }
        }.execute();

    }

    /**
     * Notify caller
     */
    public static interface SuccessCallback {
        void onSuccess(String result);
    }

    public static interface FailCallback {
        void onFail();
    }

}