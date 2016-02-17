package com.fudan.android.mapchatting.activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.fudan.android.mapchatting.R;
import com.fudan.android.mapchatting.adapter.DisListAdapter;
import com.fudan.android.mapchatting.config.Config;
import com.fudan.android.mapchatting.entity.Discussion;
import com.fudan.android.mapchatting.net.NetGetDisList;

import java.util.List;

public class DisListActivity extends ListActivity {

    private String mPhoneNum;
    private String mToken;
    private DisListAdapter mDisListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discuss_list);

        this.setTitle(R.string.activity_discuss_list_title);

        mPhoneNum = getIntent().getStringExtra(Config.KEY_PHONE_NUM);
        mToken = getIntent().getStringExtra(Config.KEY_TOKEN);

        mDisListAdapter = new DisListAdapter(this);
        setListAdapter(mDisListAdapter);

        final ProgressDialog pd = ProgressDialog.show(this, getResources().getString(R.string.login_progress_connecting_title),
                getResources().getString(R.string.login_progress_connecting_content));

        loadDiscussionList();
        pd.dismiss();
    }

    private void loadDiscussionList() {
        final ProgressDialog pd = ProgressDialog.show(this, getResources().getString(R.string.login_progress_connecting_title),
                getResources().getString(R.string.login_progress_connecting_content));

        new NetGetDisList(mPhoneNum, mToken, 1, 20, new NetGetDisList.SuccessCallback() {

            @Override
            public void onSuccess(int page, int perpage, List<Discussion> discussionList) {
                pd.dismiss();

                mDisListAdapter.clear();
                mDisListAdapter.addAll(discussionList);
            }
        }, new NetGetDisList.FailCallback() {

            @Override
            public void onFail(int errorCode) {
                pd.dismiss();

                if (errorCode == Config.RESULT_STATUS_INVALID_TOKEN) {
                    startActivity(new Intent(DisListActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(DisListActivity.this, R.string.discuss_list_toast_load_fail, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Discussion discussion = mDisListAdapter.getItem(position);
        Intent i = new Intent(this, DiscussionActivity.class);
        i.putExtra(Config.KEY_DISCUSSION, discussion.getDiscussion());
        i.putExtra(Config.KEY_DISCUSSIONID, discussion.getDiscussionId());
        i.putExtra(Config.KEY_PHONE_NUM, discussion.getUsername());
        i.putExtra(Config.KEY_TOKEN, mToken);
        startActivity(i);
    }
}
