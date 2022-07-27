//package com.inhatc.my_refrigerator;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.firebase.auth.FirebaseAuth;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class RefriAdapter extends RecyclerView.Adapter<RefriAdapter.Holder> {
//
//    private Context context;
//    private List<addRefri> refriList = new ArrayList<>();
//
//    String Uid;
//
//    public RefriAdapter(Context context, List<addRefri> refriList){
//        this.context = context;
//        this.refriList = refriList;
//        Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//    }
//
//    public Holder onCreateViewHolder(ViewGroup parent, int viewType){
//        View view = LayoutInflater.from(context).inflate(R.layout.row_recordspeed,parent,false);
//        return new AdapterSpeed.MyHolder(view);
//    }
//
//    public class Holder {
//    }
//}
