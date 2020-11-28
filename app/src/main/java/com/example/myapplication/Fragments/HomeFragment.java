package com.example.myapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.Activities.PostActivity;
import com.example.myapplication.Adapters.PostAdapter;
import com.example.myapplication.Models.Posts;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private Button btnPost;
    private RecyclerView rvPost;
    private List<Posts> postList;
    private PostAdapter postAdapter;

    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        btnPost = view.findViewById(R.id.post_button);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PostActivity.class));
            }
        });

        rvPost = view.findViewById(R.id.home_post_recycler_view);
        rvPost.setLayoutManager(new LinearLayoutManager(getActivity()));
        databaseReference = FirebaseDatabase.getInstance().getReference("Posts");

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList = new ArrayList<>();

                for (DataSnapshot postSnap: snapshot.getChildren()) {
                    Posts post = postSnap.getValue(Posts.class);
                    postList.add(post);
                }
                postAdapter = new PostAdapter(getActivity(), postList);
                rvPost.setAdapter(postAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}