package com.codepath.apps.restclienttemplate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import java.util.LinkedList;
import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    List<Tweet> tweets;
    Context context;

    public TweetsAdapter(Context context, List<Tweet> tweets){
        this.context = context;
        this.tweets = tweets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View timeline_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tweet, parent, false);
        return new ViewHolder(timeline_view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tweet item = tweets.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public void clear(){
        tweets.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Tweet> list){
        tweets.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView user_profile_image;
        TextView user_name;
        TextView user_tweet_data;
        TextView time_stamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            user_profile_image = itemView.findViewById(R.id.profile_image);
            user_name = itemView.findViewById(R.id.user_name);
            user_tweet_data = itemView.findViewById(R.id.tweet_detail);
            time_stamp = itemView.findViewById(R.id.timeStamp);
            user_profile_image.setClipToOutline(true);
        }

        //update the view inside of the view holder with the data
        @SuppressLint("CheckResult")
        public void bind(Tweet item) {
            user_name.setText(item.user.name);
            user_tweet_data.setText(item.body);
            time_stamp.setText(item.getFormattedTimestamp());
            Glide.with(context).load(item.user.publicImageUrl).into(user_profile_image);
        }
    }

}
