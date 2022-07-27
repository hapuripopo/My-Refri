package com.inhatc.my_refrigerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    RefriFragment refriFragment;
    NoticeFragment noticeFragment;
    MemoFragment memoFragment;
    SetFragment setFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Login의 인텐트 받아서 텍스트 값 저장
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        refriFragment = new RefriFragment();
        noticeFragment = new NoticeFragment();
        memoFragment = new MemoFragment();
        setFragment = new SetFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, refriFragment).commit();

        //번들객체 생성
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        Log.d("bundle_mainactivity : ", bundle+""); //확인완료

        //프래그먼트로 번들 전달
        refriFragment.setArguments(bundle);

        BottomNavigationView bottom_menu = findViewById(R.id.bottom_menu);
        bottom_menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.first_tab:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, refriFragment).commit();
                        return true;
                    case R.id.second_tab:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, noticeFragment).commit();
                        return true;
                    case R.id.third_tab:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, memoFragment).commit();
                        return true;
                    case R.id.fourth_tab:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, setFragment).commit();
                        return true;
                }
                return false;
            }

//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.first_tab:
//                        Intent refriIntent = new Intent(MainActivity.this, RefriActivity.class);
//                        startActivity(refriIntent);
//                }
//                return false;
//            }
        });
    }

}
