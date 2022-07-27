package com.inhatc.my_refrigerator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity {

    Button btn_login;
    TextView btn_join, txt_pw;
    EditText edt_email, edt_password;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            startMainActivity(user.getEmail());
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        //버튼 등록하기
        btn_join = findViewById(R.id.btn_join);
        btn_login = findViewById(R.id.btn_login);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        txt_pw = findViewById(R.id.txt_pw);

        //비밀번호 찾기
        txt_pw.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //intent함수를 통해 register액티비티 함수를 호출한다.
                Intent intent = new Intent(getApplicationContext(), FindPassword.class);
                startActivity(intent);

            }
        });

        //가입 버튼이 눌리면
        btn_join.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //intent함수를 통해 register액티비티 함수를 호출한다.
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);

            }
        });

        //로그인 버튼이 눌리면
        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = edt_email.getText().toString().trim();
                String pwd = edt_password.getText().toString().trim();
                firebaseAuth.signInWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("email", email);
//                                    Log.d("bundle : ", bundle+"");
//
//                                    AddFragment addFragment = new AddFragment();
//                                    addFragment.setArguments(bundle);

                                    // mainActivity로 전달할 인텐트 생성
                                    startMainActivity(email);
                                    finish();

                                } else {
                                    task.getException().printStackTrace();
                                    Toast.makeText(Login.this, "로그인 오류", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private void startMainActivity(String email) {
        Intent intent = new Intent(Login.this, MainActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }
}