package com.dk7aditya.firebaseimagerecognitionthroughml.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dk7aditya.firebaseimagerecognitionthroughml.R;
import com.dk7aditya.firebaseimagerecognitionthroughml.models.FeedList;

import java.util.ArrayList;

public class FeedListRecyclerAdapter extends RecyclerView.Adapter<FeedListRecyclerAdapter.ViewHolder> {
    private ArrayList<FeedList> mFeedNewsList;
    private OnFeedNameListener mOnFeedNameListener;


    public FeedListRecyclerAdapter(ArrayList<FeedList> mFeedNewsList, OnFeedNameListener mOnFeedNameListener) {
        this.mFeedNewsList = mFeedNewsList;
        this.mOnFeedNameListener = mOnFeedNameListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_updated_feed_list, parent, false);
        return new ViewHolder(view, mOnFeedNameListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.feedNewsTitle.setText(mFeedNewsList.get(position).getImageNewsTitle());
        holder.feedNewsDescription.setText(mFeedNewsList.get(position).getImageNewsDescription());
        Glide.with(holder.feedImageUrl.getContext())
                .asBitmap()
                .load(mFeedNewsList.get(position).getImageNewsUrl())
                .into(holder.feedImageUrl);

    }

    @Override
    public int getItemCount() {
        return mFeedNewsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView feedNewsTitle, feedNewsDescription;
        ImageView feedImageUrl;
        OnFeedNameListener onFeedNameListener;

        public ViewHolder(@NonNull View itemView, OnFeedNameListener onFeedNameListener) {
            super(itemView);
            feedNewsTitle = itemView.findViewById(R.id.feedImageTitle);
            feedNewsDescription = itemView.findViewById(R.id.feedImageDescription);
            feedImageUrl = itemView.findViewById(R.id.feedNewsImage);
            this.onFeedNameListener = onFeedNameListener;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            onFeedNameListener.onFeedNameClick(getAdapterPosition());
        }
    }
    public interface OnFeedNameListener{
        void onFeedNameClick(int position);
    }
}
