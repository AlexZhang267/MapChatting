package com.fudan.android.mapchatting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fudan.android.mapchatting.R;
import com.fudan.android.mapchatting.entity.Discussion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 16/1/6.
 */
public class DisListAdapter extends BaseAdapter {

    private Context context;
    private List<Discussion> mDiscussions = new ArrayList<>();

    private static class ListItem {

        private TextView discussionTitle;
        private TextView creator;
        private TextView time;

        public ListItem(TextView discussionTitle, TextView creator, TextView time) {
            this.discussionTitle = discussionTitle;
            this.creator = creator;
            this.time = time;
        }

        public TextView getDiscussionTitle() {
            return discussionTitle;
        }

        public TextView getCreator() {
            return creator;
        }

        public TextView getTime() {
            return time;
        }
    }

    public DisListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return mDiscussions.size();
    }

    @Override
    public Discussion getItem(int position) {
        return mDiscussions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_discussion, null);
            convertView.setTag(new ListItem((TextView) convertView.findViewById(R.id.dis_list_txt_discussion_title),
                    (TextView) convertView.findViewById(R.id.dis_list_txt_creator),
                    (TextView) convertView.findViewById(R.id.dis_list_txt_time)
            ));
        }

        ListItem listItem = (ListItem) convertView.getTag();
        Discussion discussion = getItem(position);
        listItem.getDiscussionTitle().setText(discussion.getDiscussion());
        listItem.getCreator().setText("By: " + discussion.getUsername()); // Use Phone Number as user name
        listItem.getTime().setText(discussion.getDate());

        return convertView;
    }

    public Context getContext() {
        return context;
    }

    public void addAll(List<Discussion> discussions) {
        mDiscussions.addAll(discussions);
        notifyDataSetChanged();
    }

    public void clear() {
        mDiscussions.clear();
        notifyDataSetChanged();
    }
}
