package com.example.reviewinator;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private ArrayList<ReviewItem> reviewItems;

    public static class ReviewViewHolder extends RecyclerView.ViewHolder{
        private TextView author;
        private TextView rating;
        private com.example.reviewinator.EpiText review;
        //private ExpandableTextView review

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.author);
            rating = itemView.findViewById(R.id.rating);
            review = itemView.findViewById(R.id.review);
        }
    }

    public ReviewAdapter(ArrayList<ReviewItem> reviewItems){
        this.reviewItems=reviewItems;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item,parent,false);
        ReviewViewHolder rvh = new ReviewViewHolder(v);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ReviewItem item = reviewItems.get(position);
        holder.author.setText(item.getAuthor());
        holder.rating.setText(item.getRating());
        holder.review.setText(item.getReview());
    }

    @Override
    public int getItemCount() {
        return reviewItems.size();
    }
}

