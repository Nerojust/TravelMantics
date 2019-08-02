package com.example.alcchallenge2.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alcchallenge2.DealActivity;
import com.example.alcchallenge2.utils.FirebaseUtil;
import com.example.alcchallenge2.R;
import com.example.alcchallenge2.model.TravelDeal;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.DealViewHolder> {
    private ArrayList<TravelDeal> dealsList;
    private Activity activity;

    public DealsAdapter() {
        DatabaseReference databaseReference = FirebaseUtil.databaseReference;
        dealsList = FirebaseUtil.dealArrayList;
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TravelDeal travelDeal = dataSnapshot.getValue(TravelDeal.class);
                dealsList.add(travelDeal);
                if (travelDeal != null) {
                    travelDeal.setId(dataSnapshot.getKey());
                }
                notifyItemInserted(dealsList.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addChildEventListener(childEventListener);

    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.deals_adapter, parent, false);
        return new DealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
        TravelDeal travelDeal = dealsList.get(position);
        holder.bind(travelDeal);
    }

    @Override
    public int getItemCount() {
        return dealsList.size();
    }

    class DealViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitle, description, price;

        DealViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            price = itemView.findViewById(R.id.price);
            itemView.setOnClickListener(this);
        }

        void bind(TravelDeal travelDeal) {
            tvTitle.setText(travelDeal.getTitle());
            description.setText(travelDeal.getDescription());
            price.setText(travelDeal.getPrice());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Log.d("Adapter", "onClick: " + position);
            TravelDeal selectedDeal = dealsList.get(position);
            Intent intent = new Intent(view.getContext(), DealActivity.class);
            intent.putExtra("Deal", selectedDeal);
            view.getContext().startActivity(intent);

        }
    }
}
