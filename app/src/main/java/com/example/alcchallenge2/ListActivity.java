package com.example.alcchallenge2;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alcchallenge2.adapter.DealsAdapter;
import com.example.alcchallenge2.model.TravelDeal;
import com.example.alcchallenge2.utils.FirebaseUtil;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    ArrayList<TravelDeal> travelDeals;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ChildEventListener childEventListener;
    private TextView tvDeals;
    private TravelDeal travelDeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        FirebaseUtil.openFirebaseReference(this, "traveldeals");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.list_activity_menu, menu);
        if (FirebaseUtil.isAdmin==true){
            menu.findItem(R.id.insert_menu).setVisible(true);
        }else {
            menu.findItem(R.id.insert_menu).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert_menu:
                startActivity(new Intent(this, DealActivity.class));
                finish();

                return true;
            case R.id.logout_menu:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("Logout", "user logged out: ");
                                FirebaseUtil.attachListener();
                            }
                        });
                FirebaseUtil.detachListenter();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    public void showMenu(){
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("Logout", "user logged out: ");
                        FirebaseUtil.attachListener();
                    }
                });
        FirebaseUtil.detachListenter();
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        FirebaseUtil.detachListenter();
        super.onPause();
    }

    @Override
    protected void onResume() {
        FirebaseUtil.attachListener();
        FirebaseUtil.openFirebaseReference(this, "traveldeals");
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final DealsAdapter dealsAdapter = new DealsAdapter();
        recyclerView.setAdapter(dealsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        super.onResume();
    }
}
