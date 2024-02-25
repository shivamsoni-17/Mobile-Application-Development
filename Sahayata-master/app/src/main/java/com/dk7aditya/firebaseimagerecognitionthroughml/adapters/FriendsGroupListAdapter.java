package com.dk7aditya.firebaseimagerecognitionthroughml.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dk7aditya.firebaseimagerecognitionthroughml.R;
import com.dk7aditya.firebaseimagerecognitionthroughml.models.Group;
import com.dk7aditya.firebaseimagerecognitionthroughml.models.ImageList;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import com.dk7aditya.firebaseimagerecognitionthroughml.R;

public class FriendsGroupListAdapter extends RecyclerView.Adapter<FriendsGroupListAdapter.ViewHolder> {
    private ArrayList<Group> mGroup;
    private OnFriendsListListener mOnFriendsListListener;

    public FriendsGroupListAdapter(ArrayList<Group> mGroup, OnFriendsListListener mOnFriendsListListener) {
        this.mGroup = mGroup;
        this.mOnFriendsListListener = mOnFriendsListListener;
    }

    @NonNull
    @Override
    public FriendsGroupListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewGroup = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new ViewHolder(viewGroup, mOnFriendsListListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsGroupListAdapter.ViewHolder holder, int position) {
        holder.groupNameFriends.setText(mGroup.get(position).getGroupName());
        holder.groupDescFriends.setText(mGroup.get(position).getGroupDesc());
    }

    @Override
    public int getItemCount() {
        return mGroup.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView groupNameFriends, groupDescFriends;
        OnFriendsListListener onFriendsListListener;
        public ViewHolder(@NonNull View itemView, OnFriendsListListener onFriendsListListener) {
            super(itemView);
            groupNameFriends = itemView.findViewById(R.id.groupNameFriends);
            groupDescFriends = itemView.findViewById(R.id.groupDescriptionFriends);
            this.onFriendsListListener = onFriendsListListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onFriendsListListener.onGroupNameClick(getAdapterPosition());
        }
    }
    public interface OnFriendsListListener{
        void onGroupNameClick(int position);
    }
}
