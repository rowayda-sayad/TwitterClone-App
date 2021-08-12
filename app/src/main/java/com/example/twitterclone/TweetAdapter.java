package com.example.twitterclone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.MyTweetHolder> {

    Context context;
    private List<Tweet> tweetList;

    public TweetAdapter(Context context, List<Tweet> tweets) {
        this.tweetList = tweets;
        this.context = context;
    }

    @NonNull
    @Override
    public MyTweetHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new MyTweetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTweetHolder holder, int position) {
        Tweet tweet = tweetList.get(position);
        holder.name.setText(tweet.getDisplayName());
        holder.username.setText("@" + tweet.getUsername());
        holder.tweetContent.setText(tweet.getTweet());
        holder.time.setText(tweet.getPublishedTime());
    }

    @Override
    public int getItemCount() {
        return tweetList.size();
    }

    public class MyTweetHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public TextView name;
        public TextView tweetContent;
        public TextView time;


        public MyTweetHolder(View v) {
            super(v);
            username = v.findViewById(R.id.username);
            name = v.findViewById(R.id.name);
            tweetContent = v.findViewById(R.id.tweetContent);
            time = v.findViewById(R.id.time);

        }
    }


}
