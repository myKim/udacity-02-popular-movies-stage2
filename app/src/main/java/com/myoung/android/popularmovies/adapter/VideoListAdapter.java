package com.myoung.android.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.myoung.android.popularmovies.R;
import com.myoung.android.popularmovies.data.VideoItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // TAG for logging
    private static final String TAG = VideoListAdapter.class.getSimpleName();

    // Constants
    private static final int VIEW_TYPE_DEFAULT = 0;
    private static final int VIEW_TYPE_FIRST = 1;

    // Variables
    private Context context;
    private final VideoListAdapterOnItemClickHandler mItemClickHandler;
    private final VideoListAdapterOnShareButtonClickHandler mShareButtonClickHandler;
    public List<VideoItem> mVideoList;
    private boolean mUseFirstLayout;


    // Constructor
    public VideoListAdapter(Context context,
                            VideoListAdapterOnItemClickHandler itemClickHandler,
                            VideoListAdapterOnShareButtonClickHandler shareButtonClickHandler) {
        this.context = context;
        this.mItemClickHandler = itemClickHandler;
        this.mShareButtonClickHandler = shareButtonClickHandler;
        this.mUseFirstLayout = context.getResources().getBoolean(R.bool.mUseFirstLayout);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        int layoutIdForListItem = R.layout.video_list_item;
        switch(viewType) {
            case VIEW_TYPE_DEFAULT:
                layoutIdForListItem = R.layout.video_list_item;
                break;
            case VIEW_TYPE_FIRST:
                layoutIdForListItem = R.layout.video_list_item_first;
                break;
        }

        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new VideoListViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (mUseFirstLayout && position == 0) {
            return VIEW_TYPE_FIRST;
        } else {
            return VIEW_TYPE_DEFAULT;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        VideoItem videoItem = mVideoList.get(position);
        VideoListViewHolder videoListViewHolder = (VideoListViewHolder) viewHolder;

        String videoName = videoItem.getName();
        String thumbnailUrl;
        if(getItemViewType(position) == VIEW_TYPE_FIRST) {
            thumbnailUrl = videoItem.getThumbnailUrl(VideoItem.IMAGE_HIGH_QUALITY);
        }
        else {
            thumbnailUrl = videoItem.getThumbnailUrl(VideoItem.IMAGE_MIDDLE_QUALITY);
        }

        videoListViewHolder.mVideoNameTextView.setText(videoName);

        if(thumbnailUrl != null) {
            Glide.with(context)
                    .load(thumbnailUrl)
                    .into(videoListViewHolder.mVideoThumbnailImageView);
        }
    }

    @Override
    public int getItemCount() {
        return (mVideoList == null) ? 0 : mVideoList.size();
    }

    public void setVideoList(List<VideoItem> videoList) {
        mVideoList = videoList;
        notifyDataSetChanged();
    }


    // Interface
    public interface VideoListAdapterOnItemClickHandler {
        void onItemClick(VideoItem videoItem);
    }

    public interface  VideoListAdapterOnShareButtonClickHandler {
        void onShareButtonClick(VideoItem videoItem);
    }


    // ViewHolder
    public class VideoListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_video_thumbnail) ImageView mVideoThumbnailImageView;
        @BindView(R.id.tv_video_name) TextView mVideoNameTextView;
        @BindView(R.id.b_share_video_url) Button mVideoShareButton;

        public VideoListViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
            if(mVideoShareButton != null) {
                mVideoShareButton.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            VideoItem videoItem = mVideoList.get(position);

            int id = v.getId();
            switch(id) {
                case R.id.b_share_video_url:
                    mShareButtonClickHandler.onShareButtonClick(videoItem);
                    break;
                default:
                    mItemClickHandler.onItemClick(videoItem);
                    break;
            }
        }
    }

}
