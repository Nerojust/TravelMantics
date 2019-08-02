package com.example.alcchallenge2.utils;

import com.example.alcchallenge2.model.TravelDeal;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FirebaseUtil {
    public static FirebaseDatabase firebaseDatabase;
    public static DatabaseReference databaseReference;
    public static FirebaseUtil firebaseUtil;
    public static ArrayList<TravelDeal> dealArrayList;


    private FirebaseUtil(){

    }

    public static void openFirebaseReference(String reference){
        if (firebaseUtil ==null){
            firebaseUtil = new FirebaseUtil();
            firebaseDatabase =FirebaseDatabase.getInstance();

        }
        dealArrayList = new ArrayList<>();
        databaseReference = firebaseDatabase.getReference().child(reference);
    }

}
