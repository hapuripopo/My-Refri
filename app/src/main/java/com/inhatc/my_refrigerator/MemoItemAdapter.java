package com.inhatc.my_refrigerator;

import android.app.LauncherActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import java.util.ArrayList;

public class MemoItemAdapter extends BaseAdapter {
    ArrayList<MemoItem> items = new ArrayList<MemoItem>();
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
        MemoItem memoItem = items.get(i);

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.memo_item, viewGroup, false);
        }

        CheckBox checkBox = view.findViewById(R.id.checkBox);

        checkBox.setText(memoItem.getName());
        checkBox.setChecked(memoItem.isChecked());

        return view;
    }

    public void addItem(MemoItem item) {
        items.add(item);
    }
}
