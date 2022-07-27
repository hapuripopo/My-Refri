package com.inhatc.my_refrigerator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class FindPassword extends AppCompatActivity {

    TextView txt_pw;
    Button btn_find, btn_cancel;
    EditText edt_email, edt_name;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        txt_pw = (TextView) findViewById(R.id.txt_pw);

        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_name = (EditText) findViewById(R.id.edt_name);

        btn_find = (Button) findViewById(R.id.btn_pwfind);
        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edt_email.getText().toString().trim();
                String name = edt_name.getText().toString().trim();

                if (email.length() == 0 || name.length() == 0) {
                    Toast toast = Toast.makeText(FindPassword.this, "모든 항목을 입력해주세요.", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    mDatabaseReference.child("user").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("firebase", "Error getting data", task.getException());
                            }
                            else {
                                String findPw = null;

                                ArrayList<HashMap<String, String>> members = (ArrayList<HashMap<String, String>>) task.getResult().getValue();
                                for (HashMap<String, String> member : members) {
                                    if (member.get("email").equals(email) && member.get("name").equals(name)) {
                                        findPw = member.get("password");
                                        txt_pw.setText(findPw);
                                    } else {
                                        Toast toast = Toast.makeText(FindPassword.this, "찾으시는 가입정보가 없습니다.", Toast.LENGTH_LONG);
                                        toast.show();
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });

        btn_cancel = (Button) findViewById(R.id.btn_cancle);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });
    }
}