package com.dk7aditya.firebaseimagerecognitionthroughml.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dk7aditya.firebaseimagerecognitionthroughml.DisplayGroupInfo;
import com.dk7aditya.firebaseimagerecognitionthroughml.R;
import com.dk7aditya.firebaseimagerecognitionthroughml.adapters.FriendsGroupListAdapter;
import com.dk7aditya.firebaseimagerecognitionthroughml.models.Group;
import com.dk7aditya.firebaseimagerecognitionthroughml.models.GroupList;
import com.dk7aditya.firebaseimagerecognitionthroughml.util.VerticalSpacingItemDecorator;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FriendsFragment extends Fragment implements FriendsGroupListAdapter.OnFriendsListListener{
    private FirebaseAuth mAuth;
    private static final String TAG = "FriendsFragment";
    private RecyclerView mRecyclerViewFriends;
    private ArrayList<Group> mFriendsList = new ArrayList<>();
    private FriendsGroupListAdapter mFriendsGroupListAdapter;
    private Group mGroupGroup;

    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        mRecyclerViewFriends = view.findViewById(R.id.recyclerViewFriends);
        mAuth = FirebaseAuth.getInstance();
        initRecyclerViewFriends();
        insertGroupName();

        return view;
    }
    private void insertGroupName() {
        mFriendsList.clear();
        mFriendsGroupListAdapter.notifyDataSetChanged();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(mAuth.getCurrentUser().getEmail()).collection("userGroupList")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            mGroupGroup = new Group();
                            String s = document.getData().get("groupName").toString();
                            mGroupGroup.setGroupName(s);
                            //CollectionReference docReference = db.collection("Groups").document(s).collection("groupMembers");
                            mGroupGroup.setGroupDesc("");
                            mFriendsList.add(mGroupGroup);
                            mFriendsGroupListAdapter.notifyDataSetChanged();
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

    }
    private void initRecyclerViewFriends(){
        LinearLayoutManager linearLayoutManagerFriends = new LinearLayoutManager(getContext());
        mRecyclerViewFriends.setLayoutManager(linearLayoutManagerFriends);
        VerticalSpacingItemDecorator itemDecoratorFriends = new VerticalSpacingItemDecorator(10);
        mRecyclerViewFriends.addItemDecoration(itemDecoratorFriends);
        mFriendsGroupListAdapter = new FriendsGroupListAdapter(mFriendsList, FriendsFragment.this);
        mRecyclerViewFriends.setAdapter(mFriendsGroupListAdapter);
    }

    @Override
    public void onGroupNameClick(int position) {
        Intent displayGroupMessages = new Intent(getContext(), DisplayGroupInfo.class);
        displayGroupMessages.putExtra("groupName", mFriendsList.get(position).getGroupName());
        startActivity(displayGroupMessages);

    }
}