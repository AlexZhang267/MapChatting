package com.fudan.android.mapchatting.activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fudan.android.mapchatting.R;
import com.fudan.android.mapchatting.adapter.DisListAdapter;
import com.fudan.android.mapchatting.adapter.DiscussionAdapter;
import com.fudan.android.mapchatting.config.Config;
import com.fudan.android.mapchatting.entity.Content;
import com.fudan.android.mapchatting.entity.Discussion;
import com.fudan.android.mapchatting.net.NetGetDiscussion;

import java.util.List;

public class DiscussionActivity extends ListActivity {

    private String discussionId;
    private String discussion;
    private String phoneNum;
    private String token;

    private DiscussionAdapter mDiscussionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

//        set

        Intent intent = getIntent();
        discussionId = intent.getStringExtra(Config.KEY_DISCUSSIONID);
        discussion = intent.getStringExtra(Config.KEY_DISCUSSION);
        phoneNum = intent.getStringExtra(Config.KEY_PHONE_NUM);
        token = intent.getStringExtra(Config.KEY_TOKEN);

        this.setTitle(discussion);

        mDiscussionAdapter = new DiscussionAdapter(this);
        setListAdapter(mDiscussionAdapter);

        getContents();
    }

    private void getContents() {
        final ProgressDialog pd = ProgressDialog.show(this, getResources().getString(R.string.login_progress_connecting_title),
                getResources().getString(R.string.login_progress_connecting_content));
        new NetGetDiscussion(phoneNum, token, discussionId, 1, 20, new NetGetDiscussion.SuccessCallback() {

            @Override
            public void onSuccess(String discussionId, int page, int perpage,
                                  List<Content> contents) {

                pd.dismiss();

                mDiscussionAdapter.clear();
                mDiscussionAdapter.addAll(contents);
            }
        }, new NetGetDiscussion.FailCallback() {

            @Override
            public void onFail(int errorCode) {
                pd.dismiss();

                if (errorCode == Config.RESULT_STATUS_INVALID_TOKEN) {
                    startActivity(new Intent(DiscussionActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(DiscussionActivity.this, R.string.discussion_toast_content_fail, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
