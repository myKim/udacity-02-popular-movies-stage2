package com.myoung.android.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.myoung.android.popularmovies.R;
import com.myoung.android.popularmovies.data.CastItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CastListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // TAG for logging
    private static final String TAG = CastListAdapter.class.getSimpleName();

    // Constants
    private static final int MAX_CAST_COUNT = 15;

    // Variables
    private Context context;
    private List<CastItem> mCastList;


    // Constructor
    public CastListAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutIdForListItem = R.layout.cast_list_item;
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new CastListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        CastItem castItem = mCastList.get(position);

        String castName = castItem.getName();
        String character = castItem.getCharacter();
        String profileUrl = castItem.getProfilePath();

        CastListViewHolder castListViewHolder = (CastListViewHolder) viewHolder;

        castListViewHolder.mCastNameTextView.setText(castName);
        castListViewHolder.mCastCharacterTextView.setText(character);

        if(profileUrl != null) {
            Glide.with(context)
                    .load(profileUrl)
                    .into(castListViewHolder.mCastProfileImageView);
        }
    }

    @Override
    public int getItemCount() {
        return (mCastList == null) ? 0 : mCastList.size();
    }

    public void setCastList(List<CastItem> castList) {
        if(castList != null && castList.size() > MAX_CAST_COUNT) {
            castList = castList.subList(0, MAX_CAST_COUNT);
        }
        mCastList = castList;
        notifyDataSetChanged();
    }


    // ViewHolder
    public class CastListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_cast_profile) ImageView mCastProfileImageView;
        @BindView(R.id.tv_cast_name) TextView mCastNameTextView;
        @BindView(R.id.tv_cast_character) TextView mCastCharacterTextView;

        public CastListViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
