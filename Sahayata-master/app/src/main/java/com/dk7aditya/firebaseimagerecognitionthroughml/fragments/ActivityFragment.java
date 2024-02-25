package com.dk7aditya.firebaseimagerecognitionthroughml.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dk7aditya.firebaseimagerecognitionthroughml.DisplayPersonImage;
import com.dk7aditya.firebaseimagerecognitionthroughml.R;
import com.dk7aditya.firebaseimagerecognitionthroughml.adapters.ImageListRecyclerAdapter;
import com.dk7aditya.firebaseimagerecognitionthroughml.models.ImageList;
import com.dk7aditya.firebaseimagerecognitionthroughml.util.VerticalSpacingItemDecorator;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityFragment extends Fragment implements ImageListRecyclerAdapter.OnImageNameListener{

    private static final String TAG = "ActivityFragment";
    private StorageReference mStorageReference, mImageRef;
    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;
    //UI Components
    private RecyclerView mRecyclerView;

    //vars
    private ArrayList<ImageList> mImageList = new ArrayList<>();
    private ImageListRecyclerAdapter mImageNameListRecyclerAdapter;
    public ActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerViewImageNameList);
        mStorage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mStorageReference = mStorage.getReference();
        mImageRef = mStorageReference.child(mAuth.getCurrentUser().getEmail());
        //mImageList.clear();
        insertFakeImageNames();
        initRecyclerView();
        return view;
    }
    private void insertFakeImageNames(){
        mImageList.clear();
        mImageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference item : listResult.getItems()) {
                    ImageList imageList = new ImageList();
                    imageList.setTitle(item.getName());
                    item.getDownloadUrl().addOnSuccessListener(uri -> imageList.setContent(uri.toString()));
                    imageList.setTimestamp(item.getName().split(" ")[1].substring(0,5));
                    mImageList.add(imageList);
                    mImageNameListRecyclerAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        mRecyclerView.addItemDecoration(itemDecorator);
        mImageNameListRecyclerAdapter = new ImageListRecyclerAdapter(mImageList, ActivityFragment.this);
        mRecyclerView.setAdapter(mImageNameListRecyclerAdapter);
    }

    @Override
    public void onImageNameClick(int position) {
        Log.d(TAG, "onImageNameClick: Position "+ position + " clicked.");
        Intent displayImageFromPosition = new Intent(getContext(), com.dk7aditya.firebaseimagerecognitionthroughml.DisplayPersonImage.class);
        displayImageFromPosition.putExtra("TEMPERATURE", mImageList.get(position).getTitle().split(" ")[2]);
        displayImageFromPosition.putExtra("IMAGEURL",mImageList.get(position).getContent());
        startActivity(displayImageFromPosition);
    }
}