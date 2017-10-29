package org.drulabs.pixelr.screens.like;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.drulabs.pixelr.R;
import org.drulabs.pixelr.dto.LikeDTO;
import org.drulabs.pixelr.firebase.FirebaseImageHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by kaushald on 25/02/17.
 */

public class LikesAdapter extends RecyclerView.Adapter<LikesAdapter.LikesVH> {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-dd, yyyy " +
            "(HH:mm:ss)");

    private List<LikeDTO> likes;
    private Context mContext;

    public LikesAdapter(Context mContext) {
        this.mContext = mContext;
        likes = new ArrayList<>();
    }

    @Override
    public LikesVH onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View likeView = LayoutInflater.from(context).inflate(R.layout.like_item, parent,
                false);
        return new LikesVH(likeView);
    }

    void append(List<LikeDTO> likeList) {
        if (likes != null && likes.size() >= 0 && likeList != null) {
            this.likes.addAll(likeList);
            Collections.sort(this.likes);
            notifyDataSetChanged();
        }
    }

    void append(LikeDTO like) {
        if (likes != null && likes.size() >= 0 && like != null) {
            this.likes.add(like);
            Collections.sort(this.likes);
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(LikesVH holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return likes == null ? 0 : likes.size();
    }

    class LikesVH extends RecyclerView.ViewHolder {
        ImageView imgLiker;
        TextView date, liker;

        public LikesVH(View itemView) {
            super(itemView);
            imgLiker = itemView.findViewById(R.id.img_liker);
            date = itemView.findViewById(R.id.liked_on);
            liker = itemView.findViewById(R.id.liked_by);
        }

        public void bind(int position) {
            LikeDTO like = likes.get(position);

            FirebaseImageHelper.loadImageFromUrlInto(mContext, like.getLikerPic(), imgLiker);
            date.setText(dateFormat.format(new Date(like.getLikedOn())));
            liker.setText(like.getLikedByName());
        }
    }
}
