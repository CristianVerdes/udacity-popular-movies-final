package com.example.cristianv.popularmovies.display.movie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.cristianv.popularmovies.R;
import com.example.cristianv.popularmovies.utilities.NetworkUtils;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cristian.verdes on 21.02.2018.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideosViewHolder> {
    private List<String> videos;
    private Context context;
    private MovieContract.OnPlayVideoListener listener;

    public VideosAdapter (Context context, MovieContract.OnPlayVideoListener listener) {
        this.videos = new ArrayList<>();
        this.context = context;
        this.listener = listener;
    }

    public void setVideos(List<String> videos) {
        this.videos = videos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public List<String> getVideos() {
        return videos;
    }

    @Override
    public VideosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.videos_list_item, parent, false);

        return new VideosViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(final VideosViewHolder holder, final int position) {
        holder.youTubeThumbnailView.initialize(NetworkUtils.YT_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(videos.get(position));
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        youTubeThumbnailView.setVisibility(View.VISIBLE);
                        youTubeThumbnailLoader.release();
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });

        holder.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPlayVideoClicked(videos.get(position));
            }
        });
    }

    public class VideosViewHolder extends RecyclerView.ViewHolder {
        YouTubeThumbnailView youTubeThumbnailView;
        protected ImageView playButton;

        public VideosViewHolder(View itemView, final MovieContract.OnPlayVideoListener listener) {
            super(itemView);
            playButton = itemView.findViewById(R.id.iv_play_button);
            youTubeThumbnailView = itemView.findViewById(R.id.ytv_trailer);

            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPlayVideoClicked(videos.get(getLayoutPosition()));
                }
            });
        }
    }
}
