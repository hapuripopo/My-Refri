package com.inhatc.my_refrigerator;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class NoticeFragment extends Fragment {

    ListView noticeList;
    NoticeItemAdapter adapter;
    DatabaseReference mDatabase;
    String userId;
    ArrayList<addRefri> contents;
    public NoticeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_notice, container, false);
        noticeList = v.findViewById(R.id.noticeList);
        Bundle bundle = getArguments();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        String[] arrEmail = userId.split("@");



        // DB
        mDatabase = FirebaseDatabase.getInstance().getReference();
        contents = new ArrayList<>();

        // 데이터 구조를 바꾸시면 아래 쿼리가 맞게 되는 거죠.
        // 다만 realtime database 는 오름차순, 내림차순을 설정할 수는 없습니다.
        // 무조건 오름차순일 거예요.
        // 그러니 lastDate 가 long 타입이라고 할 떄, 오름차순이라면 처음 나타나는 데이터가
        // 유통기한이 가장 짧게 남은 데이터가 되겠죠?
        // 이 프로젝트 바로 적용하지 마시고, 임시 프로젝트와 임시 파이어베이스 프로젝트 만드셔서 테스트 해 보세요.
        // 아 UID 를 가져오는 방법은 아래와 같스빈다.
        // 로그인을 하지 않았다면 getCurrentUser 가 null 입니다.
        // 이 uid 를 email split 하는 부분 대신 사용하시면 됩니다.
        // 넵. 감사합니다. 그럼 다 된건가요?
        // 말씀하세요
        FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query noticeQuery = mDatabase.child("Refri_List").child(userId)
                .orderByChild("lastDate");
        noticeQuery.limitToFirst(5).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot item : dataSnapshot.getChildren()){
                    contents.add(0,item.getValue(addRefri.class));
                }
                Collections.reverse(contents);
                adapter = new NoticeItemAdapter();
                for(addRefri content : contents){
                    adapter.addItem(new NoticeItem(content.name, content.lastDate));
                }
                noticeList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return v;
    }
}