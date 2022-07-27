package com.inhatc.my_refrigerator;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddFragment extends Fragment{

    //파이어베이스 데이터베이스 연동
    private FirebaseDatabase myFirebase = FirebaseDatabase.getInstance();
    private DatabaseReference myDB_Reference = myFirebase.getReference();

    EditText edtName; //식재료명
    EditText edtMemo; //메모

    Button btnBuyDate; //구입일자
    Button btnLastDate; //유통기한

    RadioGroup rdoGroup;
    Spinner spinner;

    String category;
    String name;
    String selectedRadio;
    String email; // 이메일 값 받아온 변수
    long buyDate;
    long lastDate;


    public AddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("email", this, new FragmentResultListener(){
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported
                email = bundle.getString("email");
                // Do something with the result...
            }
        });
        //툴바설정
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add, container, false);

        // 카테고리 spinner 구현
        spinner = (Spinner)v.findViewById(R.id.spnList);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> spn_adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.category, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        spn_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(spn_adapter);

        //툴바 타이틀
        getActivity().setTitle("식재료 추가");


        // 아이디 연결
        edtName = (EditText) v.findViewById(R.id.edtName);
        edtMemo = (EditText) v.findViewById(R.id.edtMemo);
        btnBuyDate = (Button) v.findViewById(R.id.btnBuyDate);
        btnLastDate = (Button) v.findViewById(R.id.btnLastDate);

        // 구입일자, 유통기한 DatePickerDialog 구현
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN);

        btnBuyDate.setOnClickListener(v1 -> {
            DatePickerDialog dialog = new DatePickerDialog(requireContext());
            dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(y, m, d);
                    btnBuyDate.setText(format.format(calendar.getTime()));
                    //Date to long
                    buyDate = (calendar.getTime()).getTime();
                }
            });
            dialog.show();
        });

        btnLastDate.setOnClickListener(v1 -> {
            DatePickerDialog dialog = new DatePickerDialog(requireContext());
            dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(y, m, d);
                    btnLastDate.setText(format.format(calendar.getTime()));

                    lastDate = (calendar.getTime()).getTime();
                }
            });
            dialog.show();
        });

        rdoGroup = (RadioGroup) v.findViewById(R.id.radioGroup);
        rdoGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rdoFridge:
                        selectedRadio= "fridge";
                        break;
                    case R.id.rdoFreezer:
                        selectedRadio= "freezer";
                        break;
                    case R.id.rdoFresh:
                        selectedRadio= "fresh";
                        break;
                    case R.id.rdoHomeBar:
                        selectedRadio= "homebar";
                        break;
                }
            }
        });
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.toolbar_menu,menu);
//        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();

        switch (curId){
            case R.id.item1: //완료버튼 클릭 시 값 저장
                category = spinner.getSelectedItem().toString();
                String storage = selectedRadio;
                
                addRefri(category, storage, buyDate , lastDate, edtMemo.getText().toString());
                
                //DB 정상 업로드 토스트메시지
                Toast.makeText(getActivity(),"저장 완료",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addRefri(String category, String storage, long buydate, long lastdate, String memo){
        name = edtName.getText().toString();
        addRefri addRefri = new addRefri(name, category, storage, buydate, lastdate, memo);

        // 데이터 구조 : Refri_List/uid/id/데이터
        // uid 는 로그인 한 사용자의 고유 id (Unique id)
        // id 는 직접 지정할 필요없이, push 함수를 사용

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("Refri_List").child(uid).push().setValue(addRefri);
//        String[] arrayEmail = email.split("@");
//        Log.d("email 2 :", arrayEmail[0]);
        //myDB_Reference.child("Refri_List").child(arrayEmail[0]).child(selectedRadio).child(name).setValue(addRefri);

    }
}