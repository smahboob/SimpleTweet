package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    EditText composeText;
    Button tweetButton;
    public static final int MAX_TWEET_LENGTH = 280;
    public static final String MAX_TWEET_LENGTH_STRING = "280";

    TwitterClient client;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApplication.getRestClient(this);
        composeText = findViewById(R.id.editComposeText);
        tweetButton = findViewById(R.id.composeButton);
        final TextView wordCountBox = findViewById(R.id.wordCount);
        wordCountBox.setText(MAX_TWEET_LENGTH_STRING);

        //handle editing word limit
        composeText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int currentCountLeft = 140 - composeText.getText().toString().length();
                wordCountBox.setText(String.valueOf(currentCountLeft));
                if(currentCountLeft < 1){
                    wordCountBox.setText("0");
                    composeText.setError("Maximum 280 characters allowed!");
                    composeText.setFocusable(true);
                }
            }
        });

        //set listener to the button
        //make an API call to TWitter to publish the tweet
        tweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checks
                String tweetDescription = composeText.getText().toString();
                if(tweetDescription.isEmpty()){
                    composeText.setError("Enter something to tweet!");
                    composeText.setFocusable(true);
                }
                if (tweetDescription.length() > MAX_TWEET_LENGTH) {
                    composeText.setFocusable(true);
                    composeText.setError("Word Limit exceeded!");
                }

                //call the API
                if(tweetDescription.length() < MAX_TWEET_LENGTH && !tweetDescription.isEmpty()) {
                    client.postNewTweet(tweetDescription, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            try {
                                Tweet tweet = Tweet.fromJson(json.jsonObject);
                                Intent intent = new Intent();
                                intent.putExtra("tweet", Parcels.wrap(tweet));
                                setResult(RESULT_OK, intent);
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Toast.makeText(ComposeActivity.this, "Failed to tweet! Try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}