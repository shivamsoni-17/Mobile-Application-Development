package com.dk7aditya.firebaseimagerecognitionthroughml.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.dk7aditya.firebaseimagerecognitionthroughml.R;
import com.dk7aditya.firebaseimagerecognitionthroughml.models.ImageList;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ImageListRecyclerAdapter extends RecyclerView.Adapter<ImageListRecyclerAdapter.ViewHolder>{
    private ArrayList<ImageList> mImageList;
    private OnImageNameListener mOnImageNameListener;

    public ImageListRecyclerAdapter(ArrayList<ImageList> mImageList, OnImageNameListener onImageNameListener) {
        this.mImageList = mImageList;
        this.mOnImageNameListener = onImageNameListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_updated_image_list, parent,false);
        return new ViewHolder(view, mOnImageNameListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageListRecyclerAdapter.ViewHolder holder, int position) {
        holder.timestamp.setText(mImageList.get(position).getTimestamp());
        holder.title.setText(mImageList.get(position).getTitle().substring(0,10));
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title, timestamp;
        OnImageNameListener onImageNameListener;

        public ViewHolder(@NonNull View itemView, OnImageNameListener onImageNameListener) {
            super(itemView);
            title = itemView.findViewById(R.id.image_title);
            timestamp = itemView.findViewById(R.id.image_timestamp);
            this.onImageNameListener = onImageNameListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onImageNameListener.onImageNameClick(getAdapterPosition());
        }
    }

    public interface OnImageNameListener{
        void onImageNameClick(int position);
    }
}
