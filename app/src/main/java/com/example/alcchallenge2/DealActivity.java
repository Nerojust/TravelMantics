package com.example.alcchallenge2;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        FirebaseUtil.openFirebaseReference("traveldeals");
        firebaseDatabase = FirebaseUtil.firebaseDatabase;
        databaseReference = FirebaseUtil.databaseReference;

        initViews();
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
        titleInput = titleEdittext.getText().toString().trim();
        descriptionInput = descriptionEdittext.getText().toString().trim();
        priceInput = priceEdittext.getText().toString().trim();
        TravelDeal travelDeal = new TravelDeal(titleInput, descriptionInput,priceInput, "");
        databaseReference.push().setValue(travelDeal);

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
