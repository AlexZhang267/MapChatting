package com.fudan.android.mapchatting.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fudan.android.mapchatting.R;
import com.fudan.android.mapchatting.config.Config;
import com.fudan.android.mapchatting.net.NetGetCode;
import com.fudan.android.mapchatting.net.NetLogin;

public class LoginActivity extends AppCompatActivity {

    private EditText edtPhone;
    private EditText edtCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edtPhone = (EditText) findViewById(R.id.login_edt_phone);
        edtCode = (EditText) findViewById(R.id.login_edt_code);

        // Get code button
        findViewById(R.id.login_btn_get_code).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // empty phone number
                if (TextUtils.isEmpty(edtPhone.getText())) {
                    Toast.makeText(LoginActivity.this, R.string.login_toast_phone_empty, Toast.LENGTH_LONG).show();
                    return;
                }

                // progress indication
                final ProgressDialog pd = ProgressDialog.show(LoginActivity.this,
                        getResources().getString(R.string.login_progress_connecting_title),
                        getResources().getString(R.string.login_progress_connecting_content));

                new NetGetCode(edtPhone.getText().toString(), new NetGetCode.SuccessCallback() {
                    @Override
                    public void onSuccess() {
                        pd.dismiss();
                        Toast.makeText(LoginActivity.this, R.string.login_toast_code_success, Toast.LENGTH_LONG).show();
                    }
                }, new NetGetCode.FailCallback() {
                    @Override
                    public void onFail() {
                        pd.dismiss();
                        Toast.makeText(LoginActivity.this, R.string.login_toast_code_fail, Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        // Login button
        findViewById(R.id.login_btn_login).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (TextUtils.isEmpty(edtPhone.getText())) {
                    Toast.makeText(LoginActivity.this, R.string.login_toast_phone_empty, Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(edtCode.getText())) {
                    Toast.makeText(LoginActivity.this, R.string.login_toast_code_empty, Toast.LENGTH_LONG).show();
                    return;
                }


                final ProgressDialog pd = ProgressDialog.show(LoginActivity.this,
                        getResources().getString(R.string.login_progress_connecting_title),
                        getResources().getString(R.string.login_progress_connecting_content));

                new NetLogin(edtPhone.getText().toString(), edtCode.getText().toString(), new NetLogin.SuccessCallback() {

                    @Override
                    public void onSuccess(String token) {

                        pd.dismiss();

                        // Set Cache
                        Config.setCacheToken(LoginActivity.this, token);
                        Config.setCachePhoneNum(LoginActivity.this, edtPhone.getText().toString());

                        Intent i = new Intent(LoginActivity.this, DisListActivity.class);
                        i.putExtra(Config.KEY_TOKEN, token);
                        i.putExtra(Config.KEY_PHONE_NUM, edtCode.getText().toString());
                        startActivity(i);

                        finish(); // close this activity (no need to return to the login activity)
                    }
                }, new NetLogin.FailCallback() {

                    @Override
                    public void onFail() {
                        pd.dismiss();

                        Toast.makeText(LoginActivity.this, R.string.login_toast_login_fail, Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }
}
