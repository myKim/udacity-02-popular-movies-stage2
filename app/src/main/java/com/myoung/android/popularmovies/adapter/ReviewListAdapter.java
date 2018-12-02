package com.myoung.android.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.transition.ChangeBounds;
import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myoung.android.popularmovies.R;
import com.myoung.android.popularmovies.data.ReviewItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ReviewViewHolder> {
    // TAG for logging
    private static final String TAG = ReviewListAdapter.class.getSimpleName();

    // Constants
    private static final int MAX_REVIEW_COUNT = 10;

    // Variables
    private List<ReviewItem> mReviewList;
    private Context context;
    private RecyclerView recyclerView;
    private int mExpandedPosition = -1;


    // Constructor
    public ReviewListAdapter(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
    }


    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutIdForListItem = R.layout.review_list_item;
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder viewHolder, int position) {
        ReviewItem reviewItem = mReviewList.get(position);

        String reviewAuthor = reviewItem.getAuthor();
        String reviewContent = reviewItem.getContent();

        ReviewViewHolder reviewViewHolder = (ReviewViewHolder) viewHolder;

        reviewViewHolder.mReviewAuthorTextView.setText(reviewAuthor);
        reviewViewHolder.mReviewContentTextView.setText(reviewContent);

        // expand / collapse review item by click
        final boolean isExpanded = (position == mExpandedPosition);
        reviewViewHolder.mSubItemLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        reviewViewHolder.itemView.setActivated(isExpanded);
        reviewViewHolder.itemView.setOnClickListener((v) -> {
            mExpandedPosition = isExpanded ? -1 : position;

            ChangeBounds transition = new ChangeBounds();
            transition.setDuration(150);

            TransitionManager.beginDelayedTransition(recyclerView, transition);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return (mReviewList == null) ? 0 : mReviewList.size();
    }

    public void setReviewList(List<ReviewItem> reviewList) {
        if(reviewList != null && reviewList.size() > MAX_REVIEW_COUNT) {
            reviewList = reviewList.subList(0, MAX_REVIEW_COUNT);
        }
        mReviewList = reviewList;
        notifyDataSetChanged();
    }


    // ViewHolder
    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_review_author) TextView mReviewAuthorTextView;
        @BindView(R.id.tv_review_content) TextView mReviewContentTextView;
        @BindView(R.id.sub_item) View mSubItemLayout;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
