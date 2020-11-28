package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private ImageView imgUserPhoto;
    private Button btnConfirm;
    private EditText edtUserName, edtUserMail, edtUserPassword, edtUserConfirm;
    private ProgressBar pgbLoading;

    private FirebaseAuth mAuth;
    private StorageReference mStorage;

    static int PReqCode = 1;
    static final int GalleryCode = 1;
    Uri pickedImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mapping();
        imgUserPhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= 22) checkAndRequestPermission();
                else openGallery();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnConfirm.setVisibility(View.INVISIBLE);
                pgbLoading.setVisibility(View.VISIBLE);
                final String name = edtUserName.getText().toString();
                final String mail = edtUserMail.getText().toString();
                final String password = edtUserPassword.getText().toString();
                final String confirm = edtUserConfirm.getText().toString();

                if(mail.isEmpty() ||
                        name.isEmpty() ||
                        password.isEmpty()
                        ) {
                    btnConfirm.setVisibility(View.VISIBLE);
                    pgbLoading.setVisibility(View.INVISIBLE);
                    sendMessage("Please fulfill all field!");
                } else if(!password.equals(confirm)) {
                    sendMessage("Wrong confirm password!");
                } else if(pickedImageUri == null) {
                    sendMessage("Choose the profile picture!");
                } else {
                    createAccount(name, mail, password);
                }

            }

        });
    }

    private void createAccount(final String name, String mail, String password) {
        mAuth = FirebaseAuth.getInstance();

        mStorage = FirebaseStorage.getInstance().getReference().child("profile_image");
        mAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(this,
                new OnCompleteListener< AuthResult >() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            sendMessage("Account Creation successfully!");
                            updateUI();
                            updateProfile(name, pickedImageUri, mAuth.getCurrentUser());
                            finish();
                        } else {
                            sendMessage("Fail " + task.getException().getMessage());
                            btnConfirm.setVisibility(View.VISIBLE);
                            pgbLoading.setVisibility(View.INVISIBLE);
                        }

                    }
                });
    }

    private void updateProfile(final String name, Uri uri, final FirebaseUser user) {
        final StorageReference filePath = mStorage.child(pickedImageUri.getLastPathSegment());
        filePath.putFile(pickedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();
                        user.updateProfile(userProfileChangeRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                sendMessage("Register successfully!");
                                updateUI();
                            }
                        });
                    }
                });
            }
        });
    }

    private void updateUI() {

        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();

    }

    private void sendMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void mapping() {

        imgUserPhoto    =  findViewById(R.id.reg_image);
        btnConfirm      =  findViewById(R.id.reg_btn);
        edtUserMail     =  findViewById(R.id.reg_mail);
        edtUserName     =  findViewById(R.id.reg_name);
        edtUserPassword =  findViewById(R.id.reg_password);
        edtUserConfirm  =  findViewById(R.id.reg_confirm);
        pgbLoading      =  findViewById(R.id.reg_pgb);

    }
    private void checkAndRequestPermission() {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }

        }
        else {
            openGallery();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GalleryCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GalleryCode && resultCode == RESULT_OK && data != null) {
            pickedImageUri = data.getData();
            imgUserPhoto.setImageURI(pickedImageUri);

        }
    }

}