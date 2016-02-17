package com.fudan.android.mapchatting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fudan.android.mapchatting.R;
import com.fudan.android.mapchatting.entity.Content;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 16/1/6.
 */
public class DiscussionAdapter extends BaseAdapter {

    private Context context;
    private List<Content> mContents = new ArrayList<>();

    private static class ListItem {

        private TextView sentence;
        private TextView user;

        public ListItem(TextView sentence, TextView user) {
            this.sentence = sentence;
            this.user = user;
        }

        public TextView getSentence() {
            return sentence;
        }

        public TextView getUser() {
            return user;
        }
    }

    public DiscussionAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return mContents.size();
    }

    @Override
    public Content getItem(int position) {
        return mContents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_content, null);
            convertView.setTag(new ListItem((TextView) convertView.findViewById(R.id.discussion_txt_sentence),
                    (TextView) convertView.findViewById(R.id.discussion_txt_user)));
        }

        ListItem listItem = (ListItem) convertView.getTag();
        Content content = getItem(position);
        listItem.getSentence().setText(content.getSentence());
        listItem.getUser().setText(content.getUsername());

        return convertView;
    }

    public Context getContext() {
        return context;
    }

    public void addAll(List<Content> contents) {
        mContents.addAll(contents);
        notifyDataSetChanged();
    }

    public void clear() {
        mContents.clear();
        notifyDataSetChanged();
    }
}
