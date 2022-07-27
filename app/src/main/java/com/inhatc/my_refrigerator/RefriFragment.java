package com.inhatc.my_refrigerator;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class RefriFragment extends Fragment implements View.OnClickListener{

    ListFragment listFragment;
    AddFragment addFragment;
    String type;
    Bundle bundle = new Bundle();

    public RefriFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //툴바설정
        setHasOptionsMenu(true);
        getActivity().setTitle("식재료 목록");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_refri, container, false);

        listFragment = new ListFragment();
        addFragment = new AddFragment();
        ImageButton btnFreezer = (ImageButton) v.findViewById(R.id.btnFreezer);
        ImageButton btnFridge = (ImageButton) v.findViewById(R.id.btnFridge);
        ImageButton btnFresh = (ImageButton) v.findViewById(R.id.btnFresh);
        ImageButton btnHomeBar = (ImageButton) v.findViewById(R.id.btnHomeBar);

        btnFreezer.setOnClickListener(this);
        btnFridge.setOnClickListener(this);
        btnFresh.setOnClickListener(this);
        btnHomeBar.setOnClickListener(this);

        getActivity().setTitle("내 손 안의 냉장고"); //툴바 타이틀 설정
        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnFreezer:
                type = "freezer";
                bundle.putString("type", type); //번들에 넘길 값 저장
                listFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.container, listFragment).commit();
                break;

            case R.id.btnFridge:
                type = "fridge";
                bundle.putString("type", type); //번들에 넘길 값 저장
                listFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.container, listFragment).commit();
                break;

            case R.id.btnFresh:
                type = "fresh";
                bundle.putString("type", type); //번들에 넘길 값 저장
                listFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.container, listFragment).commit();
                break;

            case R.id.btnHomeBar:
                type = "homebar";
                bundle.putString("type", type); //번들에 넘길 값 저장
                listFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.container, listFragment).commit();
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.toolbar_refri,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();

        switch (curId){
            case R.id.item1:
                //tab1 메뉴 아이콘 선택시 이벤트 설정
                getFragmentManager().beginTransaction().replace(R.id.container, addFragment).commit();
                break;
            case R.id.item2:
                type = "all";
                bundle.putString("type", type); //번들에 넘길 값 저장
                listFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.container, listFragment).commit();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}