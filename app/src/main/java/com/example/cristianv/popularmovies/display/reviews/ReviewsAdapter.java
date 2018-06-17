package com.example.cristianv.popularmovies.display.reviews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cristianv.popularmovies.R;
import com.example.cristianv.popularmovies.display.movie.MovieReviewModel;
import com.example.cristianv.popularmovies.display.movies.MovieViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cristian on 11/21/17.
 */

class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>{
    private List<MovieReviewModel> items;
    private int numberOfReviews;
    final private ReviewsContract.ListItemClickListener listener;

    public ReviewsAdapter(ReviewsContract.ListItemClickListener onClickListener){
        this.listener = onClickListener;
        this.items = new ArrayList<>();
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.reviews_list_item, parent, false);

        ReviewViewHolder reviewViewHolder = new ReviewViewHolder(view, listener);

        return reviewViewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, final int position) {
        holder.bind(items.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onListItemClick(items.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return numberOfReviews;
    }

    public void clearItemCount(){
        this.items.clear();

    }

    // SETTERS
    public void setItems(List<MovieReviewModel> items){
        this.items = items;
        this.numberOfReviews = items.size();
    }


    // VIEW HOLDER
    class ReviewViewHolder extends RecyclerView.ViewHolder{
        private ReviewsContract.ListItemClickListener listener;

        @BindView (R.id.tv_review_author)
        TextView reviewAuthor;
        @BindView (R.id.tv_review_content)
        TextView reviewContent;


        private ReviewViewHolder(View itemView, ReviewsContract.ListItemClickListener listener){
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
        }

        void bind (MovieReviewModel movieReviewModel){
            reviewAuthor.setText(movieReviewModel.getAuthor());
            reviewContent.setText(movieReviewModel.getContent());
        }
    }
}
