package com.example.alcchallenge2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alcchallenge2.model.TravelDeal;
import com.example.alcchallenge2.utils.FirebaseUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DealActivity extends AppCompatActivity {
    private static final String TAG = "Signup";
    EditText titleEdittext, priceEdittext, descriptionEdittext;
    private String titleInput;
    private String priceInput;
    private String descriptionInput;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private TravelDeal travelDeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        FirebaseUtil.openFirebaseReference("traveldeals");
        firebaseDatabase = FirebaseUtil.firebaseDatabase;
        databaseReference = FirebaseUtil.databaseReference;

        initViews();
        Intent intent = getIntent();
        TravelDeal gottenDeal = (TravelDeal) intent.getSerializableExtra("Deal");
        if (gottenDeal == null) {
            travelDeal = new TravelDeal();
        }
        this.travelDeal = gottenDeal;
        assert gottenDeal != null;
        titleEdittext.setText(gottenDeal.getTitle());
        descriptionEdittext.setText(gottenDeal.getDescription());
        priceEdittext.setText(gottenDeal.getPrice());
    }

    private void initViews() {
        titleEdittext = findViewById(R.id.titleEdittext);
        priceEdittext = findViewById(R.id.priceEditext);
        descriptionEdittext = findViewById(R.id.descriptionEdittext);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_menu_id:
                saveDeal();
                Toast.makeText(this, "deal saved", Toast.LENGTH_SHORT).show();
                cleanContent();
                backToListActivity();
                return true;
            case R.id.delete_menu:
                deleteDeal();
                Toast.makeText(this, "Deal deleted", Toast.LENGTH_SHORT).show();
                backToListActivity();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void cleanContent() {
        titleEdittext.setText("");
        descriptionEdittext.setText("");
        priceEdittext.setText("");
        titleEdittext.requestFocus();
    }

    private void saveDeal() {
        travelDeal.setTitle(titleEdittext.getText().toString().trim());
        travelDeal.setDescription(descriptionEdittext.getText().toString().trim());
        travelDeal.setPrice(priceEdittext.getText().toString().trim());
        TravelDeal travelDeal = new TravelDeal(titleInput, descriptionInput, priceInput, "");
        if (travelDeal.getId() == null) {
            databaseReference.push().setValue(travelDeal);
        } else {
            databaseReference.child(travelDeal.getId()).setValue(travelDeal);
        }
    }

    private void deleteDeal() {
        if (travelDeal == null) {
            Toast.makeText(this, "save data before deleting", Toast.LENGTH_SHORT).show();
            return;
        } else {
            databaseReference.child(travelDeal.getId()).removeValue();
        }
    }

    private void backToListActivity() {
        startActivity(new Intent(this, ListActivity.class));
    }

    private Boolean validateViews() {
        if (TextUtils.isEmpty(titleInput)) {
            Toast.makeText(this, "title is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(descriptionInput)) {

            Toast.makeText(this, "description is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(priceInput)) {
            Toast.makeText(this, "price is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
