package com.dk7aditya.firebaseimagerecognitionthroughml;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.dk7aditya.firebaseimagerecognitionthroughml.adapters.ImageListRecyclerAdapter;
import com.dk7aditya.firebaseimagerecognitionthroughml.fragments.ActivityFragment;
import com.dk7aditya.firebaseimagerecognitionthroughml.models.ImageList;
import com.dk7aditya.firebaseimagerecognitionthroughml.util.VerticalSpacingItemDecorator;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DisplayGroupInfo extends AppCompatActivity implements ImageListRecyclerAdapter.OnImageNameListener{
    private static final String TAG = "DisplayGroupInfo";
    private StorageReference mStorageReference, mImageRef;
    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;
    private RecyclerView mRecyclerViewGroupList;
    private ArrayList<ImageList> mImageList = new ArrayList<>();
    private ImageListRecyclerAdapter mImageNameListRecyclerAdapter;
    private String groupName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_group_info);
        groupName = getIntent().getStringExtra("groupName");
        mRecyclerViewGroupList = findViewById(R.id.recyclerViewGroupNameList);
        mStorage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mStorageReference = mStorage.getReference();
        initRecyclerView();
        insertFakeImageNames();

    }
    public void readData(MyCallBack myCallBack){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Groups")
                .document(groupName)
                .collection("groupMembers")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        ArrayList<String> eventList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String s = document.getData().get("finalUsers").toString();
                            eventList.add(s);
                            Log.i("jkdsaf", document.getData().get("finalUsers").toString());

                        }
                        myCallBack.onCallback(eventList);
                    }
                });
    }
    private void insertFakeImageNames(){
        mImageList.clear();
        readData(new MyCallBack() {
            @Override
            public void onCallback(ArrayList<String> eventList) {
                String s = eventList.get(0);
                s = s.substring(1, s.length() - 1);
                String[] arrOfStr = s.split(", ");
                for(int i=0; i<arrOfStr.length; ++i){
                    mImageRef = mStorageReference.child(arrOfStr[i]);
                    ImageList imageList = new ImageList();
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

            }
        });
    }
    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewGroupList.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecoratorDisplayGroup = new VerticalSpacingItemDecorator(10);
        mRecyclerViewGroupList.addItemDecoration(itemDecoratorDisplayGroup);
        mImageNameListRecyclerAdapter = new ImageListRecyclerAdapter(mImageList, DisplayGroupInfo.this);
        mRecyclerViewGroupList.setAdapter(mImageNameListRecyclerAdapter);
    }

    @Override
    public void onImageNameClick(int position) {
        Intent displayImageFromPosition = new Intent(this,DisplayPersonImage.class);
        displayImageFromPosition.putExtra("TEMPERATURE", mImageList.get(position).getTitle().split(" ")[2]);
        displayImageFromPosition.putExtra("IMAGEURL",mImageList.get(position).getContent());
        startActivity(displayImageFromPosition);
    }
    public interface MyCallBack{
        void onCallback(ArrayList<String> eventList);
    }
}