package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Models.Comment;
import com.example.myapplication.R;

import java.util.List;
import java.util.zip.Inflater;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context mContext;
    private List<Comment> commentList;

    public CommentAdapter(Context mContext, List<Comment> commentList) {
        super();
        this.commentList = commentList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.all_comment_layout, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {

        holder.tvUsername.setText(commentList.get(position).getUserName());
        holder.tvComment.setText(commentList.get(position).getComment());
        holder.tvTime.setText(commentList.get(position).getTime());
        Glide.with(mContext).load(commentList.get(position).getUserPhoto()).into(holder.imgUserPhoto);

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        ImageView imgUserPhoto;
        TextView tvUsername;
        TextView tvTime;
        TextView tvComment;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUserPhoto = itemView.findViewById(R.id.comment_user_photo);
            tvComment = itemView.findViewById(R.id.comment_comment);
            tvTime = itemView.findViewById(R.id.comment_time);
            tvUsername = itemView.findViewById(R.id.comment_user_name);

        }
    }
}
