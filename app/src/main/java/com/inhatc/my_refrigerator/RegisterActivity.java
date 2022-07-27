package com.inhatc.my_refrigerator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText editName, editId, editPw, editChaptcha, editPwCheck;
    private Button btnSignup, btnCaptcha, btn_delete;
    private ImageView im;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //firebase 접근 설정
        // user = firebaseAuth.getCurrentUser();
        firebaseAuth =  FirebaseAuth.getInstance();
        //firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        editName = (EditText) findViewById(R.id.editName);
        editId = (EditText) findViewById(R.id.editId);
        editPw = (EditText) findViewById(R.id.editPw);
        editChaptcha = (EditText) findViewById(R.id.edtChaptcha);
        editPwCheck = (EditText) findViewById(R.id.editPwCheck);

        im = (ImageView) findViewById(R.id.capchaView);

        /* 취소 버튼 */
        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);//액티비티 띄우기
            }
        });

//        Captcha c = new TextCaptcha(300, 100, 5, TextCaptcha.TextOptions.NUMBERS_AND_LETTERS);
        Captcha c = new MathCaptcha(300, 100, MathCaptcha.MathOptions.PLUS_MINUS_MULTIPLY);
        im.setImageBitmap(c.image);
        im.setLayoutParams(new LinearLayout.LayoutParams(c.width * 5, c.height * 2));

        /* 가입 버튼 */
        btnSignup = (Button) findViewById(R.id.btn_join);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //사용자 입력 정보
                String id = editId.getText().toString();
                String pw = editPw.getText().toString();
                String pwck = editPwCheck.getText().toString();
                String name = editName.getText().toString();
                String ans = editChaptcha.getText().toString().trim();

                if (id.length() == 0 || pw.length() == 0 || pwck.length() == 0 || name.length() == 0 || ans.length() == 0) {
                    Toast toast = Toast.makeText(RegisterActivity.this, "모든 항목을 입력해주세요.", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    boolean flag = isValidEmail(id);
                    if (!flag) {
                        Toast toast = Toast.makeText(RegisterActivity.this, "이메일 형식의 아이디를 입력해주세요.", Toast.LENGTH_LONG);
                        toast.show();
                    } else {
                        if (pw.equals(pwck)) {
                            if (ans.equals(c.answer)) {
                                createUser(id, pw);

//                                HashMap<Object, String> hashMap = new HashMap<>();
//                                hashMap.put("email", id);
//                                hashMap.put("name", name);
//                                hashMap.put("password", pw);

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference();

                                reference.child("user").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (!task.isSuccessful()) {
                                            Log.e("firebase", "Error getting data", task.getException());
                                        }
                                        else {
                                            String cnt = String.valueOf(task.getResult().getChildrenCount());
                                            reference.child("user").child(cnt).child("email").setValue(id);
                                            reference.child("user").child(cnt).child("name").setValue(name);
                                            reference.child("user").child(cnt).child("password").setValue(pw);
                                        }
                                    }
                                });
//                                reference.push().setValue(hashMap);

                                Intent intent = new Intent(getApplicationContext(), Login.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "정확한 보안문자를 입력해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        btnCaptcha = (Button) findViewById(R.id.btnCapcha);
        btnCaptcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Captcha c = new TextCaptcha(300, 100, 5, TextCaptcha.TextOptions.NUMBERS_AND_LETTERS);
                Captcha c = new MathCaptcha(300, 100, MathCaptcha.MathOptions.PLUS_MINUS_MULTIPLY);
                im.setImageBitmap(c.image);
                im.setLayoutParams(new LinearLayout.LayoutParams(c.width * 5, c.height * 2));
            }
        });
    }

    private void createUser(String id, String pw) {
        firebaseAuth.createUserWithEmailAndPassword(id, pw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공시
                            Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // 계정이 중복된 경우
                            Toast.makeText(RegisterActivity.this, "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static boolean isValidEmail(String email) {
        boolean err = false;
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if(m.matches()) {
            err = true;
        }
        return err;
    }
}