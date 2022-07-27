package com.inhatc.my_refrigerator;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MemoFragment extends Fragment implements View.OnClickListener{

    ListView memoList;
    MemoItemAdapter adapter;
    EditText etMemo;

    public MemoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_memo, container, false);

        Button btnMemo = (Button) v.findViewById(R.id.btnMemo);
        btnMemo.setOnClickListener(this);
        memoList = v.findViewById(R.id.memoList);
        etMemo = v.findViewById(R.id.etMemo);

        adapter = new MemoItemAdapter();

        return v;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnMemo){
            //메모 저장
            adapter.addItem(new MemoItem(etMemo.getText().toString()));
            etMemo.setText(null);
            memoList.setAdapter(adapter);
            Toast.makeText(getActivity().getApplicationContext(), "추가되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}