package com.inhatc.my_refrigerator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class ListFragment extends Fragment implements ValueEventListener {

    private Query dbQuery;
    private ValueEventListener eventListener;
    private final FreezerAdapter adapter = new FreezerAdapter();

    private String uid;
    private String storageType = "";
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container, false);
        storageType = getArguments().getString("type");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        assert user != null;
//        String uid = user.getUid()
        uid = user.getUid();
//        String[] token = user.getEmail().split("@");
//        databaseReference = db.getReference().child("Refri_List").child(token[0]).child("freezer");
//        dbQuery = db.getReference().child("Refri_List").child(uid).orderByChild("storage").equalTo("freezer");

        if (storageType.equals("all")) {
            dbQuery = db.getReference().child("Refri_List").child(uid).orderByChild("storage");

        } else {
            dbQuery = db.getReference().child("Refri_List").child(uid).orderByChild("storage").equalTo(storageType);
        }

        adapter.setOnItemDeleteListener(data -> {
            db.getReference().child("Refri_List").child(uid).child(data.getKey()).removeValue();
        });

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));

        ItemTouchHelper touchHelper = new ItemTouchHelper(adapter.simpleCallback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (eventListener == null) {
            eventListener = dbQuery.addValueEventListener(this);
        }
    }

    @Override
    public void onPause() {
        if (eventListener != null) {
            dbQuery.removeEventListener(eventListener);
            eventListener = null;
        }

        super.onPause();
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        ArrayList<addRefri> dataSet = new ArrayList<>();

        for (DataSnapshot child : snapshot.getChildren()) {
            addRefri data = child.getValue(addRefri.class);
            data.setKey(child.getKey());

            dataSet.add(data);
        }

        adapter.submitList(dataSet);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        error.toException().printStackTrace();
    }


    private static class FreezerAdapter extends ListAdapter<addRefri, FreezerAdapter.FreezerItemViewHolder> {

        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN);
        private Consumer<addRefri> onItemDeleteListener;
        private final ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) return;

                if (onItemDeleteListener != null) {
                    onItemDeleteListener.accept(getItem(position));
                }
            }
        };


        protected FreezerAdapter() {
            super(new DiffUtil.ItemCallback<addRefri>() {
                @Override
                public boolean areItemsTheSame(@NonNull addRefri oldItem, @NonNull addRefri newItem) {
                    return oldItem.getKey().equals(newItem.getKey());
                }

                @Override
                public boolean areContentsTheSame(@NonNull addRefri oldItem, @NonNull addRefri newItem) {
                    return oldItem.getName().equals(newItem.getName()) &&
                            oldItem.getLastDate() == newItem.getLastDate() &&
                            oldItem.getMemo().equals(newItem.getMemo());
                }

                @Override
                public Object getChangePayload(@NonNull addRefri oldItem, @NonNull addRefri newItem) {
                    return new Object();
                }
            });
        }

        public void setOnItemDeleteListener(Consumer<addRefri> onItemDeleteListener) {
            this.onItemDeleteListener = onItemDeleteListener;
        }

        @NonNull
        @Override
        public FreezerItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new FreezerItemViewHolder(inflater.inflate(R.layout.item_freezer, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull FreezerItemViewHolder holder, int position) {
            addRefri data = getItem(position);

            long lLastDate = data.getLastDate();
            Date dLastDate = new Date(lLastDate);
            String sLastDate = dateFormat.format(dLastDate);

            holder.nameTextView.setText(data.getName());
            holder.lastDateTextView.setText(sLastDate);
//            holder.lastDateTextView.setText(Long.toString(data.getLastDate()));
            holder.memoTextView.setText(data.getMemo());
        }

        static class FreezerItemViewHolder extends RecyclerView.ViewHolder {

            TextView nameTextView;
            TextView lastDateTextView;
            TextView memoTextView;

            public FreezerItemViewHolder(@NonNull View itemView) {
                super(itemView);

                nameTextView = itemView.findViewById(R.id.txtItemName);
                lastDateTextView = itemView.findViewById(R.id.txtItemLastDate);
                memoTextView = itemView.findViewById(R.id.txtItemMemo);
            }
        }
    }
}