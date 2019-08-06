package com.example.alcchallenge2;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alcchallenge2.model.TravelDeal;
import com.example.alcchallenge2.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static com.example.alcchallenge2.utils.FirebaseUtil.storageReference;

public class DealActivity extends AppCompatActivity {
    private static final String TAG = "Signup";
    EditText titleEdittext, priceEdittext, descriptionEdittext;
    Button imageButton;
    ImageView imageView;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private TravelDeal travelDeal;
    private StorageTask<UploadTask.TaskSnapshot> uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal);

        firebaseDatabase = FirebaseUtil.firebaseDatabase;
        databaseReference = FirebaseUtil.databaseReference;


        Intent intent = getIntent();
        TravelDeal travelDeal = (TravelDeal) intent.getSerializableExtra("Deal");
        if (travelDeal == null) {
            travelDeal = new TravelDeal();
        }
        initViews();
        this.travelDeal = travelDeal;
        titleEdittext.setText(travelDeal.getTitle());
        descriptionEdittext.setText(travelDeal.getDescription());
        priceEdittext.setText(travelDeal.getPrice());
        if (travelDeal.getImageUrl() != null) {
            try {
                showImage(travelDeal.getImageUrl());
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void initViews() {
        titleEdittext = findViewById(R.id.titleEdittext);
        priceEdittext = findViewById(R.id.priceEditext);
        descriptionEdittext = findViewById(R.id.descriptionEdittext);
        imageButton = findViewById(R.id.imageButton);
        imageView = findViewById(R.id.imageView);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(intent.createChooser(intent, "Insert Picture"), 43);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_menu_id:
                saveDeal();
                Toast.makeText(this, "deal saved", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_menu, menu);
        if (FirebaseUtil.isAdmin == true) {
            menu.findItem(R.id.delete_menu).setVisible(true);
            menu.findItem(R.id.save_menu_id).setVisible(true);
            enableEditexts(true);
        } else {
            menu.findItem(R.id.delete_menu).setVisible(false);
            menu.findItem(R.id.save_menu_id).setVisible(false);
            enableEditexts(false);
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 43 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            final StorageReference reference = storageReference.child(uri.getLastPathSegment());
            uploadTask = reference.putFile(uri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageName = reference.getDownloadUrl().toString();
                            travelDeal.setImageName(imageName);
                            String url = uri.toString();
                            Toast.makeText(DealActivity.this, url, Toast.LENGTH_SHORT).show();
                            travelDeal.setImageUrl(url);
                            Log.d(TAG, "Url: ".concat(url));
                            Log.d(TAG, "Name: ".concat(imageName));

                            if (url != null) {
                                showImage(url);
                            }
                        }
                    });

                    uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            System.out.println("Upload is " + progress + "% done");
                        }
                    }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                            System.out.println("Upload is paused");

                        }
                    });
                }
            });
        }
    }


    public void enableEditexts(boolean isEnabled) {
        titleEdittext.setEnabled(isEnabled);
        descriptionEdittext.setEnabled(isEnabled);
        priceEdittext.setEnabled(isEnabled);
    }


    private void saveDeal() {
        travelDeal.setTitle(titleEdittext.getText().toString().trim());
        travelDeal.setDescription(descriptionEdittext.getText().toString().trim());
        travelDeal.setPrice(priceEdittext.getText().toString().trim());
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
        }
        databaseReference.child(travelDeal.getId()).removeValue();
        if (travelDeal.getImageName() != null && !travelDeal.getImageName().isEmpty()) {
            StorageReference picRef = storageReference.child(travelDeal.getImageName());
            picRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(DealActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(DealActivity.this, "failed to delete", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void backToListActivity() {
        startActivity(new Intent(this, ListActivity.class));
        finish();
    }


    private void showImage(String url) {
        int deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.ic_launcher_background)
                .resize(deviceWidth, deviceWidth * 2 / 3)
                .centerCrop()
                .into(imageView);
    }
}
   /* private Boolean validateViews() {
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
}*/
