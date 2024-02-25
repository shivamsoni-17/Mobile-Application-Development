package com.dk7aditya.firebaseimagerecognitionthroughml.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dk7aditya.firebaseimagerecognitionthroughml.R;
import com.dk7aditya.firebaseimagerecognitionthroughml.models.FirebaseUserListItem;

import java.util.ArrayList;
import java.util.List;


public class FirebaseUserNameAdapter extends RecyclerView.Adapter<FirebaseUserNameAdapter.FirebaseUserViewHolder> implements Filterable {
    private ArrayList<FirebaseUserListItem> exampleList;
    private ArrayList<FirebaseUserListItem> exampleListFull;
    private OnUserListClickListener mOnUserListClickListener;
    private ArrayList<String> selectedIds = new ArrayList<String>();
    public class FirebaseUserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textView1;
        OnUserListClickListener onUserListClickListener;
        public FirebaseUserViewHolder(@NonNull View itemView, OnUserListClickListener onUserListClickListener) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.userFirebaseName);
            this.onUserListClickListener = onUserListClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final FirebaseUserListItem model = exampleList.get(getAdapterPosition());
            model.setSelected(!model.isSelected());
            v.setBackgroundColor(model.isSelected() ? Color.CYAN : Color.WHITE);
            if(model.isSelected()){
                listOfSelectedUsers(exampleList.get(getAdapterPosition()).getText1(), true);
            }else{
                listOfSelectedUsers(exampleList.get(getAdapterPosition()).getText1(), false);
            }
            onUserListClickListener.onUserListClick(getAdapterPosition());
            notifyDataSetChanged();
        }
    }
    private void listOfSelectedUsers(String s, boolean x){
        if(x){
            if(!selectedIds.contains(s)){
                selectedIds.add(s);
            }
        }else{
            if(selectedIds.contains(s)){
                selectedIds.remove(s);
            }
        }
    }
    public ArrayList<String> getListOfSelectedUsers(){
        return selectedIds;
    }
    public interface OnUserListClickListener{
        void onUserListClick(int position);
    }
    public FirebaseUserNameAdapter(ArrayList<FirebaseUserListItem> exampleList, OnUserListClickListener onUserListClickListener) {
        this.exampleList = exampleList;
        this.mOnUserListClickListener = onUserListClickListener;
        exampleListFull = new ArrayList<>(exampleList);
    }
    @NonNull
    @Override
    public FirebaseUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.firebase_user_names_list, parent, false);
        return new FirebaseUserViewHolder(v, mOnUserListClickListener);
    }
    @Override
    public void onBindViewHolder(@NonNull FirebaseUserViewHolder holder, int position) {
        holder.textView1.setText(exampleList.get(position).getText1());
        holder.textView1.setBackgroundColor(exampleList.get(position).isSelected() ? Color.CYAN : Color.WHITE);

    }
    @Override
    public int getItemCount() {
        return exampleList.size();
    }
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<FirebaseUserListItem> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (FirebaseUserListItem item : exampleListFull) {

                    if (item.getText1().toLowerCase().contains(filterPattern)) {

                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            exampleList.clear();
            exampleList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

}
