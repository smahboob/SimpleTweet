package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.LinkedList;
import java.util.List;

import okhttp3.Headers;

public class TimeLineActivity extends AppCompatActivity {

    TwitterClient client;
    RecyclerView recyclerView;
    List<Tweet> tweets;
    TweetsAdapter tweetsAdapter;
    SwipeRefreshLayout swipeRefreshLayoutContainer;
    EndlessRecyclerViewScroll scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

        client = TwitterApplication.getRestClient(this);
        recyclerView = findViewById(R.id.timeline_recyclerview);
        tweets = new LinkedList<>();

        swipeRefreshLayoutContainer = findViewById(R.id.swipeContainer);
        handleRefreshContainer();
        updateAdapters();

        populateHomeTimeLine();
    }

    private void handleRefreshContainer() {
        swipeRefreshLayoutContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateHomeTimeLine();
            }
        });
        swipeRefreshLayoutContainer.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void populateHomeTimeLine(){
        client.getHomeTimeLine(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONArray jsonArray = json.jsonArray;
                try {
                    tweetsAdapter.clear();
                    tweetsAdapter.addAll(Tweet.listFromJsonArray(jsonArray));
                    swipeRefreshLayoutContainer.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Toast.makeText(TimeLineActivity.this, "Failed to load API data!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateAdapters() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        tweetsAdapter = new TweetsAdapter(this,tweets);
        recyclerView.setAdapter(tweetsAdapter);
        recyclerView.setLayoutManager(layoutManager);

        scrollListener = new EndlessRecyclerViewScroll(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMoreData();
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    private void loadMoreData(){
        long lastTweetId = tweets.get(tweets.size()-1).tweet_id;
        client.getNextPageOfTweets(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONArray jsonArray = json.jsonArray;
                try {
                    tweetsAdapter.addAll(Tweet.listFromJsonArray(jsonArray));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        }, lastTweetId);
    }
}