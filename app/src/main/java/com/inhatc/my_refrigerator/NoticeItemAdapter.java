package com.inhatc.my_refrigerator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NoticeItemAdapter extends BaseAdapter {
    ArrayList<NoticeItem> items = new ArrayList<NoticeItem>();
    Context context;

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        context = viewGroup.getContext();
        NoticeItem noticeItem = items.get(i);

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.notice_item, viewGroup, false);
        }

        TextView txtName = view.findViewById(R.id.txtName);
        TextView txtDays = view.findViewById(R.id.txtDays);


        long lLastDate = noticeItem.getDays();
        Date dLastDate = new Date(lLastDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String sLastDate = dateFormat.format(dLastDate);

        txtName.setText(noticeItem.getName());
        txtDays.setText(sLastDate);
        return view;
    }

    public void addItem(NoticeItem item) {
        items.add(item);
    }
}
