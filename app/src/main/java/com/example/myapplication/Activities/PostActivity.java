package com.example.myapplication.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Models.Posts;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;

import static com.example.myapplication.Activities.RegisterActivity.GalleryCode;

public class PostActivity extends AppCompatActivity {

    private ImageView imgUser, imgPost;
    private TextView tvUserName;
    private EditText edtDescribe;
    private Button btnPost;
    private FirebaseAuth mAuth;
    private DatabaseReference postRef;
    private StorageReference mStorage;

    private Uri pickedImage;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mapping();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        tvUserName.setText(currentUser.getDisplayName());
        Glide.with(this).load(currentUser.getPhotoUrl()).into(imgUser);
        imgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        btnPost.setOnClickListener(new View.OnClickListener() {
            final String description = edtDescribe.getText().toString();
            @Override
            public void onClick(View v) {
                if(description.isEmpty() || imgPost.equals(null)) {
                    mStorage = FirebaseStorage.getInstance().getReference().child("post_image");
                    final StorageReference filePath = mStorage.child(pickedImage.getLastPathSegment());
                    filePath.putFile(pickedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
                                    SimpleDateFormat time = new SimpleDateFormat("HH:mm");
                                    String imageLink = uri.toString();
                                    Posts post = new Posts(
                                            currentUser.getDisplayName(),
                                            date.toString(),
                                            time.toString(),
                                            description,
                                            imageLink,
                                            currentUser.getPhotoUrl().toString());
                                    uploadPost(post);
                                }
                            });
                        }
                    });
                }
            }
        });

    }

    private void uploadPost(Posts post) {

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        postRef = mDatabase.getReference("Posts").push();

        String key = postRef.getKey();
        post.setPostKey(key);

        postRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                updateUI();
            }
        });

    }

    private void updateUI() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GalleryCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GalleryCode && resultCode == RESULT_OK && data != null) {
            pickedImage = data.getData();
            imgPost.setImageURI(pickedImage);
        }
    }

    private void mapping() {
        imgUser = findViewById(R.id.post_profile_image);
        imgPost = findViewById(R.id.post_image);
        tvUserName = findViewById(R.id.post_user_name);
        edtDescribe = findViewById(R.id.post_description);
        btnPost = findViewById(R.id.post_button);
    }

}