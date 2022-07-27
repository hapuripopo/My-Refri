package com.inhatc.my_refrigerator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuth mAuth = null;
    final String[] items = {"YES", "NO"};

    TextView txtTitle, txtMail;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SetFragment newInstance(String param1, String param2) {
        SetFragment fragment = new SetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_set, container, false);

        /* 로그아웃 버튼 */
        Button btnLogout = (Button) v.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(this);

        /* 로그아웃 버튼 */
        Button btnWithdrawal = (Button) v.findViewById(R.id.btn_withdrawal);
        btnWithdrawal.setOnClickListener(this);

        /* 관리자 문의 버튼 */
        Button btnInquiry = (Button) v.findViewById(R.id.btn_inquiry);
        btnInquiry.setOnClickListener(this);

        txtTitle = (TextView) v.findViewById(R.id.txt_title);
        txtMail = (TextView) v.findViewById(R.id.txt_mail);

        return v;
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_set, container, false);
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    private void revokeAccess() {
        mAuth.getCurrentUser().delete();
        Toast.makeText(getActivity().getApplicationContext(), "회원 탈퇴 되었습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_logout) {
            //로그인 화면 전환
            signOut();
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
            requireActivity().finishAffinity();
        }

        if (view.getId() == R.id.btn_withdrawal) {
            //회원 탈퇴
            /*revokeAccess();
            Intent intent = new Intent(getActivity(),Login.class);
            startActivity(intent);*/
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("회원 탈퇴하시겠습니까?");
            builder.setItems(items, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        /*..YES..*/
                        revokeAccess();
                        Intent intent = new Intent(getActivity(), Login.class);
                        startActivity(intent);
                    } else {
                        /*...NO...*/
                    }
                }
            });
            builder.show();

        }
        if (view.getId() == R.id.btn_inquiry) {
            //관리자 문의
            txtTitle.setVisibility(View.VISIBLE);
            txtMail.setVisibility(View.VISIBLE);
        }
    }
}