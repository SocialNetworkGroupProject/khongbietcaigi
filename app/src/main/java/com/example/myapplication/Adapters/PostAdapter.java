package com.example.myapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Activities.CommentActivity;
import com.example.myapplication.Models.Posts;
import com.example.myapplication.R;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    static Context mContext;
    static List<Posts> postList;

    public PostAdapter(Context mContext, List<Posts> postList) {
        this.mContext = mContext;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.all_posts_layout, parent,false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.tvUserName.setText(postList.get(position).getUserName());
        holder.tvDescription.setText(postList.get(position).getDescription());
        Glide.with(mContext).load(postList.get(position).getDownloadLink()).into(holder.imgPost);
        Glide.with(mContext).load(postList.get(position).getUserPhoto()).into(holder.imgUser);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {

        TextView tvUserName;
        TextView tvDescription;

        ImageView imgPost;
        ImageView imgUser;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.home_post_user_name);
            imgPost = itemView.findViewById(R.id.home_post_image);
            imgUser = itemView.findViewById(R.id.home_post_user_photo);
            tvDescription = itemView.findViewById(R.id.home_post_description);

            tvDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, CommentActivity.class);

                    int position = getAdapterPosition();

                    intent.putExtra("postUsername", postList.get(position).getUserName());
                    intent.putExtra("postPhoto", postList.get(position).getDownloadLink());
                    intent.putExtra("postTime", postList.get(position).getTime());
                    intent.putExtra("postDesc", postList.get(position).getDate());

                    mContext.startActivity(intent);
                }
            });

        }
    }
}
