package com.example.alcchallenge2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alcchallenge2.DealActivity;
import com.example.alcchallenge2.R;

public class AuthenticationActivity extends AppCompatActivity {
    private static final String TAG = "create Account";
    private Button emailButton, googleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication);

        initViews();
        initListeners();

    }

    private void initViews() {
        emailButton = findViewById(R.id.signInWithEmail);
        googleButton = findViewById(R.id.signInWithGoogle);

    }

    private void initListeners() {
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(AuthenticationActivity.this, DealActivity.class);
                startActivity(newIntent);

                //startActivity(new Intent(AuthenticationActivity.this, DealActivity.class));
            }
        });

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
