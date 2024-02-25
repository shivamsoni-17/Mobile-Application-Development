package com.dk7aditya.firebaseimagerecognitionthroughml.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dk7aditya.firebaseimagerecognitionthroughml.APIClient;
import com.dk7aditya.firebaseimagerecognitionthroughml.APIInterface;
import com.dk7aditya.firebaseimagerecognitionthroughml.R;
import com.dk7aditya.firebaseimagerecognitionthroughml.ViewNewsSelected;
import com.dk7aditya.firebaseimagerecognitionthroughml.adapters.FeedListRecyclerAdapter;
import com.dk7aditya.firebaseimagerecognitionthroughml.models.Article;
import com.dk7aditya.firebaseimagerecognitionthroughml.models.FeedList;
import com.dk7aditya.firebaseimagerecognitionthroughml.models.ResponseModel;
import com.dk7aditya.firebaseimagerecognitionthroughml.util.VerticalSpacingItemDecorator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class
FeedFragment extends Fragment implements FeedListRecyclerAdapter.OnFeedNameListener {
    private String API_KEY = "";
    private ArrayList <FeedList> mFeedList = new ArrayList<>();
    private FeedList mFeedListArray;
    private FeedListRecyclerAdapter mFeedListRecyclerAdapter;
    private RecyclerView mRecyclerViewFeed;
    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        mRecyclerViewFeed = view.findViewById(R.id.recyclerViewFeedList);
        initFeedRecyclerView();
        insertFakeNews();
        return view;
    }
    private void insertFakeNews(){
        mFeedList.clear();
        mFeedListArray = new FeedList();
        final APIInterface apiService = APIClient.getClient().create(APIInterface.class);
        if (API_KEY.isEmpty()){
            //Do Nothing.
        }else {
            Call<ResponseModel> call = apiService.getLatestNews("in", "covid", API_KEY);
            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    if (response.body().getStatus().equals("ok")) {
                        List<Article> articleList = response.body().getArticles();
                        if (articleList.size() > 0) {
                            for (int i = 0; i < articleList.size(); ++i) {
                                mFeedListArray = new FeedList();
                                Article articleModel = articleList.get(i);
                                mFeedListArray.setImageNewsTitle(articleModel.getTitle());
                                mFeedListArray.setImageNewsDescription(articleModel.getDescription());
                                mFeedListArray.setImageNewsUrl(articleModel.getUrlToImage());
                                mFeedListArray.setNewsUrl(articleModel.getUrl());
                                Log.d("Position: " + i, mFeedListArray.toString());
                                mFeedList.add(mFeedListArray);
                                mFeedListRecyclerAdapter.notifyDataSetChanged();
                            }

                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Log.d("onFailure", t.getMessage());
                }
            });
        }
    }
    private void initFeedRecyclerView(){
        LinearLayoutManager linearLayoutManagerFeed = new LinearLayoutManager(getContext());
        mRecyclerViewFeed.setLayoutManager(linearLayoutManagerFeed);
        VerticalSpacingItemDecorator itemDecoratorFeed = new VerticalSpacingItemDecorator(15);
        mRecyclerViewFeed.addItemDecoration(itemDecoratorFeed);
        mFeedListRecyclerAdapter = new FeedListRecyclerAdapter (mFeedList, FeedFragment.this);
        mRecyclerViewFeed.setAdapter(mFeedListRecyclerAdapter);
    }

    @Override
    public void onFeedNameClick(int position) {
        //Toast.makeText(getContext(), "Position: " + (position+1), Toast.LENGTH_SHORT).show();
        Intent goToViewNews = new Intent(getContext(), ViewNewsSelected.class);
        goToViewNews.putExtra("NEWSURL", mFeedList.get(position).getNewsUrl());
        startActivity(goToViewNews);
    }
}